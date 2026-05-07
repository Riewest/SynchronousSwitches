package com.suicidesquid.syncswitch.datagen;

import com.suicidesquid.syncswitch.SynchronousSwitches;
import com.suicidesquid.syncswitch.setup.LangInit;
import com.suicidesquid.syncswitch.setup.ModRegistration;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLangProvider extends LanguageProvider {

    public ModLangProvider(PackOutput packOutput, String locale) {
        super(packOutput, SynchronousSwitches.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + SynchronousSwitches.MODID, "Synchronous Switches");
        add(ModRegistration.SWITCH_BLOCK.get(), "Synchronous Switch");
        add(ModRegistration.BIG_BUTTON_BLOCK.get(), "Synchronous Big Button");
        add(ModRegistration.ESTOP_BUTTON_BLOCK.get(), "Synchronous E-Stop Button");
        add(ModRegistration.IO_SWITCH_BLOCK.get(), "Synchronous IO Switch");
        add(ModRegistration.VANILLA_SWITCH_BLOCK.get(), "Synchronous Vanilla Switch");
        add(ModRegistration.CHANNEL_OUTPUT_BLOCK.get(), "Synchronous Channel Output");
        add(ModRegistration.CHANNEL_INPUT_BLOCK.get(), "Synchronous Channel Input");
        add(ModRegistration.LIGHT_BLOCK.get(), "Synchronous Light Block");
        add(ModRegistration.LIGHT_PANEL_BLOCK.get(), "Synchronous Light Panel");
        add(ModRegistration.STONE_BUTTON_BLOCK.get(), "Synchronous Stone Button");

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
