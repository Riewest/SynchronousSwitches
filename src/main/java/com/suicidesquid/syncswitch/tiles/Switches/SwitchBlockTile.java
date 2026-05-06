package com.suicidesquid.syncswitch.tiles.Switches;

import com.suicidesquid.syncswitch.setup.ModRegistration;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SwitchBlockTile extends BaseChannelTile{
    public SwitchBlockTile(BlockPos pos, BlockState state) {
        super(ModRegistration.SWITCH_BLOCK_BE.get(), pos, state);
    }
}
