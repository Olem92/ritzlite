package net.runelite.client.plugins.antidrag;

import java.awt.image.BufferedImage;
import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.client.plugins.customcursor.CustomCursorPlugin;
import net.runelite.client.util.ImageUtil;

public enum CustomCursor
{
	RS3_GOLD("RS3 Gold", "cursor-rs3-gold.png"),
	RS3_SILVER("RS3 Silver", "cursor-rs3-silver.png"),
	DRAGON_DAGGER("Dragon Dagger", "cursor-dragon-dagger.png"),
	DRAGON_DAGGER_POISON("Dragon Dagger (p)", "cursor-dragon-dagger-p.png"),
	TROUT("Trout", "cursor-trout.png"),
	DRAGON_SCIMITAR("Dragon Scimitar", "cursor-dragon-scimitar.png"),
	ARMADYL_GODSWORD("Armadyl Godsword", "cursor-armadyl-godsword.png"),
	BANDOS_GODSWORD("Bandos Godsword", "cursor-bandos-godsword.png"),
	MOUSE("Mouse", "cursor-mouse.png"),
	SARADOMIN_GODSWORD("Saradomin Godsword", "cursor-saradomin-godsword.png"),
	ZAMORAK_GODSWORD("Zamorak Godsword", "cursor-zamorak-godsword.png"),
	SKILL_SPECS("Skill Specs", "cursor-skill-specs.png");

	private final String name;

	@Getter(AccessLevel.PACKAGE)
	private final BufferedImage cursorImage;

	CustomCursor(String name, String icon)
	{
		this.name = name;
		this.cursorImage = ImageUtil.getResourceStreamFromClass(CustomCursorPlugin.class, icon);
	}

	@Override
	public String toString()
	{
		return name;
	}
}