package net.runelite.client.plugins.clanchatcountryflags;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.IndexedSprite;
import net.runelite.api.ScriptID;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ScriptPostFired;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.WorldService;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.ImageUtil;
import net.runelite.http.api.worlds.World;
import net.runelite.http.api.worlds.WorldResult;
import org.apache.commons.lang3.ArrayUtils;

@Slf4j
@PluginDescriptor(
	name = "Clan Chat Country flags"
)
public class ClanChatCountryFlagsPlugin extends Plugin
{
	private int modIconsStart = -1;

	@Inject
	private Client client;

	@Inject
	private WorldService worldService;

	@Inject
	private ClientThread clientThread;

	@Override
	protected void startUp() throws Exception
	{
		loadRegionIcons();
		clientThread.invoke(() -> toggleWorldsToFlags(true));
	}

	@Override
	protected void shutDown() throws Exception
	{
		unloadRegionIcons();
		clientThread.invoke(() -> toggleWorldsToFlags(false));
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			loadRegionIcons();
		}
	}

	@Subscribe
	public void onScriptPostFired(ScriptPostFired event)
	{
		if (event.getScriptId() != ScriptID.FRIENDS_CHAT_CHANNEL_REBUILD)
		{
			return;
		}
		clientThread.invoke(() -> toggleWorldsToFlags(true));
	}

	private void loadRegionIcons()
	{
		final IndexedSprite[] modIcons = client.getModIcons();

		if (modIconsStart != -1 || modIcons == null)
		{
			return;
		}

		final ClanWorldRegion[] worldRegions = ClanWorldRegion.values();
		final IndexedSprite[] newModIcons = Arrays.copyOf(modIcons, modIcons.length + worldRegions.length);
		modIconsStart = modIcons.length;

		for (int i = 0; i < worldRegions.length; i++)
		{
			final ClanWorldRegion worldRegion = worldRegions[i];

			final BufferedImage image = worldRegion.loadImage();
			final IndexedSprite sprite = ImageUtil.getImageIndexedSprite(image, client);
			newModIcons[modIconsStart + i] = sprite;
		}

		log.debug("Loaded region icons");
		client.setModIcons(newModIcons);
	}

	private void unloadRegionIcons()
	{
		final IndexedSprite[] modIcons = client.getModIcons();

		if (modIconsStart == -1 || modIcons == null)
		{
			return;
		}

		final ClanWorldRegion[] worldRegions = ClanWorldRegion.values();
		//Icons that were loaded before region icons were loaded
		final IndexedSprite[] oldModIcons = Arrays.copyOf(modIcons, modIconsStart);

		//Icons that were loaded after region icons were loaded
		final IndexedSprite[] futureModIcons = Arrays.copyOfRange(modIcons, modIconsStart + worldRegions.length,
			modIcons.length);

		//Array without the region icons
		final IndexedSprite[] newModIcons = ArrayUtils.addAll(oldModIcons, futureModIcons);

		modIconsStart = -1;
		log.debug("Unloaded region icons");
		client.setModIcons(newModIcons);
	}

	private void toggleWorldsToFlags(boolean worldsToFlags)
	{
		Widget clanChatList = client.getWidget(WidgetInfo.FRIENDS_CHAT_LIST);
		if (clanChatList == null || clanChatList.getChildren() == null)
		{
			return;
		}

		if (worldsToFlags)
		{
			changeWorldsToFlags(clanChatList);
		}
		else
		{
			changeFlagsToWorlds(clanChatList);
		}
	}

	private void changeWorldsToFlags(Widget clanChatList)
	{
		final WorldResult worldResult = worldService.getWorlds();
		// Iterate every 3 widgets, since the order of widgets is name, world, icon
		for (int i = 1; i < clanChatList.getChildren().length; i += 3)
		{
			Widget listWidget = clanChatList.getChild(i);
			final String worldString = listWidget.getText();
			// In case the string already contains a country flag
			if (!worldString.matches("^World\\s?.*$"))
			{
				continue;
			}
			final int worldNumber = Integer.parseInt(listWidget.getText().replace("World ", ""));

			final World clanMemberWorld = worldResult.findWorld(worldNumber);
			if (clanMemberWorld == null)
			{
				continue;
			}

			final int worldRegionId = clanMemberWorld.getLocation(); // 0 - us, 1 - gb, 3 - au, 7 - de
			final int regionModIconId = ClanWorldRegion.getByRegionId(worldRegionId).ordinal() + modIconsStart;

			listWidget.setText(worldNumber + " <img=" + (regionModIconId) + ">");
		}
	}

	private void changeFlagsToWorlds(Widget clanChatList)
	{
		// Iterate every 3 widgets, since the order of widgets is name, world, icon
		for (int i = 1; i < clanChatList.getChildren().length; i += 3)
		{
			Widget listWidget = clanChatList.getChild(i);
			final String worldString = listWidget.getText();
			// In case the string already has been changed back to World
			if (!worldString.matches("^.*\\s?<img=\\d+>$"))
			{
				continue;
			}

			final String worldNum = listWidget.getText().replaceAll("\\s?<img=\\d+>$", "");
			listWidget.setText("World " + worldNum);
		}
	}
}
