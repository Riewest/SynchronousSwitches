package com.suicidesquid.syncswitch.init;

import java.util.function.Supplier;

import com.suicidesquid.syncswitch.blocks.BaseSwitchBlock;
import com.suicidesquid.syncswitch.blocks.SwitchBlock;
import com.suicidesquid.syncswitch.blocks.VanillaSwitchBlock;
import com.suicidesquid.syncswitch.items.BaseSwitchBlockItem;
import com.suicidesquid.syncswitch.SynchronousSwitches;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInit {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SynchronousSwitches.MODID);

    public static final RegistryObject<Block> SWITCH_BLOCK = BLOCKS.register("switch_block",() -> new SwitchBlock(BlockBehaviour.Properties.of(Material.STONE)));
    public static final RegistryObject<Block> VANILLA_SWITCH_BLOCK = BLOCKS.register("vanilla_switch_block",() -> new VanillaSwitchBlock(BlockBehaviour.Properties.of(Material.STONE)));

    @SubscribeEvent
    public static void onRegisterItems(final RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)){
            BLOCKS.getEntries().forEach( (blockRegistryObject) -> {
                Block block = blockRegistryObject.get();
                Item.Properties properties = new Item.Properties().tab(ItemInit.ModCreativeTab.instance);
                Supplier<Item> blockItemFactory = () -> block instanceof BaseSwitchBlock ? new BaseSwitchBlockItem(block, properties) : new BlockItem(block, properties);
                event.register(ForgeRegistries.Keys.ITEMS, blockRegistryObject.getId(), blockItemFactory);
            });
        }
    }
}
