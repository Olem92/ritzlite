package net.runelite.client.plugins.cox;

import net.runelite.client.ui.overlay.Overlay;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.Point;
import net.runelite.api.TileObject;
import net.runelite.client.ui.overlay.*;

import javax.inject.Inject;
import java.awt.*;

public class CrabsOverlay extends Overlay {

    private final Client client;
    private final CoxConfig config;
    private final CoxPlugin plugin;

    @Inject
    private CrabsOverlay(Client client, CoxConfig config, CoxPlugin plugin) {
        this.client = client;
        this.config = config;
        this.plugin = plugin;
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.LOW);
        setLayer(OverlayLayer.ABOVE_SCENE);
    }

    @Override
    public Dimension render(Graphics2D graphics) {

        for (TileObject object : plugin.getObjects()) {
            if (object.getPlane() != client.getPlane()) {
                continue;
            }
            final Polygon polygon;
            if (object instanceof GameObject) {
                polygon = (Polygon) ((GameObject) object).getConvexHull();
            } else {
                polygon = object.getCanvasTilePoly();
            }
            if (polygon == null) {
                continue;
            }
            String name;
            switch (object.getId()) {
                case 29758://black Crystal
                    name = "White";
                    break;
                case 29761://Yellow Crystal
                    name = "Blue";
                    break;
                case 29759://Cyan (Blue) Crystal
                    name = "Red";
                    break;
                case 29760://Magenta (Purple) Crystal
                    name = "Green";
                    break;
                default:
                    name = "Unknown";
            }
          /*  if (config.showText()) {
                if (config.doBoulder() && name.equals("Boulder")) {
                    renderText(graphics, object, name, config.textColor(), object.getPlane() + 100);
                } else if (config.doTree() && name.equals("Tree")) {
                    renderText(graphics, object, name, config.textColor(), object.getPlane() + 250);
                } else if (config.doMining() && name.equals("Rocks")) {
                    renderText(graphics, object, name, config.textColor(), object.getPlane() + 100);
                }
            }
            if (config.showHighlight()) {
                if (config.doBoulder() && name.equals("Boulder")) {
                    OverlayUtil.renderPolygon(graphics, polygon, config.markerColor());
                } else if (config.doTree() && name.equals("Tree")) {
                    OverlayUtil.renderPolygon(graphics, polygon, config.markerColor());
                } else if (config.doMining() && name.equals("Rocks")) {
                    OverlayUtil.renderPolygon(graphics, polygon, config.markerColor());
                }
            }*/
        }
        return null;
    }

    private void renderText(Graphics2D graphics, TileObject object, String name, Color color, int z) {
        Point textLocation = object.getCanvasTextLocation(graphics, name, z);
        if (textLocation != null) {
            OverlayUtil.renderTextLocation(graphics, textLocation, name, color);
        }
    }
}
