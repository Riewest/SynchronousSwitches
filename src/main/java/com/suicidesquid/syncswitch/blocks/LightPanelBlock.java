package com.suicidesquid.syncswitch.blocks;

import org.jspecify.annotations.Nullable;

import com.suicidesquid.syncswitch.blocks.base.BaseDirectionalLightBlock;
import com.suicidesquid.syncswitch.blocks.base.BaseSwitchBlock;
import com.suicidesquid.syncswitch.setup.ModRegistration;
import com.suicidesquid.syncswitch.tiles.LightPanelTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LightPanelBlock extends BaseDirectionalLightBlock {

    
    private static final VoxelShape DEFAULT_SHAPE = Block.box(0, 0, 0, 16, 1, 16);


    public LightPanelBlock(Properties properties) {
        super(properties);
        
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext ctx) {
        return BaseSwitchBlock.determineShape(DEFAULT_SHAPE, state);
    }
    

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModRegistration.LIGHT_PANEL_BLOCK.blockEntity().get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == ModRegistration.LIGHT_PANEL_BLOCK.blockEntity().get() ? LightPanelTile::tick : null;
    }

    

}
