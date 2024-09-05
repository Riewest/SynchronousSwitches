package com.suicidesquid.syncswitch.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import org.jetbrains.annotations.Nullable;

import com.suicidesquid.syncswitch.SynchronousSwitches;
import com.suicidesquid.syncswitch.setup.Registration;

import java.util.concurrent.CompletableFuture;

public class ModBlockTags extends BlockTagsProvider {

    public ModBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, SynchronousSwitches.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (DeferredHolder<Block, ? extends Block> regBlock : Registration.BLOCKS.getEntries()) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(regBlock.get());
            tag(BlockTags.MINEABLE_WITH_AXE)
                .add(regBlock.get());
        }
     }
}
