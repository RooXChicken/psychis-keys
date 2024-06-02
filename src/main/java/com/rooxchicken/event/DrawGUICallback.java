package com.rooxchicken.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.rooxchicken.PsychisKeys;
import com.rooxchicken.client.PsychisKeysClient;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class DrawGUICallback implements HudRenderCallback
{
    private MatrixStack matrixStack;

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta)
    {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;

        if(PsychisKeysClient.playerAbility == -1)
            return;

        int posX = 100;
        int posY = 200;
        double screenScale = 0.08;

        int cooldown1 = PsychisKeysClient.abilityData.cooldown1;
        int cooldown2 = PsychisKeysClient.abilityData.cooldown2;

        int cooldown1Max = PsychisKeysClient.abilityData.cooldown1Max;
        int cooldown2Max = PsychisKeysClient.abilityData.cooldown2Max;

        String txt1 = "";
        String txt2 = "";

        if(cooldown1 > 0)
            txt1 += PsychisKeysClient.abilityData.cooldown1;
        else
            txt1 = "READY";

        if(cooldown2 > 0)
            txt2 += PsychisKeysClient.abilityData.cooldown2;
        else
            txt2 = "READY";

        startScaling(drawContext, screenScale);
        
        drawContext.drawTexture(PsychisKeysClient.abilityData.texture1, posX, posY, 0, 0, 256, 256);
        drawContext.drawTexture(PsychisKeysClient.abilityData.texture2, posX+288, posY, 0, 0, 256, 256);

        int cooldown1Offset = (int)(256 * ((0.0+cooldown1)/cooldown1Max));
        int cooldown2Offset = (int)(256 * ((0.0+cooldown2)/cooldown2Max));

        RenderSystem.enableBlend();
        drawContext.setShaderColor(1, 1, 1, 0.5f);

        drawContext.drawTexture(PsychisKeysClient.abilityData.cooldownTexture, posX, posY+256-cooldown1Offset, 0, 0, 256, cooldown1Offset);
        drawContext.drawTexture(PsychisKeysClient.abilityData.cooldownTexture, posX+288, posY+256-cooldown2Offset, 0, 0, 256, cooldown2Offset);


        drawContext.setShaderColor(1, 1, 1, 1);
        drawContext.drawTexture(PsychisKeysClient.abilityData.outlineTexture, posX, posY, 0, 0, 256, 256);
        drawContext.drawTexture(PsychisKeysClient.abilityData.outlineTexture, posX+288, posY, 0, 0, 256, 256);

		stopScaling(drawContext);

        double textScale = 6;
        startScaling(drawContext, screenScale * textScale);

        drawContext.drawCenteredTextWithShadow(textRenderer, txt1, (int)(posX/textScale) + 23, (int)(posY/textScale) - 10, 0xFFFFFFFF);
        drawContext.drawCenteredTextWithShadow(textRenderer, txt2, (int)(posX/textScale) + 71, (int)(posY/textScale) - 10, 0xFFFFFFFF);

        stopScaling(drawContext);
        
        //drawContext.drawItem(client.player.getInventory().getMainHandStack(), posX + 4, posY + 4);
        //drawContext.drawTexture(new Identifier("minecraft:textures/gui/widgets.png"), posX, posY, 0, 22, 24, 24);
    }
    
    private void startScaling(DrawContext drawContext, double scale)
    {
        matrixStack = drawContext.getMatrices();
		matrixStack.push();
		matrixStack.scale((float)scale, (float)scale, (float)scale);
    }

    private void stopScaling(DrawContext drawContext)
    {
        matrixStack.pop();
    }
}
