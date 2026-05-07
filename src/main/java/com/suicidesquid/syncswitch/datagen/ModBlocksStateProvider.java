package com.suicidesquid.syncswitch.datagen;

import com.suicidesquid.syncswitch.SynchronousSwitches;
import com.suicidesquid.syncswitch.blocks.base.BaseDirectionalLightBlock;
import com.suicidesquid.syncswitch.blocks.base.BaseLightBlock;
import com.suicidesquid.syncswitch.setup.ModRegistration;

import com.mojang.math.Quadrant;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ModBlocksStateProvider extends ModelProvider {

    public ModBlocksStateProvider(PackOutput packOutput) {
        super(packOutput, SynchronousSwitches.MODID);
    }

    private static Identifier modBlock(String name) {
        return Identifier.fromNamespaceAndPath(SynchronousSwitches.MODID, "block/" + name);
    }

    private static Identifier mcBlock(String name) {
        return Identifier.withDefaultNamespace("block/" + name);
    }

    private static Quadrant degToQuadrant(int deg) {
        int normalized = ((deg % 360) + 360) % 360;
        return switch (normalized) {
            case 0   -> Quadrant.R0;
            case 90  -> Quadrant.R90;
            case 180 -> Quadrant.R180;
            case 270 -> Quadrant.R270;
            default  -> throw new IllegalArgumentException("Invalid rotation: " + deg);
        };
    }

    private static VariantMutator rotationMutator(int xDeg, int yDeg) {
        VariantMutator mutator = BlockModelGenerators.NOP;
        if (xDeg != 0) mutator = mutator.then(VariantMutator.X_ROT.withValue(degToQuadrant(xDeg)));
        if (yDeg != 0) mutator = mutator.then(VariantMutator.Y_ROT.withValue(degToQuadrant(yDeg)));
        return mutator;
    }

    private static void leverBlock(BlockModelGenerators blockModels, Block block,
            Identifier offModel, Identifier onModel) {
        blockModels.blockStateOutput.accept(
            MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(BlockStateProperties.POWERED)
                    .select(false, BlockModelGenerators.plainVariant(offModel))
                    .select(true,  BlockModelGenerators.plainVariant(onModel)))
                .with(PropertyDispatch.modify(BlockStateProperties.ATTACH_FACE, BlockStateProperties.HORIZONTAL_FACING)
                    .generate((face, facing) -> {
                        int xDeg = face == AttachFace.FLOOR ? 0 : (face == AttachFace.WALL ? 90 : 180);
                        int yDeg = (int) (face == AttachFace.CEILING ? facing : facing.getOpposite()).toYRot();
                        return rotationMutator(xDeg, yDeg);
                    }))
                    
        );
    }

    private static void lightBlock(BlockModelGenerators blockModels, BaseDirectionalLightBlock block,
            Identifier offModel, Identifier onModel) {
        blockModels.blockStateOutput.accept(
            MultiVariantGenerator.dispatch(block)
                .with(PropertyDispatch.initial(BaseLightBlock.LIT)
                    .select(false, BlockModelGenerators.plainVariant(offModel))
                    .select(true,  BlockModelGenerators.plainVariant(onModel)))
                .with(PropertyDispatch.modify(BlockStateProperties.ATTACH_FACE, BlockStateProperties.HORIZONTAL_FACING)
                    .generate((face, facing) -> {
                        int xDeg = face == AttachFace.FLOOR ? 0 : (face == AttachFace.WALL ? 90 : 180);
                        int yDeg = (int) (face == AttachFace.CEILING ? facing : facing.getOpposite()).toYRot();
                        return rotationMutator(xDeg, yDeg);
                    }))
        );
    }

    @Override
    protected java.util.stream.Stream<? extends Holder<Block>> getKnownBlocks() {
        return java.util.stream.Stream.of(
            ModRegistration.SWITCH_BLOCK.get(),
            ModRegistration.BIG_BUTTON_BLOCK.get(),
            ModRegistration.ESTOP_BUTTON_BLOCK.get(),
            ModRegistration.IO_SWITCH_BLOCK.get(),
            ModRegistration.VANILLA_SWITCH_BLOCK.get(),
            ModRegistration.STONE_BUTTON_BLOCK.get(),
            ModRegistration.LIGHT_PANEL_BLOCK.get()
        ).map(BuiltInRegistries.BLOCK::wrapAsHolder);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        leverBlock(blockModels, ModRegistration.SWITCH_BLOCK.get(),       modBlock("switch_block"),   modBlock("switch_block_on"));
        leverBlock(blockModels, ModRegistration.BIG_BUTTON_BLOCK.get(),   modBlock("big_button"),     modBlock("big_button_on"));
        leverBlock(blockModels, ModRegistration.ESTOP_BUTTON_BLOCK.get(), modBlock("estop_button"),   modBlock("estop_button_on"));
        leverBlock(blockModels, ModRegistration.IO_SWITCH_BLOCK.get(),    modBlock("io_switch"),      modBlock("io_switch_on"));
        leverBlock(blockModels, ModRegistration.VANILLA_SWITCH_BLOCK.get(), mcBlock("lever"),         mcBlock("lever_on"));
        leverBlock(blockModels, ModRegistration.STONE_BUTTON_BLOCK.get(), mcBlock("stone_button"),    mcBlock("stone_button_pressed"));
        lightBlock(blockModels, (BaseDirectionalLightBlock) ModRegistration.LIGHT_PANEL_BLOCK.get(),
                modBlock("light_panel"), modBlock("light_panel_on"));

        blockModels.registerSimpleItemModel(ModRegistration.VANILLA_SWITCH_BLOCK.get(), Identifier.withDefaultNamespace("item/lever"));
        blockModels.registerSimpleItemModel(ModRegistration.STONE_BUTTON_BLOCK.get(), Identifier.withDefaultNamespace("block/stone_button_inventory"));
    }
}
