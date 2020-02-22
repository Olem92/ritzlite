package net.runelite.client.plugins.templetrekking;

import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Tile;
import net.runelite.api.TileObject;
import net.runelite.api.events.DecorativeObjectChanged;
import net.runelite.api.events.DecorativeObjectDespawned;
import net.runelite.api.events.DecorativeObjectSpawned;
import net.runelite.api.events.GameObjectChanged;
import net.runelite.api.events.GameObjectDespawned;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GroundObjectChanged;
import net.runelite.api.events.GroundObjectDespawned;
import net.runelite.api.events.GroundObjectSpawned;
import net.runelite.api.events.WallObjectChanged;
import net.runelite.api.events.WallObjectDespawned;
import net.runelite.api.events.WallObjectSpawned;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
        name = "Temple Trekking",
        description = "Highlights walkable tiles for the bog encounter in the Temple Trekking minigame",
        tags = {"bog", "temple", "trekking", "ritzlite"},
        enabledByDefault = false
)
@Slf4j
public class BogPlugin extends Plugin
{
    private static final int WALKABLE_BOG_TILE_ID = 13838;

    @Getter
    private final List<TileObject> walkableBogTiles = new ArrayList<>();

    @Inject
    private OverlayManager overlayManager;

    @Inject
    private BogOverlay bogOverlay;

    @Inject
    private Client client;

    @Override
    protected void startUp() throws Exception
    {
        overlayManager.add(bogOverlay);
    }

    @Override
    protected void shutDown() throws Exception
    {
        overlayManager.remove(bogOverlay);
    }

    @Subscribe
    public void onGameObjectSpawned(GameObjectSpawned event)
    {
        onTileObject(event.getTile(), null, event.getGameObject());
    }

    @Subscribe
    public void onGameObjectChanged(GameObjectChanged event)
    {
        onTileObject(event.getTile(), event.getPrevious(), event.getGameObject());
    }

    @Subscribe
    public void onGameObjectDespawned(GameObjectDespawned event)
    {
        onTileObject(event.getTile(), event.getGameObject(), null);
    }

    @Subscribe
    public void onGroundObjectSpawned(GroundObjectSpawned event)
    {
        onTileObject(event.getTile(), null, event.getGroundObject());
    }

    @Subscribe
    public void onGroundObjectChanged(GroundObjectChanged event)
    {
        onTileObject(event.getTile(), event.getPrevious(), event.getGroundObject());
    }

    @Subscribe
    public void onGroundObjectDespawned(GroundObjectDespawned event)
    {
        onTileObject(event.getTile(), event.getGroundObject(), null);
    }

    @Subscribe
    public void onWallObjectSpawned(WallObjectSpawned event)
    {
        onTileObject(event.getTile(), null, event.getWallObject());
    }

    @Subscribe
    public void onWallObjectChanged(WallObjectChanged event)
    {
        onTileObject(event.getTile(), event.getPrevious(), event.getWallObject());
    }

    @Subscribe
    public void onWallObjectDespawned(WallObjectDespawned event)
    {
        onTileObject(event.getTile(), event.getWallObject(), null);
    }

    @Subscribe
    public void onDecorativeObjectSpawned(DecorativeObjectSpawned event)
    {
        onTileObject(event.getTile(), null, event.getDecorativeObject());
    }

    @Subscribe
    public void onDecorativeObjectChanged(DecorativeObjectChanged event)
    {
        onTileObject(event.getTile(), event.getPrevious(), event.getDecorativeObject());
    }

    @Subscribe
    public void onDecorativeObjectDespawned(DecorativeObjectDespawned event)
    {
        onTileObject(event.getTile(), event.getDecorativeObject(), null);
    }

    private void onTileObject(Tile tile, TileObject oldObject, TileObject newObject)
    {
        walkableBogTiles.remove(oldObject);
        if (newObject != null && newObject.getId() == WALKABLE_BOG_TILE_ID) {
            walkableBogTiles.add(newObject);
        }
    }
}
