package com.suicidesquid.syncswitch.tiles;

import com.suicidesquid.syncswitch.setup.ModRegistration;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelBlockTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class ChannelInputTile extends BaseChannelBlockTile{

    public ChannelInputTile(BlockPos pos, BlockState state) {
        super(ModRegistration.CHANNEL_INPUT_BLOCK.blockEntity().get(), pos, state);
    }
    
}
