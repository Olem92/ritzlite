package net.runelite.client.plugins.cox;

import net.runelite.api.Client;
import net.runelite.api.GraphicID;
import net.runelite.api.GraphicsObject;
import net.runelite.api.Perspective;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;

public class LightningOverlay extends Overlay {
    private static final int OLM_REGION_ID = 12889;
    private final Client client;
    private CoxConfig config;

    @Inject
    private LightningOverlay(Client client, CoxConfig config) {
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        setPriority(OverlayPriority.LOW);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!config.lightning()) {
            return null;
        }

        if (!client.isInInstancedRegion() || client.getMapRegions()[0] != OLM_REGION_ID) {
            return null;
        }
        for (GraphicsObject graphicsObject : client.getGraphicsObjects()) {
            Color color = null;

            if (graphicsObject.getId() >= GraphicID.OLM_LIGHTENING) {
                color = Color.RED;
            } else {
                continue;
            }

            LocalPoint lp = graphicsObject.getLocation();
            Polygon poly = Perspective.getCanvasTilePoly(client, lp);

            if (poly != null) {
                OverlayUtil.renderPolygon(graphics, poly, color);
            }
        }
        return null;
    }
}
