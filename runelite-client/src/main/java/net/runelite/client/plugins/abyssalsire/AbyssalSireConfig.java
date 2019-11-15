package net.runelite.client.plugins.abyssalsire;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("abyssalsire")
public interface AbyssalSireConfig extends Config {
    @ConfigItem(
            keyName = "walkUnderSpawn",
            name = "Walk under Spawns",
            description = "Left-click to walk under Spawns at Abyssal Sire",
            position = 1
    )
    default boolean walkUnderSpawn() {
        return true;
    }

    @ConfigItem(
            keyName = "walkUnderScion",
            name = "Walk under Scions",
            description = "Left-click to walk under Scions at Abyssal Sire",
            position = 2
    )
    default boolean walkUnderScion() {
        return true;
    }
}