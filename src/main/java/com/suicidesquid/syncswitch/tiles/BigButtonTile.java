package com.suicidesquid.syncswitch.tiles;

import com.suicidesquid.syncswitch.init.TileEntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BigButtonTile extends BaseSwitchBlockTile{
    public BigButtonTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.BIG_BUTTON_BLOCK.get(), pos, state);
    }
}
