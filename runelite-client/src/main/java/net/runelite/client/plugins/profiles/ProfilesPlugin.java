package net.runelite.client.plugins.profiles;

import com.google.inject.Provides;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import java.awt.image.BufferedImage;

@PluginDescriptor(
        name = "Profiles",
        description = "Allow for a faster log in",
        tags = {"profile", "account", "login", "log in", "ritzlite"}
)
public class ProfilesPlugin extends Plugin {
    @Inject
    private ClientToolbar clientToolbar;

    private ProfilesPanel panel;
    private NavigationButton navButton;

    @Provides
    ProfilesConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(ProfilesConfig.class);
    }

    @Override
    protected void startUp() throws Exception {
        panel = injector.getInstance(ProfilesPanel.class);

        final BufferedImage icon = ImageUtil.getResourceStreamFromClass(getClass(), "profiles_icon.png");

        navButton = NavigationButton.builder()
                .tooltip("Profiles")
                .icon(icon)
                .priority(8)
                .panel(panel)
                .build();

        clientToolbar.addNavigation(navButton);
    }

    @Override
    protected void shutDown() {
        clientToolbar.removeNavigation(navButton);
    }

    @Subscribe
    void onConfigChanged(ConfigChanged event) {
        if (event.getGroup().equals("profiles")) {
            panel.redrawProfiles();
        }
    }
}