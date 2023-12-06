package com.suicidesquid.syncswitch.datagen;

import com.suicidesquid.syncswitch.SynchronousSwitches;
import com.suicidesquid.syncswitch.setup.LangInit;
import com.suicidesquid.syncswitch.setup.Registration;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLangProvider extends LanguageProvider {

    public ModLangProvider(PackOutput packOutput, String locale) {
        super(packOutput, SynchronousSwitches.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + SynchronousSwitches.MODID, "Synchronous Switches");
        add(Registration.SWITCH_BLOCK.get(), "Synchronous Switch");
        add(Registration.BIG_BUTTON_BLOCK.get(), "Synchronous Big Button");
        add(Registration.ESTOP_BUTTON_BLOCK.get(), "Synchronous E-Stop Button");
        add(Registration.IO_SWITCH_BLOCK.get(), "Synchronous IO Switch");
        add(Registration.VANILLA_SWITCH_BLOCK.get(), "Synchronous Vanilla Switch");
        add(Registration.CHANNEL_OUTPUT_BLOCK.get(), "Synchronous Channel Output");
        add(Registration.CHANNEL_INPUT_BLOCK.get(), "Synchronous Channel Input");
        add(Registration.LIGHT_BLOCK.get(), "Synchronous Light Block");
        add(Registration.LIGHT_PANEL_BLOCK.get(), "Synchronous Light Panel");
        add(Registration.STONE_BUTTON_BLOCK.get(), "Synchronous Stone Button");

        add(LangInit.SET_CHANNEL, "Setting Channel: ");
        add(LangInit.REMOVE_CHANNEL, "Channel Cleared!");
        add(LangInit.CHANNEL, "Channel: ");
        add(LangInit.NO_CHANNEL, "No Channel");
        add(LangInit.REDACTED, "REDACTED");
        add(LangInit.SET_REDACTED, "Redacted Channel");
        add(LangInit.UNREDACTED, "Unredacted Channel");
        add(LangInit.SILENCING, "Silencing");
        add(LangInit.UNSILENCING, "Unsilencing");
        add(LangInit.ACTIVE, "Active: ");
        add(LangInit.ON, "On");
        add(LangInit.OFF, "Off");
        add(LangInit.COPIED, "Copied: ");
    }
    
}
