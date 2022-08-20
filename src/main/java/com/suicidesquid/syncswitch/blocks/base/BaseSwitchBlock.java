package com.suicidesquid.syncswitch.blocks.base;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
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
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootContext;
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


    // private BlockState updateRedstone(BlockState state, BlockGetter blockGetter, BlockPos pos){
    //     BlockEntity be = blockGetter.getBlockEntity(pos);
    //     if(be instanceof BaseSwitchBlockTile tile){
    //         if (tile.hasChannel()){
    //             Level level = tile.getLevel();
    //             SwitchData switchData = SwitchData.get(level);
    //             boolean channelActive = switchData.isActive(tile.getChannel());
    //             if (channelActive != state.getValue(LeverBlock.POWERED)){
    //                 level.setBlockAndUpdate(pos, state.setValue(LeverBlock.POWERED, channelActive));
    //                 return blockGetter.getBlockState(pos);
    //             }
    //         }
    //     }
    //     return state;
    // }

    // @Override
    // public int getSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction) {
    //     updateRedstone(state, blockGetter, pos);
    //     return super.getSignal(state, blockGetter, pos, direction);
    // }

    // @Override
    // public int getDirectSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction) {
    //     updateRedstone(state, blockGetter, pos);
    //     return super.getDirectSignal(state, blockGetter, pos, direction);
    // }

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
        BaseChannelTile tile = (BaseChannelTile) builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (tile != null){
            drops.add(createItem(tile.getChannel()));
        }
        return drops;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        BaseChannelTile tile = (BaseChannelTile) level.getBlockEntity(pos);
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
            if (tile instanceof BaseChannelTile){
                BaseChannelTile switchtile = (BaseChannelTile) tile;
                ItemStack held = player.getItemInHand(hand);
                SwitchData switchData = SwitchData.get(world);
                if(switchtile.processInteraction(held, player))
                    return InteractionResult.CONSUME;
                if (player.isShiftKeyDown()){
                    if(switchtile.hasChannel()){
                        player.sendSystemMessage(Component.literal("Channel: " + switchtile.getChannelDisplay(player.getStringUUID()) + " - Active: " + switchData.isActive(switchtile.getChannel())));   
                    } else {
                        player.sendSystemMessage(Component.literal("No Channel"));
                    }
                } else if (!switchtile.hasChannel() && held.isEmpty()){
                    return super.use(state, world, pos, player, hand, hit);
                } else {
                    world.setBlockAndUpdate(pos, state.setValue(POWERED, switchData.toggleActive(switchtile.getChannel())));
                    world.updateNeighborsAt(pos.relative(BaseChannelTile.getConnectedDirection(state).getOpposite()), this);
                    switchtile.playSound(state, world, pos);
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

    private VoxelShape translateAxis(VoxelShape shape, Axis axis, boolean positive){
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

	private VoxelShape rotateCardinal(VoxelShape shape){
		return Block.box(SHAPE_SCALAR * shape.min(Axis.Z), SHAPE_SCALAR * shape.min(Axis.Y), SHAPE_SCALAR * shape.min(Axis.X), SHAPE_SCALAR * shape.max(Axis.Z), SHAPE_SCALAR * shape.max(Axis.Y), SHAPE_SCALAR * shape.max(Axis.X));
	}

	private VoxelShape rotateWall(VoxelShape shape, Direction direction){
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


	protected VoxelShape determineShape(VoxelShape defaultShape, BlockState state){
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
