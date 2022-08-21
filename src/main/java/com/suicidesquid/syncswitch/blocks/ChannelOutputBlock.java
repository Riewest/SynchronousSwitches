package com.suicidesquid.syncswitch.blocks;

import javax.annotation.Nullable;

import com.suicidesquid.syncswitch.blocks.base.BaseChannelBlock;
import com.suicidesquid.syncswitch.init.TileEntityInit;
import com.suicidesquid.syncswitch.tiles.ChannelOutputTile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class ChannelOutputBlock extends BaseChannelBlock implements EntityBlock{

    private static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public ChannelOutputBlock(Block.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.valueOf(false)));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return TileEntityInit.CHANNEL_OUTPUT_BLOCK.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.CHANNEL_OUTPUT_BLOCK.get() ? ChannelOutputTile::tick : null;
    }

    public int getSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
     }

     @Override
     public boolean isSignalSource(BlockState p_60571_) {
         return true;
     }
    

}
