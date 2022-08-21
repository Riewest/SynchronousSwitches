package com.suicidesquid.syncswitch.items;

import com.suicidesquid.syncswitch.data.SwitchData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class UsableSwitchBlockItem extends BaseSwitchBlockItem{

    public UsableSwitchBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    protected void toggleActive(Level level, CompoundTag tag) {
        if(!level.isClientSide && tag.contains("channel")){
            SwitchData switchData = SwitchData.get(level);
            switchData.toggleActive(tag.getString("channel"));
            
        }
    }
    
}
