package com.suicidesquid.syncswitch.blocks;

import javax.annotation.Nullable;

import com.suicidesquid.syncswitch.blocks.base.BaseSwitchBlock;
import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.init.TileEntityInit;
import com.suicidesquid.syncswitch.tiles.ChannelOutputTile;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChannelOutputBlock extends Block implements EntityBlock{

    private static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public ChannelOutputBlock(Block.Properties properties) {
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


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return TileEntityInit.CHANNEL_OUTPUT_BLOCK.get().create(pos, state);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return type == TileEntityInit.CHANNEL_OUTPUT_BLOCK.get() ? ChannelOutputTile::tick : null;
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
                    return InteractionResult.PASS;
                }
            }
        }
        return super.use(state, world, pos, player, hand, hit);
    }


    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        builder.add(POWERED);
        super.createBlockStateDefinition(builder);
    }

    public int getSignal(BlockState state, BlockGetter blockGetter, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) ? 15 : 0;
     }

     @Override
     public boolean isSignalSource(BlockState p_60571_) {
         return true;
     }
    

}
