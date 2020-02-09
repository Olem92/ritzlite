
package net.runelite.client.plugins.profiles;

import com.google.inject.Provides;
import java.awt.image.BufferedImage;
import java.util.concurrent.ScheduledExecutorService;
import javax.inject.Inject;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;


@PluginDescriptor(
	name = "Account Switcher",
	description = "Allow for a allows you to easily switch between multiple OSRS Accounts",
	tags = {"profile", "account", "login", "log in", "ritzlite"}
)
public class ProfilesPlugin extends Plugin
{
	@Inject
	private ClientToolbar clientToolbar;

	@Inject
	private ProfilesConfig config;

	@Inject
	private ScheduledExecutorService executorService;

	private ProfilesPanel panel;
	private NavigationButton navButton;

	@Provides
	ProfilesConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ProfilesConfig.class);
	}

	@Override
	protected void startUp()
	{
		panel = injector.getInstance(ProfilesPanel.class);
		panel.init();

		final BufferedImage icon = ImageUtil.getResourceStreamFromClass(getClass(), "profiles_icon.png");

		navButton = NavigationButton.builder()
			.tooltip("Profiles")
			.icon(icon)
			.priority(2)
			.panel(panel)
			.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown()
	{
		clientToolbar.removeNavigation(navButton);
	}

	@Subscribe
	private void onGameStateChanged(GameStateChanged event)
	{
		if (!config.switchPanel())
		{
			return;
		}
		if (event.getGameState().equals(GameState.LOGIN_SCREEN))
		{
			if (!navButton.isSelected())
			{
				OpenPanel(true);
			}
		}
	}

	@Subscribe
	private void onConfigChanged(ConfigChanged event) throws Exception
	{
		if (event.getGroup().equals("profiles") && event.getKey().equals("rememberPassword"))
		{
			panel = injector.getInstance(ProfilesPanel.class);
			this.shutDown();
			this.startUp();
		}
		if (event.getGroup().equals("profiles") && !event.getKey().equals("rememberPassword"))
		{
			panel = injector.getInstance(ProfilesPanel.class);
			panel.redrawProfiles();
		}
	}

	private void OpenPanel(boolean openPanel)
	{
		if (openPanel && config.switchPanel())
		{
			// If we haven't seen the latest feed item,
			// open the feed panel.
			navButton.getOnSelect().run();
		}
	}

}
