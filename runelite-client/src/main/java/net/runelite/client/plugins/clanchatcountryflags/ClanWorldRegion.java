package net.runelite.client.plugins.clanchatcountryflags;

import com.google.common.collect.ImmutableMap;
import java.awt.image.BufferedImage;
import java.util.Map;
import net.runelite.client.util.ImageUtil;

enum ClanWorldRegion
{
	// Follow ISO 3166-1 alpha-2 for country codes
	FLAG_US(0),
	FLAG_GB(1),
	FLAG_AU(3),
	FLAG_DE(7);

	private static final Map<Integer, ClanWorldRegion> worldRegionMap;

	private final int regionId;

	static
	{
		ImmutableMap.Builder<Integer, ClanWorldRegion> builder = new ImmutableMap.Builder<>();

		for (final ClanWorldRegion worldRegion : values())
		{
			builder.put(worldRegion.regionId, worldRegion);
		}
		worldRegionMap = builder.build();
	}

	ClanWorldRegion(int regionId)
	{
		this.regionId = regionId;
	}

	BufferedImage loadImage()
	{
		return ImageUtil.getResourceStreamFromClass(getClass(), "/" + this.name().toLowerCase() + ".png");
	}

	static ClanWorldRegion getByRegionId(int regionId)
	{
		return worldRegionMap.get(regionId);
	}
}
