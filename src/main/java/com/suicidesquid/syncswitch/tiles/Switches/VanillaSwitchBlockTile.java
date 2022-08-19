package com.suicidesquid.syncswitch.tiles.Switches;

import com.suicidesquid.syncswitch.init.TileEntityInit;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class VanillaSwitchBlockTile extends BaseChannelTile{
    public VanillaSwitchBlockTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.VANILLA_SWITCH_BLOCK.get(), pos, state);
    }
}
