package com.suicidesquid.syncswitch.blocks.Switches;

import javax.annotation.Nullable;

import com.suicidesquid.syncswitch.blocks.base.BaseSwitchBlock;
import com.suicidesquid.syncswitch.init.TileEntityInit;
import com.suicidesquid.syncswitch.tiles.Switches.EStopButtonTile;

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

public class EStopButtonBlock extends BaseSwitchBlock implements EntityBlock{

    public EStopButtonBlock(Block.Properties properties) {
        super(properties);
    }

	private static final VoxelShape DEFAULT_SHAPE = Block.box(3, 0, 3, 13, 3.5, 13);
	private static final VoxelShape DEFAULT_SHAPE_ON = Block.box(3, 0, 3, 13, 4.5, 13);


    @Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {

		if(state.getValue(POWERED))
			return determineShape(DEFAULT_SHAPE_ON, state);
		return determineShape(DEFAULT_SHAPE, state);
	}

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return TileEntityInit.ESTOP_BUTTON_BLOCK.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.ESTOP_BUTTON_BLOCK.get() ? EStopButtonTile::tick : null;
    }

}