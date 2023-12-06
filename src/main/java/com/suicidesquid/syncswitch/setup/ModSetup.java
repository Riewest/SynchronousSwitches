package com.suicidesquid.syncswitch.setup;

import com.suicidesquid.syncswitch.SynchronousSwitches;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = SynchronousSwitches.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModSetup {
    public static final String TAB_NAME = "syncswitch";
    public static CreativeModeTab SYNC_SWITCH_TAB;

    // public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab(TAB_NAME) {
    //     @Override
    //     public ItemStack makeIcon() {
    //         return new ItemStack(Registration.SWITCH_BLOCK.get());
    //     }
    // };

    @SubscribeEvent
    public static void onCustomTab(CreativeModeTabEvent.Register event) {
        SYNC_SWITCH_TAB = event.registerCreativeModeTab(new ResourceLocation(SynchronousSwitches.MODID, TAB_NAME), builder -> {
            builder.title(Component.translatable("itemGroup." + SynchronousSwitches.MODID))
                    .icon(() -> new ItemStack(Registration.SWITCH_BLOCK.get()));
                    // .displayItems((enabledFeatures, output, tab) -> {
                    //     for (RegistryObject<Item> regItem : Registration.ITEMS.getEntries()) {
                    //         output.accept(regItem.get());
                    //     }
                    // });
        });
    }

    public static void init(final FMLCommonSetupEvent event) {
    }
}
