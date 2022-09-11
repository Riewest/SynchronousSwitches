package com.suicidesquid.syncswitch.tiles.Switches;

import com.suicidesquid.syncswitch.setup.Registration;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EStopButtonTile extends BaseChannelTile{
    public EStopButtonTile(BlockPos pos, BlockState state) {
        super(Registration.ESTOP_BUTTON_BLOCK_BE.get(), pos, state);
    }
}
