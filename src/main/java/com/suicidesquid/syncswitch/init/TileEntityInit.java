package com.suicidesquid.syncswitch.init;

import com.suicidesquid.syncswitch.SynchronousSwitches;
import com.suicidesquid.syncswitch.tiles.SwitchBlockTile;
import com.suicidesquid.syncswitch.tiles.VanillaSwitchBlockTile;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TileEntityInit {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SynchronousSwitches.MODID);

    public static final RegistryObject<BlockEntityType<SwitchBlockTile>> SWITCH_BLOCK
            = TILE_ENTITY_TYPES.register("switch_block",
            () -> BlockEntityType.Builder.of(SwitchBlockTile::new, BlockInit.SWITCH_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<VanillaSwitchBlockTile>> VANILLA_SWITCH_BLOCK
            = TILE_ENTITY_TYPES.register("vanilla_switch_block",
            () -> BlockEntityType.Builder.of(VanillaSwitchBlockTile::new, BlockInit.VANILLA_SWITCH_BLOCK.get()).build(null));

}