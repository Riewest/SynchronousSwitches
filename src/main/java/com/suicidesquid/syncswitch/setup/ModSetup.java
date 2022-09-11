package com.suicidesquid.syncswitch.setup;


import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

// @Mod.EventBusSubscriber(modid = SynchronousSwitches.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {
    public static final String TAB_NAME = "syncswitch";

    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(TAB_NAME) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Registration.SWITCH_BLOCK.get());
        }
    };


    public static void init(final FMLCommonSetupEvent event) {
    }
}
