package com.suicidesquid.syncswitch.tiles.Switches;

import com.suicidesquid.syncswitch.init.TileEntityInit;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BigButtonTile extends BaseChannelTile{
    public BigButtonTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.BIG_BUTTON_BLOCK.get(), pos, state);
    }
}