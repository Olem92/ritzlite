package net.runelite.client.plugins.currentworld;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.EnumSet;
import javax.inject.Inject;

import net.runelite.api.Client;
import net.runelite.api.WorldType;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.ui.overlay.components.LineComponent;

class CurrentWorldOverlay extends Overlay {
    private final Client client;
    private final CurrentWorldConfig config;
    private final PanelComponent panelComponent = new PanelComponent();

    @Inject
    private CurrentWorldOverlay(Client client, CurrentWorldConfig config) {
        setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
        this.client = client;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        panelComponent.getChildren().clear();
        String showWorld = "World: " + Integer.toString(client.getWorld());

        // Build overlay title
        panelComponent.getChildren().add(TitleComponent.builder()
                .text(showWorld)
                .color(Color.lightGray)
                .build());

        // Set the size of the overlay (width)
        panelComponent.setPreferredSize(new Dimension(
                graphics.getFontMetrics().stringWidth(showWorld) + 10,
                0));

        return panelComponent.render(graphics);
    }
}