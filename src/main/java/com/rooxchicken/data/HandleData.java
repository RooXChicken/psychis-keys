package com.rooxchicken.data;

import com.rooxchicken.PsychisKeys;
import com.rooxchicken.client.PsychisKeysClient;
import com.rooxchicken.screen.AbilitySelection;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class HandleData
{
    public static void parseData(String msg)
    {
        String[] data = msg.split("_");
        int mode = Integer.parseInt(data[1]);

        //PsychisKeys.LOGGER.info("Received server data! " + msg);

        switch(mode)
        {
            case 0: //set ability
                PsychisKeysClient.playerAbility = Integer.parseInt(data[2]);
                PsychisKeysClient.abilityData = new AbilityData("" + PsychisKeysClient.playerAbility);
                PsychisKeysClient.abilityData.secondLocked = !Boolean.parseBoolean(data[3]);
                PsychisKeysClient.sendChatCommand("hdn_verifymod");
            break;
            case 1: //set cooldown
                int ability = Integer.parseInt(data[2]);
                int cooldown = Integer.parseInt(data[3]);
                int cooldownMax = Integer.parseInt(data[4]);

                if(ability == 0)
                {
                    PsychisKeysClient.abilityData.cooldown1 = cooldown;
                    PsychisKeysClient.abilityData.cooldown1Max = cooldownMax;
                }
                if(ability == 1)
                {
                    PsychisKeysClient.abilityData.cooldown2 = cooldown;
                    PsychisKeysClient.abilityData.cooldown2Max = cooldownMax;
                }
            break;
            case 2:
                AbilityDesc desc = new AbilityDesc();
                desc.name = data[2];
                if(desc.name.equals("."))
                {
                    PsychisKeysClient.abilities.clear();
                    return;
                }
                desc.index = Integer.parseInt(data[3]);
                desc.passive = data[4];
                desc.ability1Name = data[5];
                desc.ability1Desc = data[6];
                desc.ability2Name = data[7];
                desc.ability2Desc = data[8];
                desc.secondUnlock = data[9];
                PsychisKeysClient.abilities.add(desc);
            break;
            case 3:
                PsychisKeysClient.playerAbility = -2;
                PsychisKeysClient.abilityData = new AbilityData("empty");
                MinecraftClient client = MinecraftClient.getInstance();
                client.setScreen(new AbilitySelection(Text.of("Ability Selection")));
            break;
        }
    }
}
