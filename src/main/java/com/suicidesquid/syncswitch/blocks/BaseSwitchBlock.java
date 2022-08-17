package com.suicidesquid.syncswitch.blocks;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.tiles.BaseSwitchBlockTile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootContext.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class BaseSwitchBlock extends LeverBlock{

    public BaseSwitchBlock(Block.Properties properties) {
        super(properties);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        
        if(placer != null && placer instanceof Player){
            BlockEntity be =level.getBlockEntity(pos);
            if (be instanceof BaseSwitchBlockTile tile){
                tile.setPlayer(((Player)placer).getStringUUID());
            }
        }

        super.setPlacedBy(level, pos, state, placer, stack);
    }

    @Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		if (context.getClickedFace().getAxis() == Direction.Axis.Y)
			return this.defaultBlockState()
					.setValue(FACE, context.getClickedFace().getOpposite() == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)
					.setValue(FACING, context.getHorizontalDirection());
		return this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING, context.getClickedFace());
	}

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        List<ItemStack> drops = new ArrayList<>();
        BaseSwitchBlockTile tile = (BaseSwitchBlockTile) builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (tile != null){
            drops.add(createItem(tile.getChannel()));
        }
        return drops;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        BaseSwitchBlockTile tile = (BaseSwitchBlockTile) level.getBlockEntity(pos);
        return createItem(tile.getChannel());
    }

    private ItemStack createItem(String channel){
        ItemStack stack = new ItemStack(this, 1);
        if (channel != null){
            CompoundTag tag = stack.getOrCreateTag();
            tag.putString("channel", channel);
        }
        return stack;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide() && hand == InteractionHand.MAIN_HAND){
            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof BaseSwitchBlockTile){
                BaseSwitchBlockTile switchtile = (BaseSwitchBlockTile) tile;
                ItemStack held = player.getItemInHand(hand);
                SwitchData switchData = SwitchData.get(world);
                if (player.isShiftKeyDown()){
                    if(switchtile.hasChannel()){
                        player.sendSystemMessage(Component.literal("Channel: " + switchtile.getChannelDisplay(player.getStringUUID()) + " - Active: " + switchData.isActive(switchtile.getChannel())));   
                    } else {
                        player.sendSystemMessage(Component.literal("No Channel"));
                    }
                } else if (held.getItem() == Items.PAPER){
                    String channel = held.getDisplayName().getString().replace("[", "").replace("]", "");
                    switchtile.setChannel(channel);
                    player.sendSystemMessage(Component.literal("Setting Channel: " + channel));
                } else if (held.getItem() == Items.INK_SAC){
                    switchtile.setRedacted(true);
                    player.sendSystemMessage(Component.literal("Redacted Channel"));
                } else if (held.getItem() == Items.WATER_BUCKET){
                    switchtile.setRedacted(false);
                    player.sendSystemMessage(Component.literal("Unredacted Channel"));
                } else if (!switchtile.hasChannel() && held.isEmpty()){
                    return super.use(state, world, pos, player, hand, hit);
                } else {
                    boolean active = switchData.toggleActive(switchtile.getChannel());
                    float f = active ? 0.6F : 0.5F;
                    world.playSound((Player)null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, f);
                    world.setBlockAndUpdate(pos, state.setValue(POWERED, active));
                    world.updateNeighborsAt(pos.relative(BaseSwitchBlockTile.getConnectedDirection(state).getOpposite()), this);
                }
                
            }
                
            return InteractionResult.CONSUME;
        }
        

        return super.use(state, world, pos, player, hand, hit);
    }

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(BlockStateProperties.FACING, rot.rotate(state.getValue(BlockStateProperties.FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(BlockStateProperties.FACING)));
	}

}
