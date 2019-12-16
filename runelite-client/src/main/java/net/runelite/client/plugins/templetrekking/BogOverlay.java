package net.runelite.client.plugins.templetrekking;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Polygon;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

class BogOverlay extends Overlay
{
    private static final Color TILE_HIGHLIGHT_COLOR = Color.GREEN;

    private final Client client;
    private final BogPlugin plugin;

    @Inject
    private BogOverlay(Client client, BogPlugin plugin)
    {
        super(plugin);
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        this.client = client;
        this.plugin = plugin;
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        plugin.getWalkableBogTiles().forEach((object) -> {
            Polygon polygon = object.getCanvasTilePoly();
            if (polygon != null)
            {
                OverlayUtil.renderPolygon(graphics, polygon, TILE_HIGHLIGHT_COLOR);
            }
        });
        return null;
    }
}
