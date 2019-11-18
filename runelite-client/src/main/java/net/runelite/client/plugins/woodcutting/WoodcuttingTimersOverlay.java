package net.runelite.client.plugins.woodcutting;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import javax.inject.Inject;

import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.ProgressPieComponent;

class WoodcuttingTimersOverlay extends Overlay {
    // Regular trees have a respawn time that ranges between 36-60 seconds
    static final int TREE_MAX_RESPAWN_TIME = 60;
    private static final int TREE_MIN_RESPAWN_TIME = 36;
    private static final float TREE_RANDOM_PERCENT_THRESHOLD = (float) TREE_MIN_RESPAWN_TIME / TREE_MAX_RESPAWN_TIME;
    private static final Color DARK_GREEN = new Color(0, 100, 0);

    private final Client client;
    private final WoodcuttingPlugin plugin;
    private final WoodcuttingConfig config;

    @Inject
    private WoodcuttingTimersOverlay(Client client, WoodcuttingPlugin plugin, WoodcuttingConfig config) {
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);
        this.plugin = plugin;
        this.client = client;
        this.config = config;
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (!config.showRespawnTimers()) {
            return null;
        }

        List<TreeRespawn> respawns = plugin.getRespawns();
        if (respawns.isEmpty()) {
            return null;
        }

        Instant now = Instant.now();
        for (Iterator<TreeRespawn> it = respawns.iterator(); it.hasNext(); ) {
            Color pieFillColor = Color.YELLOW;
            Color pieBorderColor = Color.ORANGE;
            TreeRespawn treeRespawn = it.next();
            float percent = (now.toEpochMilli() - treeRespawn.getStartTime().toEpochMilli()) / (float) treeRespawn.getRespawnTime();
            WorldPoint worldPoint = treeRespawn.getWorldPoint();
            LocalPoint loc = LocalPoint.fromWorld(client, worldPoint);

            // This is used to get the X and Y for centre location to work
            LocalPoint tempLoc = LocalPoint.fromWorld(client, worldPoint.getX(), worldPoint.getY());
            LocalPoint centreLoc = new LocalPoint(
                    tempLoc.getX() + Perspective.LOCAL_TILE_SIZE * (client.getPlane() - 1) / 2,
                    tempLoc.getY() + Perspective.LOCAL_TILE_SIZE * (client.getPlane() - 1) / 2);
            if (loc == null || percent > 1.0f) {
                it.remove();
                continue;
            }

            Point centrePoint = Perspective.localToCanvas(client, centreLoc, client.getPlane());
            Point point = Perspective.localToCanvas(client, loc, client.getPlane());
            if (point == null || centrePoint == null) {
                it.remove();
                continue;
            }

            Tree tree = treeRespawn.getTree();

            // Recolour pie on regular trees within the range they spawn
            if (tree == Tree.REGULAR_TREES && percent > TREE_RANDOM_PERCENT_THRESHOLD) {
                pieFillColor = Color.GREEN;
                pieBorderColor = DARK_GREEN;
            }

            // The pie is already centred on these trees as they are on one tile
            if (tree == Tree.OAK_TREES || tree == Tree.YEW_TREES || tree == Tree.MAHOGANY_TREES || tree == Tree.TEAK_TREES || tree == Tree.REDWOODS) {
                point = point;
            }

            // Centre the pie on these trees as they are spread over multiple tiles
            if (tree == Tree.MAGIC_TREES || tree == Tree.MAPLE_TREES || tree == Tree.WILLOW_TREES || tree == Tree.REGULAR_TREES) {
                point = centrePoint;
            }

            ProgressPieComponent ppc = new ProgressPieComponent();
            ppc.setBorderColor(pieBorderColor);
            ppc.setFill(pieFillColor);
            ppc.setPosition(point);
            ppc.setProgress(percent);
            ppc.render(graphics);
        }
        return null;
    }
}