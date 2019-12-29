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
    private static final int CRABS_REGION_ID_1 = 13139;
    private static final int CRABS_REGION_ID_2 = 13395;

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
    //here
        if (!client.isInInstancedRegion() || client.getMapRegions()[0] != CRABS_REGION_ID_1
                || client.getMapRegions()[0] != CRABS_REGION_ID_2) {
            return null;
        }

        for (TileObject object : plugin.getObjects()) {
            if (object.getPlane() != client.getPlane()) {
                continue;
            }
            final Shape polygon;
            if (object instanceof GameObject) {
                polygon = ((GameObject) object).getConvexHull();
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

            //if (config.showText()) {
                if (config.showText() && name.equals("White")) {
                    renderText(graphics, object, name, config.textColor(), object.getPlane() + 100);
                } else if (config.showText() && name.equals("Blue (Magic)")) {
                    renderText(graphics, object, name, config.textColor(), object.getPlane() + 100);
                } else if (config.showText() && name.equals("Red (Melee)")) {
                    renderText(graphics, object, name, config.textColor(), object.getPlane() + 100);
                } else if (config.showText() && name.equals("Green (Ranged)")) {
                    renderText(graphics, object, name, config.textColor(), object.getPlane() + 100);
                }
            }
       // }
        return null;
    }

    private void renderText(Graphics2D graphics, TileObject object, String name, Color color, int z) {
        Point textLocation = object.getCanvasTextLocation(graphics, name, z);
        if (textLocation != null) {
            OverlayUtil.renderTextLocation(graphics, textLocation, name, color);
        }
    }
}
