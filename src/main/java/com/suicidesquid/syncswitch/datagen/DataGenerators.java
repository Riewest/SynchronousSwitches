package com.suicidesquid.syncswitch.datagen;

import com.suicidesquid.syncswitch.SynchronousSwitches;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SynchronousSwitches.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        generator.addProvider(event.includeServer(), new ModRecipeProvider(generator));
        // generator.addProvider(new ModLootTableProvider(generator));
        generator.addProvider(event.includeClient(), new ModBlocksStateProvider(generator, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModLangProvider(generator, SynchronousSwitches.MODID, "en_us"));
        // generator.addProvider(new ModItemModelProvider(generator, existingFileHelper));
    }
}
