package com.suicidesquid.syncswitch.blocks.base;

import javax.annotation.Nullable;

import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class BaseChannelBlock extends Block{

    private static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public BaseChannelBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, Boolean.valueOf(false)));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        
        if(placer != null && placer instanceof Player){
            BlockEntity be =level.getBlockEntity(pos);
            if (be instanceof BaseChannelTile tile){
                tile.setPlayer(((Player)placer).getStringUUID());
            }
        }

        super.setPlacedBy(level, pos, state, placer, stack);
    }


    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide() && hand == InteractionHand.MAIN_HAND){
            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof BaseChannelTile){
                BaseChannelTile switchtile = (BaseChannelTile) tile;
                ItemStack held = player.getItemInHand(hand);
                SwitchData switchData = SwitchData.get(world);
                if(switchtile.processInteraction(held, player, switchData))
                    return InteractionResult.CONSUME;
            }
        }
        return super.use(state, world, pos, player, hand, hit);
    }


    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        super.createBlockStateDefinition(builder);
    }
    
}
