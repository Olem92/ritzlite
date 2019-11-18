
package net.runelite.client.plugins.woodcutting;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.coords.WorldPoint;

@AllArgsConstructor
@Getter
class TreeRespawn {
    private final Tree tree;
    private final WorldPoint worldPoint;
    private final Instant startTime;
    private final int respawnTime;
}