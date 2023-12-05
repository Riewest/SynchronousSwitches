package com.suicidesquid.syncswitch.tiles.Base;

import com.suicidesquid.syncswitch.data.SwitchData;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BaseButtonTile extends BaseChannelTile{
    public BaseButtonTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
    private Boolean previousChannelCheck; 

    @Override
    protected void tick(Level level, BlockPos pos, BlockState state) {
        SwitchData switchData = SwitchData.get(level);
        if(this.previousChannelCheck == null){
            this.previousChannelCheck = switchData.isActive(this.getChannel());
        }
        boolean channelActive = switchData.isActive(this.getChannel());
        if (!this.previousChannelCheck && channelActive)
        {
            level.setBlockAndUpdate(pos, state.setValue(LeverBlock.POWERED, channelActive));
            this.playSound(state, level, pos);
        } else if(state.getValue(LeverBlock.POWERED)){
            level.setBlockAndUpdate(pos, state.setValue(LeverBlock.POWERED, false));
            this.playSound(state, level, pos);
        }
       this.previousChannelCheck = channelActive;
    }
}
