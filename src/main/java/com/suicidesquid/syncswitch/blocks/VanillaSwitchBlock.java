package com.suicidesquid.syncswitch.blocks;

import javax.annotation.Nullable;

import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.init.TileEntityInit;
import com.suicidesquid.syncswitch.tiles.VanillaSwitchBlockTile;
import com.suicidesquid.syncswitch.tiles.VanillaSwitchBlockTile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
        super(properties.noCollission().noOcclusion());
        // this.registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.POWERED, Boolean.valueOf(false)));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return TileEntityInit.VANILLA_SWITCH_BLOCK.get().create(pos, state);
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.VANILLA_SWITCH_BLOCK.get() ? VanillaSwitchBlockTile::tick : null;
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!world.isClientSide() && hand == InteractionHand.MAIN_HAND){
            BlockEntity tile = world.getBlockEntity(pos);
            if (tile instanceof VanillaSwitchBlockTile){
                VanillaSwitchBlockTile switchtile = (VanillaSwitchBlockTile) tile;
                ItemStack held = player.getItemInHand(hand);
                SwitchData switchData = SwitchData.get(world);
                if(player.isShiftKeyDown()){
                    player.sendSystemMessage(Component.literal("Channel: " + switchtile.getChannel() + " - Active: " + switchData.isActive(switchtile.getChannel())));
                    
                } else if (held.getItem() == Items.PAPER){
                    String channel = held.getDisplayName().getString();
                    switchtile.setChannel(channel);
                    player.sendSystemMessage(Component.literal("Setting Channel: " + channel));
                } else {
                    
                    
                    boolean active = switchData.toggleActive(switchtile.getChannel());
                    float f = active ? 0.6F : 0.5F;
                    world.playSound((Player)null, pos, SoundEvents.LEVER_CLICK, SoundSource.BLOCKS, 0.3F, f);
                    world.setBlockAndUpdate(pos, state.setValue(POWERED, active));
                    world.updateNeighborsAt(pos.relative(getConnectedDirection(state).getOpposite()), this);
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

    protected int getRedstoneOutput(BlockState state, BlockGetter world, BlockPos pos, Direction side){
        int signal = 0;
        if (state.getBlock() instanceof VanillaSwitchBlock){
            if (state.getValue(BlockStateProperties.POWERED)){
                signal = 15;
            }
        }
        return signal;
    }

}
