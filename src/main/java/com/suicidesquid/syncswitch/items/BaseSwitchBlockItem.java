package com.suicidesquid.syncswitch.items;

import java.util.List;

import com.suicidesquid.syncswitch.blocks.SwitchBlock;
import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.init.ItemInit;
import com.suicidesquid.syncswitch.tiles.BaseSwitchBlockTile;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
        BaseSwitchBlockTile tile = (BaseSwitchBlockTile) level.getBlockEntity(pos);
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
            tooltip.add(Component.literal("Channel: " + channel));
        }
    }

}
