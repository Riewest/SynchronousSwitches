package com.suicidesquid.syncswitch.setup;

import com.suicidesquid.syncswitch.SynchronousSwitches;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;


@Mod.EventBusSubscriber(modid = SynchronousSwitches.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {
    @SubscribeEvent
    public static void init(final FMLCommonSetupEvent event) {
    }
}
