package net.runelite.client.plugins.wintertodt;

import net.runelite.api.Client;
import net.runelite.api.GraphicID;
import net.runelite.api.GraphicsObject;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;

public class AOEOverlay extends Overlay {
    private static final int WINTERTODT_REGION = 6205;
    private final Client client;
    private WintertodtConfig config;

    @Inject
    private AOEOverlay(Client client, WintertodtConfig config) {
        this.client = client;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        setPriority(OverlayPriority.LOW);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        //System.out.println("aoeOverlay: " + config.aoe());
        if (!config.aoe()) {
            return null;
        }
        //System.out.println("isIninstancedRegion: "+client.isInInstancedRegion()+",- Mapregion[0]= "+client.getMapRegions()[0]);
        if (client.getMapRegions()[0] != WINTERTODT_REGION) {
            return null;
        }
        //System.out.println("here2;");
        for (GraphicsObject graphicsObject : client.getGraphicsObjects()) {
            LocalPoint lp = graphicsObject.getLocation();
            Polygon poly = Perspective.getCanvasTilePoly(client, lp);
            Color color = null;
            String text = "";

            if (poly == null) {
                return null;
            }

            if (graphicsObject.getId() >= GraphicID.FALLING_SNOW) {
                color = Color.BLUE;
                text = "Falling Snow";
            } else if (graphicsObject.getId() >= GraphicID.BROKEN_BRAZIER) {
                color = Color.RED;
            }

            if (color != null) {
                OverlayUtil.renderPolygon(graphics, poly, color);
                Point textLocation = Perspective.getCanvasTextLocation(client, graphics, lp, text, 0);
                if (textLocation != null) {
                    OverlayUtil.renderTextLocation(graphics, textLocation, text, color);
                }
            }

        }
        return null;
    }
}
