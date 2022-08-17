package com.suicidesquid.syncswitch.init;

import com.suicidesquid.syncswitch.SynchronousSwitches;
import com.suicidesquid.syncswitch.items.BaseSwitchBlockItem;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SynchronousSwitches.MODID);
    // public static final RegistryObject<Item> SWITCH_BLOCK_ITEM = ITEMS.register("switch_block",() -> new BaseSwitchBlockItem(BlockInit.SWITCH_BLOCK.get()));


    public static class ModCreativeTab extends CreativeModeTab {
        public static final ModCreativeTab instance = new ModCreativeTab(CreativeModeTab.TABS.length, "syncswitch");
        private ModCreativeTab(int index, String label) {
            super(index, label);
        }
    
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(BlockInit.SWITCH_BLOCK.get());
        }
    }
}