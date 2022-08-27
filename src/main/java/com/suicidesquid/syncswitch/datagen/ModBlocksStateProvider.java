package com.suicidesquid.syncswitch.datagen;

import com.suicidesquid.syncswitch.SynchronousSwitches;
import com.suicidesquid.syncswitch.blocks.base.BaseDirectionalLightBlock;
import com.suicidesquid.syncswitch.init.BlockInit;

import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelFile.ExistingModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlocksStateProvider extends BlockStateProvider {

    public ModBlocksStateProvider(DataGenerator gen, ExistingFileHelper existingFileHelper){
        super(gen, SynchronousSwitches.MODID, existingFileHelper);
    }

    public void leverBlock(LeverBlock block, ModelFile button, ModelFile buttonPressed) {
        getVariantBuilder(block).forAllStates(state -> {
            Direction facing = state.getValue(ButtonBlock.FACING);
            AttachFace face = state.getValue(ButtonBlock.FACE);
            boolean powered = state.getValue(ButtonBlock.POWERED);

            return ConfiguredModel.builder()
                    .modelFile(powered ? buttonPressed : button)
                    .rotationX(face == AttachFace.FLOOR ? 0 : (face == AttachFace.WALL ? 90 : 180))
                    .rotationY((int) (face == AttachFace.CEILING ? facing : facing.getOpposite()).toYRot())
                    .build();
        });
    }
    public void lightBlock(BaseDirectionalLightBlock block, ModelFile light, ModelFile light_on) {
        getVariantBuilder(block).forAllStates(state -> {
            Direction facing = state.getValue(ButtonBlock.FACING);
            AttachFace face = state.getValue(ButtonBlock.FACE);
            boolean lit = state.getValue(BaseDirectionalLightBlock.LIT);

            return ConfiguredModel.builder()
                    .modelFile(lit ? light_on : light)
                    .rotationX(face == AttachFace.FLOOR ? 0 : (face == AttachFace.WALL ? 90 : 180))
                    .rotationY((int) (face == AttachFace.CEILING ? facing : facing.getOpposite()).toYRot())
                    .build();
        });
    }

    @Override
    protected void registerStatesAndModels() {

        leverBlock((LeverBlock)BlockInit.SWITCH_BLOCK.get() , new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/switch_block"), models().existingFileHelper ), new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/switch_block_on"), models().existingFileHelper ));
        leverBlock((LeverBlock)BlockInit.BIG_BUTTON_BLOCK.get() , new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/big_button"), models().existingFileHelper ), new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/big_button_on"), models().existingFileHelper ));
        leverBlock((LeverBlock)BlockInit.ESTOP_BUTTON_BLOCK.get() , new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/estop_button"), models().existingFileHelper ), new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/estop_button_on"), models().existingFileHelper ));
        leverBlock((LeverBlock)BlockInit.IO_SWITCH_BLOCK.get() , new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/io_switch"), models().existingFileHelper ), new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/io_switch_on"), models().existingFileHelper ));
        leverBlock((LeverBlock)BlockInit.VANILLA_SWITCH_BLOCK.get() , new ExistingModelFile(new ResourceLocation("minecraft:block/lever"), models().existingFileHelper ), new ExistingModelFile(new ResourceLocation("minecraft:block/lever_on"), models().existingFileHelper ));
        
        
        lightBlock((BaseDirectionalLightBlock)BlockInit.LIGHT_PANEL_BLOCK.get() , new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/light_panel"), models().existingFileHelper ), new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/light_panel_on"), models().existingFileHelper ));
        
    }
    
}
