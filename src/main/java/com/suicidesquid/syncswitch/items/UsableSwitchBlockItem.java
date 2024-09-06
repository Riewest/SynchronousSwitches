package com.suicidesquid.syncswitch.items;

import com.suicidesquid.syncswitch.data.SwitchData;
import com.suicidesquid.syncswitch.setup.LangInit;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class UsableSwitchBlockItem extends BaseSwitchBlockItem{

    public UsableSwitchBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected void toggleActive(Level level, Player player, String channel) {
        if(!level.isClientSide){
            SwitchData switchData = SwitchData.get(level);
            boolean newState = switchData.toggleActive(channel);
            player.displayClientMessage(Component.translatable(LangInit.CHANNEL).append(Component.translatable(newState ? LangInit.ON : LangInit.OFF)), true);
        }
    }
}
