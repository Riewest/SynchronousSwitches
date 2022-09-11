package com.suicidesquid.syncswitch.blocks.Switches;

import javax.annotation.Nullable;

import com.suicidesquid.syncswitch.blocks.base.BaseSwitchBlock;
import com.suicidesquid.syncswitch.setup.Registration;
import com.suicidesquid.syncswitch.tiles.Switches.VanillaSwitchBlockTile;

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
        super(properties.noCollission().noOcclusion());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return Registration.VANILLA_SWITCH_BLOCK_BE.get().create(pos, state);
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == Registration.VANILLA_SWITCH_BLOCK_BE.get() ? VanillaSwitchBlockTile::tick : null;
    }

}
