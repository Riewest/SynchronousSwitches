package com.suicidesquid.syncswitch.tiles;

import com.suicidesquid.syncswitch.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SwitchBlockTile extends BaseSwitchBlockTile{
    public SwitchBlockTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.SWITCH_BLOCK.get(), pos, state);
    }
}
