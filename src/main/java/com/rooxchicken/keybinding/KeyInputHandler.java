package com.rooxchicken.keybinding;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import com.rooxchicken.screen.ConfigScreen;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

public class KeyInputHandler
{
    private static ArrayList<Keybind> bindings;
	private static KeyBinding configKey = new KeyBinding("key.ckb.config", GLFW.GLFW_KEY_C, "key.category.ckb");
	
	public static void registerKeyInputs(ArrayList<Keybind> _bindings)
	{
        bindings = _bindings;

		ClientTickEvents.END_CLIENT_TICK.register(client ->
		{	
			for(Keybind bind : bindings)
			{
				bind.CheckKey();
			}

			if(configKey.wasPressed())
			{
				client.setScreen(new ConfigScreen(Text.of("Config Screen")));
			}
		});
    }
}