package com.suicidesquid.syncswitch.blocks.Switches;

import javax.annotation.Nullable;


import com.suicidesquid.syncswitch.blocks.base.BaseSwitchBlock;
import com.suicidesquid.syncswitch.setup.Registration;
import com.suicidesquid.syncswitch.tiles.Switches.SwitchBlockTile;

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

public class SwitchBlock extends BaseSwitchBlock implements EntityBlock{

	private static final VoxelShape DEFAULT_SHAPE = Block.box(5, 0, 3, 11, 6, 13);
	

    public SwitchBlock(Block.Properties properties) {
        super(properties);
    }

    @Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return determineShape(DEFAULT_SHAPE, state);
	}

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return Registration.SWITCH_BLOCK_BE.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == Registration.SWITCH_BLOCK_BE.get() ? SwitchBlockTile::tick : null;
    }

}
