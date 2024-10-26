package com.suicidesquid.syncswitch.blocks.base;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.setup.Registration;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BaseSwitchBlock extends LeverBlock{

    private static final int SHAPE_SCALAR = 16;
    // public static final BooleanProperty SILENT = BooleanProperty.create("silent");

    public BaseSwitchBlock(Block.Properties properties) {
        super(properties);
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
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		if (context.getClickedFace().getAxis() == Direction.Axis.Y)
			return this.defaultBlockState()
					.setValue(FACE, context.getClickedFace().getOpposite() == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)
					.setValue(FACING, context.getHorizontalDirection());
		return this.defaultBlockState().setValue(FACE, AttachFace.WALL).setValue(FACING, context.getClickedFace());
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
                if (!switchtile.hasChannel() && held.isEmpty()){
                    return super.useWithoutItem(state, world, pos, player, hit);
                } else {
                    world.setBlockAndUpdate(pos, state.setValue(POWERED, switchData.toggleActive(switchtile.getChannel())));
                    world.updateNeighborsAt(pos.relative(BaseChannelTile.getConnectedDirection(state).getOpposite()), this);
                    switchtile.playSound(state, world, pos);
                }
            }
                
            return InteractionResult.CONSUME;
        }
        

        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide() && hand == InteractionHand.MAIN_HAND){
            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof BaseChannelTile){
                BaseChannelTile switchtile = (BaseChannelTile) tile;
                ItemStack held = player.getItemInHand(hand);
                SwitchData switchData = SwitchData.get(world);
                if(switchtile.processInteraction(held, player, switchData))
                    return ItemInteractionResult.CONSUME;
                if (!switchtile.hasChannel() && held.isEmpty()){
                    return super.useItemOn(stack, state, world, pos, player, hand, hit);
                } else {
                    world.setBlockAndUpdate(pos, state.setValue(POWERED, switchData.toggleActive(switchtile.getChannel())));
                    world.updateNeighborsAt(pos.relative(BaseChannelTile.getConnectedDirection(state).getOpposite()), this);
                    switchtile.playSound(state, world, pos);
                }
            }
                
            return ItemInteractionResult.CONSUME;
        }
        

        return super.useItemOn(stack, state, world, pos, player, hand, hit);
    }


    

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(BlockStateProperties.FACING, rot.rotate(state.getValue(BlockStateProperties.FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(BlockStateProperties.FACING)));
	}

    public static VoxelShape translateAxis(VoxelShape shape, Axis axis, boolean positive){
		double moveAmount;
		if (positive)
			moveAmount = 1 - shape.max(axis);
		else
			moveAmount = -shape.min(axis);
		shape.move(0, moveAmount, 0);
		return switch (axis) {
			case X -> shape.move(moveAmount, 0, 0);
			case Z -> shape.move(0, 0, moveAmount);
			default -> shape.move(0, moveAmount, 0);
		};
	}

	public static VoxelShape rotateCardinal(VoxelShape shape){
		return Block.box(SHAPE_SCALAR * shape.min(Axis.Z), SHAPE_SCALAR * shape.min(Axis.Y), SHAPE_SCALAR * shape.min(Axis.X), SHAPE_SCALAR * shape.max(Axis.Z), SHAPE_SCALAR * shape.max(Axis.Y), SHAPE_SCALAR * shape.max(Axis.X));
	}

	public static VoxelShape rotateWall(VoxelShape shape, Direction direction){
		VoxelShape returnShape;
		switch (direction) {
			case NORTH:
			case SOUTH:
				returnShape = Block.box(SHAPE_SCALAR * shape.min(Axis.X), SHAPE_SCALAR * shape.min(Axis.Z), SHAPE_SCALAR * shape.min(Axis.Y), SHAPE_SCALAR * shape.max(Axis.X), SHAPE_SCALAR * shape.max(Axis.Z), SHAPE_SCALAR * shape.max(Axis.Y));
				if(direction == Direction.NORTH)
					returnShape = translateAxis(returnShape, Axis.Z, true);
				break;
		
			default:
				returnShape = Block.box(SHAPE_SCALAR * shape.min(Axis.Y), SHAPE_SCALAR * shape.min(Axis.X), SHAPE_SCALAR * shape.min(Axis.Z), SHAPE_SCALAR * shape.max(Axis.Y), SHAPE_SCALAR * shape.max(Axis.X), SHAPE_SCALAR * shape.max(Axis.Z));
				if (direction == Direction.WEST)
					returnShape = translateAxis(returnShape, Axis.X, true);
				break;
		}

		return returnShape;
	}


	public static VoxelShape determineShape(VoxelShape defaultShape, BlockState state){
		Direction facing = state.getValue(FACING);
		
		return switch (state.getValue(FACING)) {
			default -> switch (state.getValue(FACE)) {
				case FLOOR -> defaultShape;
				case WALL -> rotateWall(defaultShape, facing);
				case CEILING -> translateAxis(defaultShape, Axis.Y, true);
			};
			case EAST, WEST -> switch (state.getValue(FACE)) {
				case FLOOR -> rotateCardinal(defaultShape);
				case WALL -> rotateWall(rotateCardinal(defaultShape), facing);
				case CEILING -> translateAxis(rotateCardinal(defaultShape), Axis.Y, true);
			};
		};
	}
}
