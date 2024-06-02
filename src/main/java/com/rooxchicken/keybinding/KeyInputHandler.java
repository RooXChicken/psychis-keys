package com.rooxchicken.keybinding;

import java.util.ArrayList;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class KeyInputHandler
{
    private static ArrayList<Keybind> bindings;
	
	public static void registerKeyInputs(ArrayList<Keybind> _bindings)
	{
        bindings = _bindings;

		ClientTickEvents.END_CLIENT_TICK.register(client ->
		{	
			for(Keybind bind : bindings)
			{
				bind.CheckKey();
			}
		});
    }
}