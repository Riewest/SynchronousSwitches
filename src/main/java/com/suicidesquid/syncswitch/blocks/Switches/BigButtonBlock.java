package com.suicidesquid.syncswitch.blocks.Switches;

import javax.annotation.Nullable;

import com.suicidesquid.syncswitch.blocks.base.BaseSwitchBlock;
import com.suicidesquid.syncswitch.init.TileEntityInit;
import com.suicidesquid.syncswitch.tiles.Switches.BigButtonTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BigButtonBlock extends BaseSwitchBlock implements EntityBlock{

    public BigButtonBlock(Block.Properties properties) {
        super(properties);
    }

	private static final VoxelShape DEFAULT_SHAPE = Block.box(1, 0, 1, 15, 2, 15);
	private static final VoxelShape DEFAULT_SHAPE_ON = Block.box(1, 0, 1, 15, 1, 15);


    @Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {

		if(state.getValue(POWERED))
			return determineShape(DEFAULT_SHAPE_ON, state);
		return determineShape(DEFAULT_SHAPE, state);
	}

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return TileEntityInit.BIG_BUTTON_BLOCK.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.BIG_BUTTON_BLOCK.get() ? BigButtonTile::tick : null;
    }

}
