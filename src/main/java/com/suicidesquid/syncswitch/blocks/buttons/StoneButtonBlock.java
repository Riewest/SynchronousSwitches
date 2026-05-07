package com.suicidesquid.syncswitch.blocks.buttons;

import org.jspecify.annotations.Nullable;

import com.suicidesquid.syncswitch.blocks.base.BaseButtonBlock;
import com.suicidesquid.syncswitch.setup.ModRegistration;
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
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModRegistration.STONE_BUTTON_BLOCK_BE.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == ModRegistration.STONE_BUTTON_BLOCK_BE.get() ? StoneButtonTile::tick : null;
    }
    
}
