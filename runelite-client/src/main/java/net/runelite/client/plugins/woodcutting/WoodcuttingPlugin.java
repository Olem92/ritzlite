package net.runelite.client.plugins.woodcutting;

import com.google.inject.Provides;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;

import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectChanged;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.Notifier;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.xptracker.XpTrackerPlugin;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
        name = "Woodcutting",
        description = "Show woodcutting statistics and/or bird nest notifications",
        tags = {"birds", "nest", "notifications", "overlay", "skilling", "wc"}
)
@PluginDependency(XpTrackerPlugin.class)
public class WoodcuttingPlugin extends Plugin {
    @Inject
    private Notifier notifier;

    @Inject
    private Client client;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private WoodcuttingOverlay overlay;

    @Inject
    private WoodcuttingTreesOverlay treesOverlay;

    @Inject
    private WoodcuttingConfig config;

    @Getter
    private WoodcuttingSession session;

    @Getter
    private Axe axe;

    @Getter
    private final Set<GameObject> treeObjects = new HashSet<>();

    @Inject
    private WoodcuttingTimersOverlay timersOverlay;

    @Getter(AccessLevel.PACKAGE)
    private final List<TreeRespawn> respawns = new ArrayList<>();
    private boolean recentlyLoggedIn;

    @Provides
    WoodcuttingConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(WoodcuttingConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        overlayManager.add(overlay);
        overlayManager.add(treesOverlay);
        overlayManager.add(timersOverlay);
    }

    @Override
    protected void shutDown() throws Exception {
        overlayManager.remove(overlay);
        overlayManager.remove(treesOverlay);
        overlayManager.remove(timersOverlay);
        respawns.clear();
        treeObjects.clear();
        session = null;
        axe = null;
    }

    @Subscribe
    public void onGameTick(GameTick gameTick) {
        recentlyLoggedIn = false;

        if (session == null || session.getLastLogCut() == null) {
            return;
        }

        Duration statTimeout = Duration.ofMinutes(config.statTimeout());
        Duration sinceCut = Duration.between(session.getLastLogCut(), Instant.now());

        if (sinceCut.compareTo(statTimeout) >= 0) {
            session = null;
            axe = null;
        }
    }

    @Subscribe
    public void onChatMessage(ChatMessage event) {
        if (event.getType() == ChatMessageType.SPAM || event.getType() == ChatMessageType.GAMEMESSAGE) {
            if (event.getMessage().startsWith("You get some") && (event.getMessage().endsWith("logs.") || event.getMessage().endsWith("mushrooms."))) {
                if (session == null) {
                    session = new WoodcuttingSession();
                }

                session.setLastLogCut();
            }

            if (event.getMessage().contains("A bird's nest falls out of the tree") && config.showNestNotification()) {
                notifier.notify("A bird nest has spawned!");
            }
        }
    }

    @Subscribe
    public void onGameObjectSpawned(final GameObjectSpawned event) {
        GameObject gameObject = event.getGameObject();
        Tree tree = Tree.findTree(gameObject.getId());

        if (tree != null) {
            treeObjects.add(gameObject);
        }
    }

    @Subscribe
    public void onGameObjectDespawned(final GameObjectDespawned event) {
        if (client.getGameState() != GameState.LOGGED_IN || recentlyLoggedIn) {
            return;
        }

        final GameObject object = event.getGameObject();

        Tree tree = Tree.findTree(object.getId());
        if (tree != null) {
            TreeRespawn treeRespawn = new TreeRespawn(tree, object.getWorldLocation(), Instant.now(), (int) tree.getRespawnTime().toMillis());
            respawns.add(treeRespawn);
        }
        treeObjects.remove(event.getGameObject());
    }

    @Subscribe
    public void onGameObjectChanged(final GameObjectChanged event) {
        treeObjects.remove(event.getGameObject());
        respawns.clear();
    }

    @Subscribe
    public void onGameStateChanged(final GameStateChanged event) {
        switch (event.getGameState()) {
            case LOADING:
            case HOPPING:
                respawns.clear();
                treeObjects.clear();
                break;
            case LOGGED_IN:
                // After login trees that are depleted will be changed,
                // waits for the next game tick before watching for
                // trees to deplete
                recentlyLoggedIn = true;
                break;
        }
    }

    @Subscribe
    public void onAnimationChanged(final AnimationChanged event) {
        Player local = client.getLocalPlayer();

        if (event.getActor() != local) {
            return;
        }

        int animId = local.getAnimation();
        Axe axe = Axe.findAxeByAnimId(animId);
        if (axe != null) {
            this.axe = axe;
        }
    }
}