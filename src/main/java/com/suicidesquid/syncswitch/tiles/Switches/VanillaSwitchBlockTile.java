package com.suicidesquid.syncswitch.tiles.Switches;

import com.suicidesquid.syncswitch.setup.ModRegistration;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class VanillaSwitchBlockTile extends BaseChannelTile{
    public VanillaSwitchBlockTile(BlockPos pos, BlockState state) {
        super(ModRegistration.VANILLA_SWITCH_BLOCK.blockEntity().get(), pos, state);
    }
}
