package com.suicidesquid.syncswitch.tiles;

import com.suicidesquid.syncswitch.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class VanillaSwitchBlockTile extends BaseSwitchBlockTile{
    public VanillaSwitchBlockTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.VANILLA_SWITCH_BLOCK.get(), pos, state);
    }
}
