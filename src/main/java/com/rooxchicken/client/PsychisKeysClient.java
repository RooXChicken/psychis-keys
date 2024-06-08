package com.rooxchicken.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.lwjgl.glfw.GLFW;

import com.rooxchicken.PsychisKeys;
import com.rooxchicken.data.AbilityData;
import com.rooxchicken.data.AbilityDesc;
import com.rooxchicken.event.DrawGUICallback;
import com.rooxchicken.keybinding.KeyInputHandler;
import com.rooxchicken.keybinding.Keybind;
import com.rooxchicken.screen.AbilityElement;

public class PsychisKeysClient implements ClientModInitializer
{
    public ArrayList<Keybind> keybinds;
	private String category = "key.category.ckb";

	public static int playerAbility = -1;
	public static AbilityData abilityData = new AbilityData("empty");
	public static ArrayList<AbilityDesc> abilities;

	public static AbilityElement abilityElement1;
	public static AbilityElement abilityElement2;

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

		abilityElement1 = new AbilityElement(0);
		abilityElement2 = new AbilityElement(1);

		load();
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

	public static void load()
	{
		File file = new File("psychis-keys.cfg");
		if(!file.exists())
		{
			save();
			return;
		}
		try
		{
			Scanner scan = new Scanner(file);
			abilityElement1.load(scan);
			abilityElement2.load(scan);
			scan.close();
		}
		catch (FileNotFoundException e)
		{
			PsychisKeys.LOGGER.error("Failed to open config file.", e);
		}
	}

	public static void save()
	{
		File file = new File("psychis-keys.cfg");
		try
		{
			FileWriter write = new FileWriter(file);
			write.write(abilityElement1.save() + abilityElement2.save());

			write.close();

		}
		catch (IOException e)
		{
			PsychisKeys.LOGGER.error("Failed to save config file.", e);
		}
	}
}