package com.suicidesquid.syncswitch.tiles.Switches;

import com.suicidesquid.syncswitch.init.TileEntityInit;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class IOSwitchTile extends BaseChannelTile{
    public IOSwitchTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.IO_SWITCH_BLOCK.get(), pos, state);
    }
}
