package com.suicidesquid.syncswitch.tiles.Switches;

import com.suicidesquid.syncswitch.init.TileEntityInit;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SwitchBlockTile extends BaseChannelTile{
    public SwitchBlockTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.SWITCH_BLOCK.get(), pos, state);
    }
}
