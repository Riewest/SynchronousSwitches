package com.suicidesquid.syncswitch.blocks.Switches;

import org.jspecify.annotations.Nullable;

import com.suicidesquid.syncswitch.blocks.base.BaseSwitchBlock;
import com.suicidesquid.syncswitch.setup.ModRegistration;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class VanillaSwitchBlock extends BaseSwitchBlock implements EntityBlock{


    public VanillaSwitchBlock(Block.Properties properties) {
        super(properties.noCollision().noOcclusion());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModRegistration.VANILLA_SWITCH_BLOCK.blockEntity().get().create(pos, state);
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == ModRegistration.VANILLA_SWITCH_BLOCK.blockEntity().get() ? BaseChannelTile::tick : null;
    }

}
