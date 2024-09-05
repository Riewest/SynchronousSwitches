package com.suicidesquid.syncswitch.datagen;

import com.suicidesquid.syncswitch.SynchronousSwitches;
import com.suicidesquid.syncswitch.blocks.base.BaseDirectionalLightBlock;
import com.suicidesquid.syncswitch.setup.Registration;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelFile.ExistingModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlocksStateProvider extends BlockStateProvider {

    public ModBlocksStateProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper){
        super(packOutput, SynchronousSwitches.MODID, existingFileHelper);
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

        leverBlock((LeverBlock)Registration.SWITCH_BLOCK.get() , new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/switch_block"), models().existingFileHelper ), new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/switch_block_on"), models().existingFileHelper ));
        leverBlock((LeverBlock)Registration.BIG_BUTTON_BLOCK.get() , new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/big_button"), models().existingFileHelper ), new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/big_button_on"), models().existingFileHelper ));
        leverBlock((LeverBlock)Registration.ESTOP_BUTTON_BLOCK.get() , new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/estop_button"), models().existingFileHelper ), new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/estop_button_on"), models().existingFileHelper ));
        leverBlock((LeverBlock)Registration.IO_SWITCH_BLOCK.get() , new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/io_switch"), models().existingFileHelper ), new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/io_switch_on"), models().existingFileHelper ));
        leverBlock((LeverBlock)Registration.VANILLA_SWITCH_BLOCK.get() , new ExistingModelFile(new ResourceLocation("minecraft:block/lever"), models().existingFileHelper ), new ExistingModelFile(new ResourceLocation("minecraft:block/lever_on"), models().existingFileHelper ));
        buttonBlock((ButtonBlock)Registration.STONE_BUTTON_BLOCK.get() , new ExistingModelFile(new ResourceLocation("minecraft:block/stone_button"), models().existingFileHelper ), new ExistingModelFile(new ResourceLocation("minecraft:block/stone_button_pressed"), models().existingFileHelper ));
        
        
        lightBlock((BaseDirectionalLightBlock)Registration.LIGHT_PANEL_BLOCK.get() , new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/light_panel"), models().existingFileHelper ), new ExistingModelFile(new ResourceLocation(SynchronousSwitches.MODID + ":block/light_panel_on"), models().existingFileHelper ));
        
    }
    
}
