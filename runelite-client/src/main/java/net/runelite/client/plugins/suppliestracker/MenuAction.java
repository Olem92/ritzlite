package net.runelite.client.plugins.suppliestracker;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.Item;

/**
 * Data class that tracks all info related to a menu click action
 */
@AllArgsConstructor
public class MenuAction {

    @Getter
    private ActionType type;
    @Getter
    private Item[] oldInventory;

    public static class ItemAction extends MenuAction {

        @Getter
        private int itemID;
        @Getter
        private int slot;

        public ItemAction(ActionType type, Item[] oldInventory, int itemID, int slot) {
            super(type, oldInventory);
            this.itemID = itemID;
            this.slot = slot;
        }

    }

}