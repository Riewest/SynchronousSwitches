package com.suicidesquid.syncswitch.setup;

import com.suicidesquid.syncswitch.SynchronousSwitches;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
@EventBusSubscriber(modid = SynchronousSwitches.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModSetup {
    @SubscribeEvent
    public static void init(final FMLCommonSetupEvent event) {
    }
}
