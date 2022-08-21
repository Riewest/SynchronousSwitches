package com.suicidesquid.syncswitch.init;

import com.suicidesquid.syncswitch.SynchronousSwitches;
import com.suicidesquid.syncswitch.tiles.ChannelInputTile;
import com.suicidesquid.syncswitch.tiles.ChannelOutputTile;
import com.suicidesquid.syncswitch.tiles.Switches.BigButtonTile;
import com.suicidesquid.syncswitch.tiles.Switches.EStopButtonTile;
import com.suicidesquid.syncswitch.tiles.Switches.IOSwitchTile;
import com.suicidesquid.syncswitch.tiles.Switches.SwitchBlockTile;
import com.suicidesquid.syncswitch.tiles.Switches.VanillaSwitchBlockTile;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TileEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SynchronousSwitches.MODID);

    public static final RegistryObject<BlockEntityType<ChannelOutputTile>> CHANNEL_OUTPUT_BLOCK
            = TILE_ENTITY_TYPES.register("channel_output",
            () -> BlockEntityType.Builder.of(ChannelOutputTile::new, BlockInit.CHANNEL_OUTPUT_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<ChannelInputTile>> CHANNEL_INPUT_BLOCK
            = TILE_ENTITY_TYPES.register("channel_input",
            () -> BlockEntityType.Builder.of(ChannelInputTile::new, BlockInit.CHANNEL_INPUT_BLOCK.get()).build(null));
            
    public static final RegistryObject<BlockEntityType<SwitchBlockTile>> SWITCH_BLOCK
            = TILE_ENTITY_TYPES.register("switch_block",
            () -> BlockEntityType.Builder.of(SwitchBlockTile::new, BlockInit.SWITCH_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<BigButtonTile>> BIG_BUTTON_BLOCK
            = TILE_ENTITY_TYPES.register("big_button",
            () -> BlockEntityType.Builder.of(BigButtonTile::new, BlockInit.BIG_BUTTON_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<EStopButtonTile>> ESTOP_BUTTON_BLOCK
            = TILE_ENTITY_TYPES.register("estop_button",
            () -> BlockEntityType.Builder.of(EStopButtonTile::new, BlockInit.ESTOP_BUTTON_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<IOSwitchTile>> IO_SWITCH_BLOCK
            = TILE_ENTITY_TYPES.register("io_switch",
            () -> BlockEntityType.Builder.of(IOSwitchTile::new, BlockInit.IO_SWITCH_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<VanillaSwitchBlockTile>> VANILLA_SWITCH_BLOCK
            = TILE_ENTITY_TYPES.register("vanilla_switch_block",
            () -> BlockEntityType.Builder.of(VanillaSwitchBlockTile::new, BlockInit.VANILLA_SWITCH_BLOCK.get()).build(null));

}