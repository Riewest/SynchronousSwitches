package com.suicidesquid.syncswitch.items;

import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.setup.LangInit;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class UsableSwitchBlockItem extends BaseSwitchBlockItem{

    public UsableSwitchBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected void toggleActive(Level level, Player player, CompoundTag tag) {
        if(!level.isClientSide && tag.contains("channel")){
            SwitchData switchData = SwitchData.get(level);
            boolean newState = switchData.toggleActive(tag.getString("channel"));
            player.displayClientMessage(Component.translatable(LangInit.CHANNEL).append(Component.translatable(newState ? LangInit.ON : LangInit.OFF)), true);
        }
    }
}
