package com.suicidesquid.syncswitch.init;

import com.suicidesquid.syncswitch.SynchronousSwitches;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SynchronousSwitches.MODID);


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
