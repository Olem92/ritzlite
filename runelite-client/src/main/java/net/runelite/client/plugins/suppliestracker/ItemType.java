package net.runelite.client.plugins.suppliestracker;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The potential types that supplies can be along with a categorization function
 * that assigns the supplies to these categories
 */
@AllArgsConstructor
public enum ItemType {
    FOOD("Food"),
    POTION("Potions"),
    RUNE("Runes"),
    AMMO("Ammo"),
    TELEPORT("Teleports");

    @Getter
    private String label;

    /**
     * Takes an item and determines what ItemType it should categorize into
     *
     * @param item the item to determine category for
     * @return our best guess for what category this item goes into
     * note that if the guess is wrong (per say) it won't break anything because it will be
     * consistently wrong but it could have an item that is clearly not food in the food section
     */
    public static ItemType categorize(SuppliesTrackerItem item) {
        if (item.getName().contains("(4)")) {
            return ItemType.POTION;
        }
        if (item.getName().toLowerCase().contains("bolt") || item.getName().toLowerCase().contains("dart")
                || item.getName().toLowerCase().contains("arrow") || item.getName().toLowerCase().contains("javelin")
                || item.getName().toLowerCase().contains("knive") || item.getName().toLowerCase().contains("throwing")
                || item.getName().toLowerCase().contains("zulrah's scale") || item.getName().toLowerCase().contains("cannonball")) {
            return ItemType.AMMO;
        }
        if (item.getName().toLowerCase().contains("rune")) {
            return ItemType.RUNE;
        }
        if (item.getName().toLowerCase().contains("teleport")) {
            return ItemType.TELEPORT;
        }
        return ItemType.FOOD;
    }
}