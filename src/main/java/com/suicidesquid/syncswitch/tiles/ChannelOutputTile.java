package com.suicidesquid.syncswitch.tiles;

import com.suicidesquid.syncswitch.init.TileEntityInit;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ChannelOutputTile extends BaseChannelTile{
    public ChannelOutputTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.CHANNEL_OUTPUT_BLOCK.get(), pos, state);
    }
}