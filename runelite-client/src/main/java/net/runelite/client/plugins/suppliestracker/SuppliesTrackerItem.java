package net.runelite.client.plugins.suppliestracker;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
class SuppliesTrackerItem {
    @Getter
    private int id;
    @Getter
    private String name;
    @Getter
    private int quantity;
    @Getter
    private long price;
}