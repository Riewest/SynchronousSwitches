package com.suicidesquid.syncswitch.blocks.buttons;

import org.jspecify.annotations.Nullable;

import com.suicidesquid.syncswitch.blocks.base.BaseButtonBlock;
import com.suicidesquid.syncswitch.setup.ModRegistration;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;

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

public class BigButtonBlock extends BaseButtonBlock implements EntityBlock{

    public BigButtonBlock(Properties properties) {
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
        return ModRegistration.BIG_BUTTON_BLOCK.blockEntity().get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == ModRegistration.BIG_BUTTON_BLOCK.blockEntity().get() ? BaseChannelTile::tick : null;
    }

}
