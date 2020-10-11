package net.runelite.client.plugins.environmenteffects;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

/**
 * @author Danny
 */

@ConfigGroup("environmenteffects")
public interface EnvironmentConfig extends Config {

    @ConfigItem(
            position = 0,
            keyName = "dungeonLight",
            name = "Night-vision",
            description = "Removes the on-screen darkening effect in caves and dungeons",
            warning = "This feature will remove the darkening effect in certain areas\nbut will NOT stop the player from taking damage!"

    )
    default boolean nightVision() {
        return false;
    }

    @ConfigItem(
            position = 1,
            keyName = "smokeDungeon",
            name = "Remove smoke",
            description = "Removes the on-screen smoke effect in the Pollnivneach Smoke Dungeon & Thermonuclear Smoke Dungeon"
    )
    default boolean smokeEffect() {
        return false;
    }

    @ConfigItem(
            position = 2,
            keyName = "snowTrollheim",
            name = "Remove snow",
            description = "Removes the on-screen snow effect north of Trollheim & God Wars entrance"
    )
    default boolean snowEffect() {
        return false;
    }

    @ConfigItem(
            position = 3,
            keyName = "zamorakDarkness",
            name = "Remove darkness",
            description = "Removes the on-screen darkening effect in the Zamorak chamber of GWD"
    )
    default boolean zamorakEffect() {
        return false;
    }
/*
       @ConfigItem(
               position = 4,
               keyName = "fishingTrawler",
               name = "Fishing Trawler",
               description = "Disable the camera bobbing effect in the Fishing Trawler minigame"
       )
       default boolean fishingTrawler() {
           return false;
       }
       @ConfigItem(
               position = 5,
               keyName = "olmShake",
               name = "CoX earthquake",
               description = "Disable the camera shake effect during the fight against Olm in Chambers of Xeric"
       )
       default boolean olmShake() {
           return false;
       }
*/
}
