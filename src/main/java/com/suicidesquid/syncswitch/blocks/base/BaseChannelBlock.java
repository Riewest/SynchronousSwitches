package com.suicidesquid.syncswitch.blocks.base;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.setup.Registration;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;

public class BaseChannelBlock extends Block{

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

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
    public InteractionResult useWithoutItem(BlockState state, Level world, BlockPos pos, Player player, BlockHitResult hit) {
        var hand = InteractionHand.MAIN_HAND;
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
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!level.isClientSide() && hand == InteractionHand.MAIN_HAND){
            BlockEntity tile = level.getBlockEntity(pos);
            if (tile instanceof BaseChannelTile){
                BaseChannelTile switchtile = (BaseChannelTile) tile;
                ItemStack held = player.getItemInHand(hand);
                SwitchData switchData = SwitchData.get(level);
                if(switchtile.processInteraction(held, player, switchData))
                    return ItemInteractionResult.CONSUME;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }


    @Override
    public boolean canConnectRedstone(BlockState state, BlockGetter level, BlockPos pos, @Nullable Direction direction) {
        return true;
    }


    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        BaseChannelTile tile = (BaseChannelTile) builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (tile != null){
            drops.add(createItem(tile.getChannel()));
        }
        return drops;
    }

    @Override
    public ItemStack getCloneItemStack(LevelReader pLevel, BlockPos pPos, BlockState pState) {
        BaseChannelTile tile = (BaseChannelTile) pLevel.getBlockEntity(pPos);
        return createItem(tile.getChannel());
    }

    private ItemStack createItem(String channel){
        ItemStack stack = new ItemStack(this, 1);
        if (channel != null){
            stack.set(Registration.CHANNEL, channel);
        }
        
        return stack;
    }
    
}
