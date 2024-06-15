package com.rooxchicken.screen;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rooxchicken.PsychisKeys;
import com.rooxchicken.client.PsychisKeysClient;
import com.rooxchicken.data.AbilityData;
import com.rooxchicken.data.AbilityDesc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AbilitySelection extends Screen
{
    private int mouseStatus = -2;
    private Identifier bgTexture;
    private int index = 0;
    private int clickAction = -1;

    private ButtonWidget back;

    public AbilitySelection(Text title)
    {
        super(title);
    }

    @Override
	public void init()
	{	
		bgTexture = Identifier.of("psychis-keys", "textures/gui/bg.png");
	}
	 
	@Override
	public void close()
	{
		super.close();
	}
	 
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
    	mouseStatus = button;

        if(clickAction != -1)
        {
            client.world.playSound(client.player, client.player.getBlockPos(), SoundEvents.UI_BUTTON_CLICK.value(), SoundCategory.MASTER, 0.6f, 1f);
            switch(clickAction)
            {
                case 0:
                    if(--index < 0)
                        index = PsychisKeysClient.abilities.size()-1;
                break;
                case 1:
                    if(++index > PsychisKeysClient.abilities.size()-1)
                        index = 0;
                break;
                case 2:
                    PsychisKeysClient.sendChatCommand("hdn_pickability " + index);
                    close();
                break;
            }
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
    	mouseStatus = -1;
    	
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    @Override
    public void tick()
    {
    	
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE)
        {
            MinecraftClient.getInstance().player.sendMessage(Text.of("You may reopen this menu at any time by running the command /selectability. Until then, you will remain abilityless."));
            close();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta)
    {
        MinecraftClient client = MinecraftClient.getInstance();
		this.renderBackground(context, mouseX, mouseY, delta);
		
        RenderSystem.enableBlend();
        context.drawTexture(bgTexture, width/2 - 128, height/2 - 128, 0, 0, 256, 256);

        AbilityDesc ability = PsychisKeysClient.abilities.get(index);

        int yIndex = 0;

        context.drawCenteredTextWithShadow(textRenderer, Text.of(ability.name), width/2, height/2 - 90, 0xFFFFFFFF);
        context.drawCenteredTextWithShadow(textRenderer, Text.of("Passive: " + ability.passive), width/2 + 6, height/2 - 78, 0xFF999999);
        context.drawTexture(Identifier.of("psychis-keys", "textures/abilities/" + ability.index + ".png"), width/2 - 90, height/2 - 90, 0, 0, 24, 24, 24, 24);
        context.drawTextWithShadow(textRenderer, Text.of(ability.ability1Name), width/2 - 90, height/2 - 52, 0xFFFF4444);
        String[] ability1Desc = ability.ability1Desc.split("\\+");
        for(int i = 0; i < ability1Desc.length; i++)
        {
            context.drawTextWithShadow(textRenderer, Text.of(ability1Desc[i]), width/2 - 90, height/2 - 40 + (12*i), 0xFFCCCCCC);
            yIndex++;
        }

        context.drawTextWithShadow(textRenderer, Text.of(ability.ability2Name), width/2 - 90, height/2 - 40 + (12*(yIndex+1)), 0xFFFF4444);
        String[] ability2Desc = ability.ability2Desc.split("\\+");
        for(int i = 0; i < ability2Desc.length; i++)
        {
            context.drawTextWithShadow(textRenderer, Text.of(ability2Desc[i]), width/2 - 90, height/2 - 40 + (12*(yIndex+2)), 0xFFCCCCCC);
            yIndex++;
        }

        context.drawTexture(Identifier.of("psychis-keys", "textures/gui/left.png"), width/2 - 90, height/2 + 70, 0, 0, 24, 24, 24, 24);
        context.drawTexture(Identifier.of("psychis-keys", "textures/gui/right.png"), width/2 + 60, height/2 + 70, 0, 0, 24, 24, 24, 24);

        context.drawTexture(Identifier.of("psychis-keys", "textures/gui/unlock.png"), width/2 + 30, height/2 + 70, 0, 0, 24, 24, 24, 24);
        context.drawTexture(Identifier.of("psychis-keys", "textures/gui/select.png"), width/2 - 54, height/2 + 70, 0, 0, 72, 24, 72, 24);

        double scalingFactor = client.getWindow().getScaleFactor();
        double screenX = (mouseX * scalingFactor);// * (1920.0/width));
        double screenY = (mouseY * scalingFactor);
        //double screenY = (mouseY + 0.0) * (1080.0/(height/2));

        //PsychisKeys.LOGGER.info("" + ((width/2 - 90) * scalingFactor));

        clickAction = -1;

        if(screenX > (width/2 - 90) * scalingFactor && screenX < ((width/2 - 90) * scalingFactor) + (24*scalingFactor) && screenY > (height/2 + 70) * scalingFactor && screenY < ((height/2 + 70) * scalingFactor) + (24*scalingFactor))
        {
            setTooltip(Text.of("Back"));
            clickAction = 0;
        }

        if(screenX > (width/2 + 60) * scalingFactor && screenX < ((width/2 + 60) * scalingFactor) + (24*scalingFactor) && screenY > (height/2 + 70) * scalingFactor && screenY < ((height/2 + 70) * scalingFactor) + (24*scalingFactor))
        {
            setTooltip(Text.of("Next"));
            clickAction = 1;
        }

        if(screenX > (width/2 + 30) * scalingFactor && screenX < ((width/2 + 30) * scalingFactor) + (24*scalingFactor) && screenY > (height/2 + 70) * scalingFactor && screenY < ((height/2 + 70) * scalingFactor) + (24*scalingFactor))
        {
            setTooltip(Text.of("Second ability unlock: " + ability.secondUnlock));
            clickAction = -1;
        }

        if(screenX > (width/2 - 54) * scalingFactor && screenX < ((width/2 - 54) * scalingFactor) + (72*scalingFactor) && screenY > (height/2 + 70) * scalingFactor && screenY < ((height/2 + 70) * scalingFactor) + (24*scalingFactor))
        {
            setTooltip(Text.of("Select this ability (choose wisely!)"));
            clickAction = 2;
        }
    	
    	super.render(context, mouseX, mouseY, delta);
    }
}
