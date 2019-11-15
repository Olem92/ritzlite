package net.runelite.client.plugins.cox;

import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.*;

@ConfigGroup("cox")
public interface CoxConfig extends Config {
    @Alpha
    @ConfigItem(
            position = 0,
            keyName = "shadow",
            name = "Olm Shadow Color",
            description = "Color of his shadow"
    )
    default Color shadow() {
        return Color.red;
    }

    @ConfigItem(
            position = 1,
            keyName = "rocks",
            name = "Falling Crystals",
            description = "Display tile indicators for falling crystals at cox"
    )
    default boolean rocks() {
        return false;
    }

    @ConfigItem(
            position = 2,
            keyName = "lightning",
            name = "Lightning",
            description = "Display tile indicators for lightning at cox"
    )
    default boolean lightning() {
        return false;
    }

    @ConfigItem(
            position = 3,
            keyName = "crystals",
            name = "Crystals",
            description = "Display tile indicators for crystals at cox"
    )
    default boolean crystals() {
        return false;
    }

    //here
    @ConfigItem(
            keyName = "showHighlight",
            name = "Highlight Shortcuts",
            description = "Shows shortcuts",
            position = 4
    )
    default boolean showHighlight() {
        return true;
    }

    @ConfigItem(
            keyName = "markerColor",
            name = "Marker color",
            description = "Configures the color shortcut color",
            position = 5
    )
    default Color markerColor() {
        return Color.white;
    }

    @ConfigItem(
            keyName = "showText",
            name = "Show text",
            description = "Shows name of shortcuts",
            position = 6
    )
    default boolean showText() {
        return true;
    }

    @ConfigItem(
            keyName = "doBoulder",
            name = "Boulder",
            description = "Shows boulder",
            position = 7
    )
    default boolean doBoulder() {
        return true;
    }

    @ConfigItem(
            keyName = "doTree",
            name = "Woodcutting",
            description = "Shows tree",
            position = 8
    )
    default boolean doTree() {
        return true;
    }

    @ConfigItem(
            keyName = "doMining",
            name = "Mining",
            description = "Shows Rocks",
            position = 9
    )
    default boolean doMining() {
        return true;
    }

    @ConfigItem(
            keyName = "textColor",
            name = "Text color",
            description = "Configures the color of text marker",
            position = 10
    )
    default Color textColor() {
        return Color.red;
    }


}
