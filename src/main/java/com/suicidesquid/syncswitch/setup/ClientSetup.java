package com.suicidesquid.syncswitch.setup;

import com.suicidesquid.syncswitch.SynchronousSwitches;

import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;


@EventBusSubscriber(modid = SynchronousSwitches.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientSetup {
    @SubscribeEvent
    public static void init(final FMLClientSetupEvent event) {
    }
}
