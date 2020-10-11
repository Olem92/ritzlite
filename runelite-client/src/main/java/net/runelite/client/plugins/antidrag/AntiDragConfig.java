package net.runelite.client.plugins.antidrag;

import java.awt.Color;
import java.awt.event.KeyEvent;
import net.runelite.api.Constants;
import net.runelite.client.config.Alpha;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.ModifierlessKeybind;

@ConfigGroup("antiDrag")
public interface AntiDragConfig extends Config
{
	@ConfigItem(
		position = 0,
		keyName = "alwaysOn",
		name = "Always On",
		description = "Makes the anti-drag always active and disables the hotkey toggle"
	)
	default boolean alwaysOn()
	{
		return false;
	}

	@ConfigItem(
		position = 1,
		keyName = "toggleKeyBind",
		name = "Toggle with Keybind",
		description = "Toggle anti drag on and off, rather than always on."
	)
	default boolean toggleKeyBind()
	{
		return false;
	}

	@ConfigItem(
		position = 2,
		keyName = "holdKeyBind",
		name = "Hold with Keybind",
		description = "Hold anti drag key to turn it on, rather than toggle it on or off."
	)
	default boolean holdKeyBind()
	{
		return false;
	}

	@ConfigItem(
		keyName = "key",
		name = "Keybind",
		description = "The keybind you want to use for antidrag",
		position = 3,
		hidden = true
	)
	default Keybind key()
	{
		return new ModifierlessKeybind(KeyEvent.VK_SHIFT, 0);
	}

	@ConfigItem(
		keyName = "dragDelay",
		name = "Drag Delay",
		description = "Configures the inventory drag delay in client ticks (20ms)",
		position = 4
	)
	default int dragDelay()
	{
		return Constants.GAME_TICK_LENGTH / Constants.CLIENT_TICK_LENGTH; // one game tick
	}

	@ConfigItem(
		keyName = "reqFocus",
		name = "Reset on focus loss",
		description = "Disable antidrag when losing focus (like alt tabbing)",
		position = 5,
		hidden = true
	)
	default boolean reqFocus()
	{
		return false;
	}

	@ConfigItem(
		keyName = "overlay",
		name = "Enable overlay",
		description = "Do you really need a description?",
		position = 6,
		hidden = true
	)
	default boolean overlay()
	{
		return false;
	}

	@Alpha
	@ConfigItem(
		keyName = "color",
		name = "Overlay color",
		description = "Change the overlay color, duh",
		hidden = true,
		position = 7
	)
	default Color color()
	{
		return new Color(255, 0, 0, 30);
	}

	@ConfigItem(
		keyName = "changeCursor",
		name = "Change Cursor",
		description = "Change cursor when you have anti-drag enabled.",
		position = 8,
		hidden = true
	)
	default boolean changeCursor()
	{
		return false;
	}

	@ConfigItem(
		keyName = "cursorStyle",
		name = "Cursor",
		description = "Select which cursor you wish to use",
		hidden = true,
		position = 9
	)
	default CustomCursor selectedCursor()
	{
		return CustomCursor.RS3_GOLD;
	}

	@ConfigItem(
		keyName = "disableOnCtrl",
		name = "Disable On Control Pressed",
		description = "Configures whether to ignore the delay while holding control.",
		position = 3
	)
	default boolean disableOnCtrl()
	{
		return false;
	}
}
