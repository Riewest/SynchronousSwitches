package com.suicidesquid.syncswitch.blocks.buttons;

import javax.annotation.Nullable;

import com.suicidesquid.syncswitch.blocks.base.BaseButtonBlock;
import com.suicidesquid.syncswitch.setup.Registration;
import com.suicidesquid.syncswitch.tiles.Buttons.StoneButtonTile;

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

public class StoneButtonBlock extends BaseButtonBlock implements EntityBlock{

    public StoneButtonBlock(boolean bool, Properties properties) {
        super(bool, properties);
        //TODO Auto-generated constructor stub
    }

    // private static final VoxelShape DEFAULT_SHAPE = Block.box(1, 0, 1, 15, 2, 15);
	// private static final VoxelShape DEFAULT_SHAPE_ON = Block.box(1, 0, 1, 15, 1, 15);


    // @Override
	// public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {

	// 	if(state.getValue(POWERED))
	// 		return determineShape(DEFAULT_SHAPE_ON, state);
	// 	return determineShape(DEFAULT_SHAPE, state);
	// }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return Registration.STONE_BUTTON_BLOCK_BE.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == Registration.STONE_BUTTON_BLOCK_BE.get() ? StoneButtonTile::tick : null;
    }
    
}
