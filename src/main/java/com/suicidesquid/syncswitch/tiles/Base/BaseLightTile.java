package com.suicidesquid.syncswitch.tiles.Base;

import java.util.Random;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;



public class BaseLightTile extends BaseChannelBlockTile {

    private static final int MAX_LIGHT_UPDATE = 20;

    public BaseLightTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        int maxtime = (int) Math.abs(new Random().nextGaussian()*10+5);
        timerMax = maxtime > MAX_LIGHT_UPDATE ? MAX_LIGHT_UPDATE : maxtime;
    }
    
}
