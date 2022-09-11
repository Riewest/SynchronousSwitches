package com.suicidesquid.syncswitch.items;

import java.util.List;

import com.suicidesquid.syncswitch.setup.LangInit;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BaseSwitchBlockItem extends BlockItem{

    public BaseSwitchBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    public String getChannel(ItemStack stack){
        if (stack.hasTag()){
            CompoundTag stackTag = stack.getTag();
            if(stackTag.contains("channel")){
                return stackTag.getString("channel");
            }
        }
        return null;
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level level, Player player, ItemStack stack, BlockState state) {
        boolean flag = super.updateCustomBlockEntityTag(pos, level, player, stack, state);
        BaseChannelTile tile = (BaseChannelTile) level.getBlockEntity(pos);
        if (tile != null){
            String channel = getChannel(stack);
            if (channel != null){
                tile.setChannel(channel);
                return true;
            }
        }
        return flag;
    }
    
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flagIn) {
        String channel = getChannel(stack);
        if (channel != null){
            tooltip.add(new TranslatableComponent(LangInit.CHANNEL).append(channel));
        }
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        if(level.isClientSide())
            if(context.getPlayer().isCrouching())
                return InteractionResult.CONSUME;
            else
                return InteractionResult.PASS;

        BlockEntity be = level.getBlockEntity(context.getClickedPos());
        if (context.getPlayer().isCrouching() && be instanceof BaseChannelTile tile){
            if (!tile.isRedacted() || tile.isPlayer(context.getPlayer().getStringUUID())){
                CompoundTag tag = stack.getOrCreateTag();
                tag.putString("channel", tile.getChannel());
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    protected void toggleActive(Level level, CompoundTag tag){}

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();
        if(player.isCrouching()){
            if (tag.contains("channel"))
                tag.remove("channel");
                if(level.isClientSide)
                    player.displayClientMessage(new TranslatableComponent(LangInit.REMOVE_CHANNEL), true);
            return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, stack);
        }
        toggleActive(level, tag);
        return new InteractionResultHolder<ItemStack>(InteractionResult.SUCCESS, stack);
    }

}
