package net.runelite.client.plugins.currentworld;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
        name = "Current World",
        description = "Show the current world as an overlay",
        tags = {"world", "overlay", "ping", "ritzlite"},
        enabledByDefault = false
)
public class CurrentWorldPlugin extends Plugin
{
    @Inject
    private OverlayManager overlayManager;

    @Inject
    private CurrentWorldOverlay overlay;

    @Inject
    private CurrentWorldConfig config;

    @Provides
    CurrentWorldConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(CurrentWorldConfig.class);
    }

    @Override
    protected void startUp() throws Exception
    {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(overlay);
    }
}
