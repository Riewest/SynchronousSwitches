package com.suicidesquid.syncswitch.datagen;

import com.suicidesquid.syncswitch.SynchronousSwitches;
import com.suicidesquid.syncswitch.init.BlockInit;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLangProvider extends LanguageProvider {

    public ModLangProvider(DataGenerator gen, String modid, String locale) {
        super(gen, modid, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + SynchronousSwitches.MODID, "Synchronous Switches");
        add(BlockInit.SWITCH_BLOCK.get(), "Synchronous Switch");
        add(BlockInit.BIG_BUTTON_BLOCK.get(), "Synchronous Big Button");
        add(BlockInit.ESTOP_BUTTON_BLOCK.get(), "Synchronous E-Stop Button");
        add(BlockInit.IO_SWITCH_BLOCK.get(), "Synchronous IO Switch");
        add(BlockInit.VANILLA_SWITCH_BLOCK.get(), "Synchronous Vanilla Switch");
        add(BlockInit.CHANNEL_OUTPUT_BLOCK.get(), "Synchronous Channel Output");
        add(BlockInit.CHANNEL_INPUT_BLOCK.get(), "Synchronous Channel Input");
        add(BlockInit.LIGHT_BLOCK.get(), "Synchronous Light Block");
        add(BlockInit.LIGHT_PANEL_BLOCK.get(), "Synchronous Light Panel");
    }
    
}
