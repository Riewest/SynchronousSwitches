package com.suicidesquid.syncswitch.tiles;

import com.suicidesquid.syncswitch.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class EStopButtonTile extends BaseSwitchBlockTile{
    public EStopButtonTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.ESTOP_BUTTON_BLOCK.get(), pos, state);
    }
}
