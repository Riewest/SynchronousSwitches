package com.suicidesquid.syncswitch.tiles.Base;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BaseChannelBlockTile extends BaseChannelTile{

    public BaseChannelBlockTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void playSound(BlockState state, Level level, BlockPos pos) {
        
    }
}
