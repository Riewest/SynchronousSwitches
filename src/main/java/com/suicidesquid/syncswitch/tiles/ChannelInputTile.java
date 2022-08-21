package com.suicidesquid.syncswitch.tiles;

import com.suicidesquid.syncswitch.init.TileEntityInit;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelBlockTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ChannelInputTile extends BaseChannelBlockTile{

    public ChannelInputTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.CHANNEL_INPUT_BLOCK.get(), pos, state);
    }
    
}
