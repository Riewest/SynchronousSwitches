package com.suicidesquid.syncswitch.datagen;

import com.suicidesquid.syncswitch.SynchronousSwitches;
import com.suicidesquid.syncswitch.blocks.base.BaseDirectionalLightBlock;
import com.suicidesquid.syncswitch.blocks.base.BaseLightBlock;
import com.suicidesquid.syncswitch.setup.Registration;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.blockstates.Variant;
import net.minecraft.client.data.models.blockstates.VariantProperties;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class ModBlocksStateProvider extends ModelProvider {

    public ModBlocksStateProvider(PackOutput packOutput) {
        super(packOutput, SynchronousSwitches.MODID);
    }

    private static ResourceLocation modBlock(String name) {
        return ResourceLocation.fromNamespaceAndPath(SynchronousSwitches.MODID, "block/" + name);
    }

    private static ResourceLocation mcBlock(String name) {
        return ResourceLocation.withDefaultNamespace("block/" + name);
    }

    private static VariantProperties.Rotation degToRotation(int deg) {
        int normalized = ((deg % 360) + 360) % 360;
        return switch (normalized) {
            case 0   -> VariantProperties.Rotation.R0;
            case 90  -> VariantProperties.Rotation.R90;
            case 180 -> VariantProperties.Rotation.R180;
            case 270 -> VariantProperties.Rotation.R270;
            default  -> throw new IllegalArgumentException("Invalid rotation: " + deg);
        };
    }

    private static void leverBlock(BlockModelGenerators blockModels, Block block,
            ResourceLocation offModel, ResourceLocation onModel) {
        blockModels.blockStateOutput.accept(
            MultiVariantGenerator.multiVariant(block)
                .with(PropertyDispatch.property(BlockStateProperties.POWERED)
                    .select(false, Variant.variant().with(VariantProperties.MODEL, offModel))
                    .select(true,  Variant.variant().with(VariantProperties.MODEL, onModel)))
                .with(PropertyDispatch.properties(BlockStateProperties.ATTACH_FACE, BlockStateProperties.HORIZONTAL_FACING)
                    .generate((face, facing) -> {
                        int xDeg = face == AttachFace.FLOOR ? 0 : (face == AttachFace.WALL ? 90 : 180);
                        int yDeg = (int) (face == AttachFace.CEILING ? facing : facing.getOpposite()).toYRot();
                        Variant v = Variant.variant();
                        if (xDeg != 0) v = v.with(VariantProperties.X_ROT, degToRotation(xDeg));
                        if (yDeg != 0) v = v.with(VariantProperties.Y_ROT, degToRotation(yDeg));
                        return v;
                    }))
        );
    }

    private static void lightBlock(BlockModelGenerators blockModels, BaseDirectionalLightBlock block,
            ResourceLocation offModel, ResourceLocation onModel) {
        blockModels.blockStateOutput.accept(
            MultiVariantGenerator.multiVariant(block)
                .with(PropertyDispatch.property(BaseLightBlock.LIT)
                    .select(false, Variant.variant().with(VariantProperties.MODEL, offModel))
                    .select(true,  Variant.variant().with(VariantProperties.MODEL, onModel)))
                .with(PropertyDispatch.properties(BlockStateProperties.ATTACH_FACE, BlockStateProperties.HORIZONTAL_FACING)
                    .generate((face, facing) -> {
                        int xDeg = face == AttachFace.FLOOR ? 0 : (face == AttachFace.WALL ? 90 : 180);
                        int yDeg = (int) (face == AttachFace.CEILING ? facing : facing.getOpposite()).toYRot();
                        Variant v = Variant.variant();
                        if (xDeg != 0) v = v.with(VariantProperties.X_ROT, degToRotation(xDeg));
                        if (yDeg != 0) v = v.with(VariantProperties.Y_ROT, degToRotation(yDeg));
                        return v;
                    }))
        );
    }

    @Override
    protected java.util.stream.Stream<? extends Holder<Block>> getKnownBlocks() {
        return java.util.stream.Stream.of(
            Registration.SWITCH_BLOCK.get(),
            Registration.BIG_BUTTON_BLOCK.get(),
            Registration.ESTOP_BUTTON_BLOCK.get(),
            Registration.IO_SWITCH_BLOCK.get(),
            Registration.VANILLA_SWITCH_BLOCK.get(),
            Registration.STONE_BUTTON_BLOCK.get(),
            Registration.LIGHT_PANEL_BLOCK.get()
        ).map(BuiltInRegistries.BLOCK::wrapAsHolder);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        leverBlock(blockModels, Registration.SWITCH_BLOCK.get(),       modBlock("switch_block"),   modBlock("switch_block_on"));
        leverBlock(blockModels, Registration.BIG_BUTTON_BLOCK.get(),   modBlock("big_button"),     modBlock("big_button_on"));
        leverBlock(blockModels, Registration.ESTOP_BUTTON_BLOCK.get(), modBlock("estop_button"),   modBlock("estop_button_on"));
        leverBlock(blockModels, Registration.IO_SWITCH_BLOCK.get(),    modBlock("io_switch"),      modBlock("io_switch_on"));
        leverBlock(blockModels, Registration.VANILLA_SWITCH_BLOCK.get(), mcBlock("lever"),         mcBlock("lever_on"));
        leverBlock(blockModels, Registration.STONE_BUTTON_BLOCK.get(), mcBlock("stone_button"),    mcBlock("stone_button_pressed"));
        lightBlock(blockModels, (BaseDirectionalLightBlock) Registration.LIGHT_PANEL_BLOCK.get(),
                modBlock("light_panel"), modBlock("light_panel_on"));
    }
}
