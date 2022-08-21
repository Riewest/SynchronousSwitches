package com.suicidesquid.syncswitch.init;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import com.suicidesquid.syncswitch.blocks.ChannelInputBlock;
import com.suicidesquid.syncswitch.blocks.ChannelOutputBlock;
import com.suicidesquid.syncswitch.blocks.BaseLightBlock;
import com.suicidesquid.syncswitch.blocks.Switches.BigButtonBlock;
import com.suicidesquid.syncswitch.blocks.Switches.EStopButtonBlock;
import com.suicidesquid.syncswitch.blocks.Switches.IOSwitchBlock;
import com.suicidesquid.syncswitch.blocks.Switches.SwitchBlock;
import com.suicidesquid.syncswitch.blocks.Switches.VanillaSwitchBlock;
import com.suicidesquid.syncswitch.blocks.base.BaseChannelBlock;
import com.suicidesquid.syncswitch.blocks.base.BaseSwitchBlock;
import com.suicidesquid.syncswitch.items.BaseSwitchBlockItem;
import com.suicidesquid.syncswitch.items.UsableSwitchBlockItem;
import com.suicidesquid.syncswitch.SynchronousSwitches;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInit {
    private static final int LIGHT_LEVEL = 15;

    public static final DeferredRegister<Block> CHANNEL_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SynchronousSwitches.MODID);


    public static final RegistryObject<Block> CHANNEL_OUTPUT_BLOCK = CHANNEL_BLOCKS.register("channel_output",() -> new ChannelOutputBlock(BlockBehaviour.Properties.of(Material.STONE)));
    public static final RegistryObject<Block> CHANNEL_INPUT_BLOCK = CHANNEL_BLOCKS.register("channel_input",() -> new ChannelInputBlock(BlockBehaviour.Properties.of(Material.STONE)));
    
    // Switches
    public static final RegistryObject<Block> SWITCH_BLOCK = CHANNEL_BLOCKS.register("switch_block",() -> new SwitchBlock(BlockBehaviour.Properties.of(Material.STONE).noCollission().noCollission()));
    public static final RegistryObject<Block> BIG_BUTTON_BLOCK = CHANNEL_BLOCKS.register("big_button",() -> new BigButtonBlock(BlockBehaviour.Properties.of(Material.STONE).noCollission().noCollission()));
    public static final RegistryObject<Block> ESTOP_BUTTON_BLOCK = CHANNEL_BLOCKS.register("estop_button",() -> new EStopButtonBlock(BlockBehaviour.Properties.of(Material.STONE).noCollission().noCollission()));
    public static final RegistryObject<Block> IO_SWITCH_BLOCK = CHANNEL_BLOCKS.register("io_switch",() -> new IOSwitchBlock(BlockBehaviour.Properties.of(Material.STONE).noCollission().noCollission()));
    public static final RegistryObject<Block> VANILLA_SWITCH_BLOCK = CHANNEL_BLOCKS.register("vanilla_switch_block",() -> new VanillaSwitchBlock(BlockBehaviour.Properties.of(Material.STONE).noCollission().noCollission()));

    // Lights
    public static final RegistryObject<Block> TEST_LIGHT_BLOCK = CHANNEL_BLOCKS.register("light_block",() -> new BaseLightBlock(BlockBehaviour.Properties.of(Material.STONE).lightLevel(getLightLevel())));

    @SubscribeEvent
    public static void onRegisterItems(final RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)){
            CHANNEL_BLOCKS.getEntries().forEach( (blockRegistryObject) -> {
                Block block = blockRegistryObject.get();
                Item.Properties properties = new Item.Properties().tab(ItemInit.ModCreativeTab.instance);
                Supplier<Item> blockItemFactory = () -> (block instanceof BaseChannelBlock) ? new BaseSwitchBlockItem(block, properties) : new UsableSwitchBlockItem(block, properties);
                event.register(ForgeRegistries.Keys.ITEMS, blockRegistryObject.getId(), blockItemFactory);
            });
        }
    }

    private static ToIntFunction<BlockState> getLightLevel() {
		return (state) -> {
			return (Boolean)state.getValue(BaseLightBlock.LIT) ? LIGHT_LEVEL : 0;
		};
	}
}
