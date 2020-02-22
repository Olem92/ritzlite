package net.runelite.client.plugins.cox;

import com.google.inject.Provides;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.TileObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@PluginDescriptor(
        name = "Raids",
        description = "Show cox plugins",
        tags = {"cox", "raids", "shadow", "ritz", "rocks", "lightening", "crystals", "crabs", "olm", "ritzlite"},
        enabledByDefault = false
)
public class CoxPlugin extends Plugin {
    public Date time;
    public boolean showShadow;
    public int ID = 7551;
    //public int ID = 2130;

    @Inject
    private Client client;

    @Getter(AccessLevel.PACKAGE)
    private final List<TileObject> objects = new ArrayList<>();
    public boolean inRaid;

    @Inject
    private KeyManager keyManager;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private CoxConfig config;

    @Provides
    CoxConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(CoxConfig.class);
    }

    @Inject
    private CoxOverlay overlay;

    @Inject
    private FallingRocksOverlay rocks;

    @Inject
    private LightningOverlay lightning;

    @Inject
    private CrabsOverlay crabs;

    @Override
    protected void startUp() {
        overlayManager.add(overlay);
        overlayManager.add(rocks);
        overlayManager.add(lightning);
        overlayManager.add(crabs);
        inRaid = false;
    }

    @Override
    protected void shutDown() {
        overlayManager.remove(overlay);
        overlayManager.remove(rocks);
        overlayManager.remove(lightning);
        overlayManager.remove(crabs);
        inRaid = false;
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event) {
        final WorldPoint worldPoint = WorldPoint.fromLocalInstance(client, event.getGameObject().getLocalLocation());
        if (worldPoint == null) {
            return;
        }
        if (event.getGameObject().getId() == 29758 ||
                event.getGameObject().getId() == 29761 ||
                event.getGameObject().getId() == 29759 ||
                event.getGameObject().getId() == 29760) {
            objects.add(event.getGameObject());
        }
    }
}
