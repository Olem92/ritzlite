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
                case 114://29758://black Crystal
                    name = "White";
                    break;
                case 29761://Yellow Crystal
                    name = "Blue (Magic)";
                    break;
                case 29759://Cyan (Blue) Crystal
                    name = "Red (Melee)";
                    break;
                case 29760://Magenta (Purple) Crystal
                    name = "Green (Ranged)";
                    break;
                default:
                    name = "Unknown";
            }
//test this more
            if (config.showText()) {
                if (config.doCrab() && name.equals("White")) {
                    renderText(graphics, object, name, config.textColor(), object.getPlane() + 100);
                    System.out.println("here1");
                } /*else if (config.doCrab() && name.equals("Blue (Magic)")) {
                    renderText(graphics, object, name, config.textColor(), object.getPlane() + 100);//250
                    System.out.println("here2");
                } else if (config.doCrab() && name.equals("Red (Melee)")) {
                    renderText(graphics, object, name, config.textColor(), object.getPlane() + 100);
                    System.out.println("here3");
                } else if (config.doCrab() && name.equals("Green (Ranged)")) {
                    renderText(graphics, object, name, config.textColor(), object.getPlane() + 100);
                    System.out.println("here4");
                }*/
            }

                /*else if (config.crabText() && name.equals("Blue (Magic)")) {
                renderText(graphics, object, name, config.textColor(), object.getPlane() + 100);
                System.out.println("here2");
            } else if (config.crabText() && name.equals("Red (Melee)")) {
                renderText(graphics, object, name, config.textColor(), object.getPlane() + 100);
                System.out.println("here3");
            } else if (config.crabText() && name.equals("Green (Ranged)")) {
                renderText(graphics, object, name, config.textColor(), object.getPlane() + 100);
                System.out.println("here4");
            }*/

          /*  if (config.showText()) {
                if (config.doBoulder() && name.equals("Boulder")) {
                    renderText(graphics, object, name, config.textColor(), object.getPlane() + 100);
                } else if (config.doTree() && name.equals("Tree")) {
                    renderText(graphics, object, name, config.textColor(), object.getPlane() + 100);//250
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
