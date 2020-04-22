package net.runelite.client.plugins.antidrag;

import com.google.inject.Provides;

import javax.inject.Inject;

import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.FocusChanged;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

@PluginDescriptor(
        name = "Anti Drag",
        enabledByDefault = false,
        description = "Prevent dragging an item for a specified delay",
        tags = {"antidrag", "delay", "inventory", "items", "ritzlite"}
)
public class AntiDragPlugin extends Plugin {
    private static final int DEFAULT_DELAY = 5;

    @Inject
    private Client client;

    @Inject
    private ClientUI clientUI;

    @Inject
    private AntiDragConfig config;

    @Inject
    private AntiDragOverlay overlay;

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private KeyManager keyManager;

    private boolean toggleDrag;

    private final HotkeyListener toggleListener = new HotkeyListener(() -> config.key()) {
        @Override
        public void hotkeyPressed() {
            toggleDrag = !toggleDrag;
            if (toggleDrag) {
                if (config.overlay()) {
                    overlayManager.add(overlay);
                }
                if (config.changeCursor()) {
                    clientUI.setCursor(config.selectedCursor().getCursorImage(), config.selectedCursor().toString());
                }

                final int delay = config.dragDelay();
                client.setInventoryDragDelay(delay);
                setBankDragDelay(delay);
            } else {
                overlayManager.remove(overlay);
                client.setInventoryDragDelay(DEFAULT_DELAY);
                // In this case, 0 is the default for bank item widgets.
                setBankDragDelay(0);
                clientUI.resetCursor();
            }
        }
    };

    private final HotkeyListener holdListener = new HotkeyListener(() -> config.key()) {
        @Override
        public void hotkeyPressed() {
            if (config.overlay()) {
                overlayManager.add(overlay);
            }
            if (config.changeCursor()) {
                clientUI.setCursor(config.selectedCursor().getCursorImage(), config.selectedCursor().toString());
            }

            final int delay = config.dragDelay();
            client.setInventoryDragDelay(delay);
            setBankDragDelay(delay);
        }

        public void hotkeyReleased() {
            overlayManager.remove(overlay);
            client.setInventoryDragDelay(DEFAULT_DELAY);
            setBankDragDelay(DEFAULT_DELAY);
            clientUI.resetCursor();
        }
    };

    @Provides
    AntiDragConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(AntiDragConfig.class);
    }

    @Override
    protected void startUp() {
        overlay.setColor(config.color());
        updateKeyListeners();

        if (config.alwaysOn()) {
            client.setInventoryDragDelay(config.dragDelay());
        }
    }

    @Override
    protected void shutDown() {
        client.setInventoryDragDelay(DEFAULT_DELAY);
        keyManager.unregisterKeyListener(holdListener);
        keyManager.unregisterKeyListener(toggleListener);
        toggleDrag = false;
        overlayManager.remove(overlay);
        clientUI.resetCursor();
    }

    @Subscribe
    private void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("antiDrag")) {
            switch (event.getKey()) {
                case "toggleKeyBind":
                case "holdKeyBind":
                    updateKeyListeners();
                    break;
                case "alwaysOn":
                    client.setInventoryDragDelay(config.alwaysOn() ? config.dragDelay() : DEFAULT_DELAY);
                    break;
                case "dragDelay":
                    if (config.alwaysOn()) {
                        client.setInventoryDragDelay(config.dragDelay());
                    }
                    break;
                case ("changeCursor"):
                    clientUI.resetCursor();
                    break;
                case ("color"):
                    overlay.setColor(config.color());
                    break;
            }
        }
    }

    @Subscribe
    private void onGameStateChanged(GameStateChanged event) {
        if (event.getGameState() == GameState.LOGIN_SCREEN) {
            keyManager.unregisterKeyListener(toggleListener);
            keyManager.unregisterKeyListener(holdListener);
        } else if (event.getGameState() == GameState.LOGGING_IN) {
            updateKeyListeners();
        }
    }

    @Subscribe
    private void onFocusChanged(FocusChanged focusChanged) {
        if (!focusChanged.isFocused() && config.reqFocus() && !config.alwaysOn()) {
            client.setInventoryDragDelay(DEFAULT_DELAY);
            setBankDragDelay(DEFAULT_DELAY);
            overlayManager.remove(overlay);
        }
    }

    private void updateKeyListeners() {
        if (config.holdKeyBind()) {
            keyManager.registerKeyListener(holdListener);
        } else {
            keyManager.unregisterKeyListener(holdListener);
        }

        if (config.toggleKeyBind()) {
            keyManager.registerKeyListener(toggleListener);
        } else {
            keyManager.unregisterKeyListener(toggleListener);
        }
    }

    private void setBankDragDelay(int delay) {
        final Widget bankItemContainer = client.getWidget(WidgetInfo.BANK_ITEM_CONTAINER);
        if (bankItemContainer != null) {
            Widget[] items = bankItemContainer.getDynamicChildren();
            for (Widget item : items) {
                item.setDragDeadTime(delay);
            }
        }
    }
}
