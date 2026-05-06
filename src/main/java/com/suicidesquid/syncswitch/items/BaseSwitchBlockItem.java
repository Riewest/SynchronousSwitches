package com.suicidesquid.syncswitch.items;

import java.util.function.Consumer;

import com.suicidesquid.syncswitch.setup.LangInit;
import com.suicidesquid.syncswitch.setup.ModRegistration;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
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
        if (stack.has(ModRegistration.CHANNEL)){
            return stack.get(ModRegistration.CHANNEL);
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
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag tooltipFlag) {
        String channel = getChannel(stack);
        if (channel != null){
            tooltip.accept(Component.translatable(LangInit.CHANNEL).append(channel));
        }
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Player player = context.getPlayer();
        Level level = context.getLevel();
        if(level.isClientSide())
            if(player.isCrouching())
                return InteractionResult.CONSUME;
            else
                return InteractionResult.PASS;

        BlockEntity be = level.getBlockEntity(context.getClickedPos());
        
        if (player.isCrouching() && be instanceof BaseChannelTile tile){
            if (!tile.isRedacted() || tile.isPlayer(player.getStringUUID())){
                String channel = tile.getChannel();
                stack.set(ModRegistration.CHANNEL, channel);
                player.displayClientMessage(Component.translatable(LangInit.COPIED).append(channel), true);
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    protected void toggleActive(Level level, Player player, String channel){}

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if(player.isCrouching()){
            if (stack.has(ModRegistration.CHANNEL))
                stack.remove(ModRegistration.CHANNEL);
                if(level.isClientSide())
                    player.displayClientMessage(Component.translatable(LangInit.REMOVE_CHANNEL), true);
            return InteractionResult.SUCCESS;
        }
        if (stack.has(ModRegistration.CHANNEL)){
            toggleActive(level, player, stack.get(ModRegistration.CHANNEL));
        }
        return InteractionResult.SUCCESS;
    }

}
