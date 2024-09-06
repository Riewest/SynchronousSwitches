package com.suicidesquid.syncswitch.datagen;

import java.util.concurrent.CompletableFuture;

import com.suicidesquid.syncswitch.SynchronousSwitches;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = SynchronousSwitches.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        

        ModBlockTags blockTags = new ModBlockTags(packOutput, lookupProvider, event.getExistingFileHelper());
        generator.addProvider(event.includeServer(), blockTags);
        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput, event.getLookupProvider()));
        // generator.addProvider(new ModLootTableProvider(generator));
        generator.addProvider(event.includeClient(), new ModBlocksStateProvider(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModLangProvider(packOutput, "en_us"));
        // generator.addProvider(new ModItemModelProvider(generator, existingFileHelper));
    }
}
