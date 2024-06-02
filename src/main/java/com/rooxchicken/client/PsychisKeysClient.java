package com.rooxchicken.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import com.rooxchicken.data.AbilityData;
import com.rooxchicken.data.AbilityDesc;
import com.rooxchicken.event.DrawGUICallback;
import com.rooxchicken.keybinding.KeyInputHandler;
import com.rooxchicken.keybinding.Keybind;

public class PsychisKeysClient implements ClientModInitializer
{
    public ArrayList<Keybind> keybinds;
	private String category = "key.category.ckb";

	public static int playerAbility = -1;
	public static AbilityData abilityData = new AbilityData("empty");
	public static ArrayList<AbilityDesc> abilities;

	@Override
	public void onInitializeClient()
	{
		keybinds = new ArrayList<Keybind>();
		abilities = new ArrayList<AbilityDesc>();
		
		keybinds.add(new Keybind(category, "key.ckb.ability1", GLFW.GLFW_KEY_Z, "hdn_ability1"));
		keybinds.add(new Keybind(category, "key.ckb.ability2", GLFW.GLFW_KEY_X, "hdn_ability2"));
		
		KeyInputHandler.registerKeyInputs(keybinds);
		HudRenderCallback.EVENT.register(new DrawGUICallback());
		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) ->
		{
			PsychisKeysClient.abilityData = new AbilityData("empty");
			PsychisKeysClient.playerAbility = -1;
		});
	}

	public static void sendChatCommand(String msg)
	{
		if(PsychisKeysClient.playerAbility == -1)
            return;
			
		MinecraftClient client = MinecraftClient.getInstance();
    	ClientPlayNetworkHandler handler = client.getNetworkHandler();
		if(handler == null)
			return;
    	handler.sendChatCommand(msg);
	}
}