package net.runelite.client.plugins.abyssalsire;

import com.google.inject.Provides;

import javax.inject.Inject;

import net.runelite.api.Client;

import static net.runelite.api.MenuAction.MENU_ACTION_DEPRIORITIZE_OFFSET;
import static net.runelite.api.MenuAction.WALK;

import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

@PluginDescriptor(
        name = "Abyssal Sire",
        description = "Left-click to walk under Spawns and Scions at Abyssal Sire",
        tags = {"sire", "abyssal", "scion", "spawn", "ritzlite"}
)
public class AbyssalSirePlugin extends Plugin {
    @Inject
    private AbyssalSireConfig config;

    @Inject
    private Client client;

    @Provides
    AbyssalSireConfig getConfig(ConfigManager configManager) {
        return configManager.getConfig(AbyssalSireConfig.class);
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event) {
        final String target = Text.removeTags(event.getTarget()).toLowerCase();
        if ((config.walkUnderSpawn() && target.equals("spawn  (level-60)"))
                || (config.walkUnderScion() && target.equals("scion  (level-100)"))) {
            if (event.getType() < WALK.getId()) {
                MenuEntry[] menuEntries = client.getMenuEntries();
                MenuEntry menuEntry = menuEntries[menuEntries.length - 1];
                menuEntry.setType(event.getType() + MENU_ACTION_DEPRIORITIZE_OFFSET);
                client.setMenuEntries(menuEntries);
            }
        }
    }
}