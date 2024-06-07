package com.rooxchicken.screen;

import java.util.Scanner;

import com.google.gson.JsonObject;
import com.rooxchicken.PsychisKeys;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;

public class AbilityElement
{
	public String Name;
	public String Description;
	public boolean Enabled;
	public boolean Visible = true;
	public boolean HasLines = true;
	public double SmallestSize = 0.2;
	public double SnapIncrement = 4.0; //4 = 1/4 = 0.25 :3
    public int index = -1;
	
	public String KeyName;
	public KeyBinding UsageKey;
	
	public boolean SettingsOpen;
	public int MouseStatus = -2;
	public int ManipulationStatus = -1;
	
	public int PositionX;
	public int PositionY;

	public int screenX;
	public int screenY;
	public double screenScale;

	public double Scale;
	public double ScaleX;
	public double ScaleY;
	
	protected int x1Mod = 0;
	protected int x2Mod = 64;
	protected int y1Mod = 0;
	protected int y2Mod = 64;
	
	protected int oldMouseX;
	protected int oldMouseY;
	protected int oldPositionX;
	protected int oldPositionY;
	protected double oldScale;
	protected int length;

    public AbilityElement(int _index)
    {
        index = _index;

        PositionX = 100 *  index;
        PositionY = 100;
        Scale = 1;
    }

	public void convertPosToScreen()
	{
		MinecraftClient client = MinecraftClient.getInstance();
		double scalingFactor = client.getWindow().getScaleFactor();

		screenX = PositionX;//(int)(client.getWindow().getWidth()/(scalingFactor*2)) + PositionX;
		screenY = PositionY;//(int)(client.getWindow().getHeight()/(scalingFactor*3)*3 + PositionY);

		screenScale = Scale;

        //PsychisKeys.LOGGER.info(Name + ": " + screenX + " | " + screenY + "   |    " + client.getWindow().getWidth()/scalingFactor);
	}
	
	public void HandleLines(ConfigScreen screen, DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY)
	{
		MinecraftClient client = MinecraftClient.getInstance();
		double scalingFactor = client.getWindow().getScaleFactor();

		int x1, x2, y1, y2;
		
		x1 = (int)(screenX + x1Mod); //RIGHT
        x2 = (int)(screenX + (x2Mod*screenScale)); //LEFT
        
        y1 = (int)((screenY + y1Mod)); //DOWN
        y2 = (int)(screenY + (y2Mod*screenScale)); //UP
		
		if(ManipulationStatus == 2 && MouseStatus > -1)
			context.fill(x1, y1, x1+8, y1+8, 0xFFFF00E4);
		else
			context.fill(x1, y1, x1+8, y1+8, 0xFFFFFFFF);
		
		context.drawHorizontalLine(x1, x2, y1, 0xFFFFFFFF);
		context.drawHorizontalLine(x1, x2, y2, 0xFFFFFFFF);
		
		context.drawVerticalLine(x1, y1, y2, 0xFFFFFFFF);
		context.drawVerticalLine(x2, y1, y2, 0xFFFFFFFF);

        //PsychisKeys.LOGGER.info(x1 + " " + x2 + " |" + y1 + " " + y2);
		
		if(!screen.ObjectSelected && MouseStatus > -1 && ManipulationStatus == -1)
		{
			if(AABBCheck(mouseX, mouseY, x1, x2, y1, y2))
			{
				ManipulationStatus = 1;
				if(AABBCheck(mouseX, mouseY, x1, x1+8, y1, y1+8))
				{
					ManipulationStatus = 2;
					oldScale = Scale;
					length = x2-x1;

					oldMouseX = mouseX;
					oldMouseY = mouseY;

					oldPositionX = PositionX;
					oldPositionY = PositionY;

					//FireClient.LOGGER.info("" + oldScale);
				}

				screen.ObjectSelected = true;
			}
			else
				ManipulationStatus = 0;
		}
		else if(MouseStatus == -1)
		{
			ManipulationStatus = -1;
			screen.ObjectSelected = false;
		}
		
		if(ManipulationStatus == 1)
		{
			PositionX += ((mouseX - oldMouseX));
			PositionY += ((mouseY - oldMouseY));
			
			//FireClient.LOGGER.info("" + ((mouseX - oldMouseX)));
		}
		
		if(ManipulationStatus == 2)
		{
			HandleScaling(mouseX, mouseY);

			if(Scale < SmallestSize)
				Scale = SmallestSize;

			return;
		}
		
		oldMouseX = mouseX;
		oldMouseY = mouseY;
	}

	public void HandleScaling(int mouseX, int mouseY)
	{
		double mScale = Math.max(mouseX - oldMouseX + 0.0, mouseY - oldMouseY + 0.0);

		double val = oldScale * ((length-mScale)/length);

		Scale = val;

		if(Scale < SmallestSize)
		{
			Scale = SmallestSize;
			return;
		}

		if(MouseStatus == 1)
			Scale = ((int)(Scale*SnapIncrement))/SnapIncrement;
	
		PositionX = (int)(oldPositionX + (oldScale-Scale)*x2Mod);
		PositionY = (int)(oldPositionY + (oldScale-Scale)*y2Mod);
	}
	
	protected boolean AABBCheck(int mouseX, int mouseY, int x1, int x2, int y1, int y2)
	{
		return (mouseX > x1 && mouseX < x2 && mouseY < y2 && mouseY > y1);
	}

    public void reset()
    {
        PositionX = 100 * index;
        PositionY = 100;
        Scale = 1;
    }
}
