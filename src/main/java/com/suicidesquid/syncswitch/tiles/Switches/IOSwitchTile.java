package com.suicidesquid.syncswitch.tiles.Switches;

import com.suicidesquid.syncswitch.setup.Registration;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class IOSwitchTile extends BaseChannelTile{
    public IOSwitchTile(BlockPos pos, BlockState state) {
        super(Registration.IO_SWITCH_BLOCK_BE.get(), pos, state);
    }
}
