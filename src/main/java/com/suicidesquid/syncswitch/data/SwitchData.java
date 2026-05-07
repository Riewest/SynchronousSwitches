package com.suicidesquid.syncswitch.data;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.SavedDataStorage;

public class SwitchData extends SavedData {
    public static final SavedDataType<SwitchData> TYPE = new SavedDataType<>(
        Identifier.fromNamespaceAndPath("syncswitch", "switchdata"),
        SwitchData::new,
        Codec.unboundedMap(Codec.STRING, Codec.BOOL).xmap(
            map -> {
                SwitchData data = new SwitchData();
                map.forEach((channel, active) -> data.switchMap.put(channel, new SwitchState(active)));
                return data;
            },
            data -> {
                Map<String, Boolean> map = new HashMap<>();
                data.switchMap.forEach((channel, state) -> map.put(channel, Boolean.valueOf(state.isActive())));
                return map;
            }
        )
    );

    private final Map<String, SwitchState> switchMap = new HashMap<>();

    private SwitchState getSwitchState(String channel) {
        return switchMap.computeIfAbsent(channel, ch -> new SwitchState(false));
    }

    @Nonnull
    public static SwitchData get(Level level) {
        if (level.isClientSide()) {
            throw new RuntimeException("Don't access this client-side!");
        }
        SavedDataStorage storage = ((ServerLevel) level).getDataStorage();
        return storage.computeIfAbsent(TYPE);
    }

    public boolean isActive(String channel) {
        SwitchState switchState = getSwitchState(channel);
        return switchState.isActive();
    }

    public boolean toggleActive(String channel) {
        SwitchState switchState = getSwitchState(channel);
        switchState.toggleActive();
        switchMap.put(channel, switchState);
        setDirty();
        return switchState.isActive();
    }

    public boolean setActive(String channel, boolean active) {
        SwitchState switchState = getSwitchState(channel);
        switchState.setActive(active);
        switchMap.put(channel, switchState);
        setDirty();
        return switchState.isActive();
    }

    public SwitchData() {}
}
