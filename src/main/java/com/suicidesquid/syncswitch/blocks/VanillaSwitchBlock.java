package com.suicidesquid.syncswitch.blocks;

import javax.annotation.Nullable;

import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.init.TileEntityInit;
import com.suicidesquid.syncswitch.tiles.SwitchBlockTile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

public class VanillaSwitchBlock extends LeverBlock implements EntityBlock{


    public VanillaSwitchBlock(Block.Properties properties) {
        super(properties);
        // this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.POWERED, Boolean.valueOf(false)));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return TileEntityInit.SWITCH_BLOCK.get().create(pos, state);
    }

    // @Override
    // public boolean isSignalSource(BlockState p_60571_) {
    //     return true;
    // }

    // @Override
    // public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
    //     return this.getRedstoneOutput(state, world, pos, side);
    // }

    // @Override
    // public int getDirectSignal(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
    //     return this.getRedstoneOutput(state, world, pos, side);
    // }

    // @Override
    // public boolean canConnectRedstone(BlockState state, BlockGetter world, BlockPos pos, Direction side) {
    //     return true;
    // }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.SWITCH_BLOCK.get() ? SwitchBlockTile::tick : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide() && hand == InteractionHand.MAIN_HAND){
            BlockEntity tile = world.getBlockEntity(pos);
            System.out.println("tile:" + tile);
            if (tile instanceof SwitchBlockTile){
                SwitchBlockTile switchtile = (SwitchBlockTile) tile;
                ItemStack held = player.getItemInHand(hand);
                SwitchData switchData = SwitchData.get(world);
                if(player.isShiftKeyDown()){
                    player.sendSystemMessage(Component.literal("Channel: " + switchtile.getChannel() + " - Active: " + switchData.isActive(switchtile.getChannel())));
                    return InteractionResult.SUCCESS;
                } else if (held.getItem() == Items.PAPER){
                    String channel = held.getDisplayName().getString();
                    switchtile.setChannel(channel);
                    player.sendSystemMessage(Component.literal("Setting Channel: " + channel));
                } else {
                    switchData.toggleActive(switchtile.getChannel());
                }
                
            }
                
            return super.use(state, world, pos, player, hand, hit);
        }
        

        return super.use(state, world, pos, player, hand, hit);
    }

    // @Override
    // protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    //   builder.add(BlockStateProperties.POWERED);
    //   builder.add(BlockStateProperties.FACING);
    // }

    // @Override
	// public BlockState getStateForPlacement(BlockPlaceContext context) {
	// 	return this.defaultBlockState().setValue(BlockStateProperties.FACING, context.getClickedFace());
	// }

	public BlockState rotate(BlockState state, Rotation rot) {
		return state.setValue(BlockStateProperties.FACING, rot.rotate(state.getValue(BlockStateProperties.FACING)));
	}

	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.getRotation(state.getValue(BlockStateProperties.FACING)));
	}

    protected int getRedstoneOutput(BlockState state, BlockGetter world, BlockPos pos, Direction side){
        int signal = 0;
        if (state.getBlock() instanceof SwitchBlock){
            if (state.getValue(BlockStateProperties.POWERED)){
                signal = 15;
            }
        }
        return signal;
    }

}
