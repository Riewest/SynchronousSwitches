package com.suicidesquid.syncswitch.blocks;

import javax.annotation.Nullable;

import com.suicidesquid.syncswitch.blocks.base.BaseChannelBlock;
import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.init.TileEntityInit;
import com.suicidesquid.syncswitch.tiles.ChannelInputTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ChannelInputBlock extends BaseChannelBlock implements EntityBlock{

    public ChannelInputBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return TileEntityInit.CHANNEL_INPUT_BLOCK.get().create(pos, state);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (level.isClientSide)
            return;
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof ChannelInputTile tile){
            boolean flag = level.hasNeighborSignal(pos);
            SwitchData switchData = SwitchData.get(level);
            if (tile.hasChannel() && flag != switchData.isActive(tile.getChannel())) {
                switchData.setActive(tile.getChannel(), flag);
                level.setBlockAndUpdate(pos, state.setValue(POWERED, Boolean.valueOf(flag)));
            }
        }
    }

    
    
}
