package com.suicidesquid.syncswitch.data;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

public class SwitchData extends SavedData{
    private final Map<String, SwitchState> switchMap = new HashMap<>();

    private SwitchState getSwitchState(String channel){
        return switchMap.computeIfAbsent(channel, ch -> new SwitchState(false));
    }

    @Nonnull
    public static SwitchData get(Level level){
        if (level.isClientSide){
            throw new RuntimeException("Don't access this client-side!");
        }
        DimensionDataStorage storage = ((ServerLevel)level).getDataStorage();
        return storage.computeIfAbsent(SwitchData::new, SwitchData::new, "switchdata");
    }

    public boolean isActive(String channel){
        SwitchState switchState = getSwitchState(channel);
        return switchState.isActive();
    }

    public void toggleActive(String channel){
        SwitchState switchState = getSwitchState(channel);
        switchState.toggleActive();
        switchMap.put(channel, switchState);
        setDirty();
    }

    public SwitchData(){}

    public SwitchData(CompoundTag tag){
        ListTag list = tag.getList("switchdata", Tag.TAG_COMPOUND);
        for (Tag t : list){
            CompoundTag switchTag = (CompoundTag) t;
            SwitchState switchState = new SwitchState(switchTag.getBoolean("active"));
            switchMap.put(switchTag.getString("channel"), switchState);
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        switchMap.forEach((channel, switchstate) -> {
            CompoundTag switchTag = new CompoundTag();
            switchTag.putBoolean("active", switchstate.isActive());
            switchTag.putString("channel", channel);
            list.add(switchTag);
        });
        tag.put("switchdata", list);
        return tag;
    }

    
}
