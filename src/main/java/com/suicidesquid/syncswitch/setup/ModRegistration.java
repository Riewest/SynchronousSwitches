package com.suicidesquid.syncswitch.setup;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import com.mojang.serialization.Codec;
import com.suicidesquid.syncswitch.SynchronousSwitches;
import com.suicidesquid.syncswitch.blocks.ChannelInputBlock;
import com.suicidesquid.syncswitch.blocks.ChannelOutputBlock;
import com.suicidesquid.syncswitch.blocks.LightPanelBlock;
import com.suicidesquid.syncswitch.blocks.Switches.EStopButtonBlock;
import com.suicidesquid.syncswitch.blocks.Switches.IOSwitchBlock;
import com.suicidesquid.syncswitch.blocks.Switches.SwitchBlock;
import com.suicidesquid.syncswitch.blocks.Switches.VanillaSwitchBlock;
import com.suicidesquid.syncswitch.blocks.base.BaseChannelBlock;
import com.suicidesquid.syncswitch.blocks.base.BaseLightBlock;
import com.suicidesquid.syncswitch.blocks.buttons.BigButtonBlock;
import com.suicidesquid.syncswitch.blocks.buttons.StoneButtonBlock;
import com.suicidesquid.syncswitch.items.BaseSwitchBlockItem;
import com.suicidesquid.syncswitch.items.UsableSwitchBlockItem;
import com.suicidesquid.syncswitch.tiles.ChannelInputTile;
import com.suicidesquid.syncswitch.tiles.ChannelOutputTile;
import com.suicidesquid.syncswitch.tiles.LightBlockTile;
import com.suicidesquid.syncswitch.tiles.LightPanelTile;
import com.suicidesquid.syncswitch.tiles.Buttons.BigButtonTile;
import com.suicidesquid.syncswitch.tiles.Buttons.StoneButtonTile;
import com.suicidesquid.syncswitch.tiles.Switches.EStopButtonTile;
import com.suicidesquid.syncswitch.tiles.Switches.IOSwitchTile;
import com.suicidesquid.syncswitch.tiles.Switches.SwitchBlockTile;
import com.suicidesquid.syncswitch.tiles.Switches.VanillaSwitchBlockTile;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRegistration {
    private static final int LIGHT_LEVEL = 15;

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, SynchronousSwitches.MODID);
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SynchronousSwitches.MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SynchronousSwitches.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, SynchronousSwitches.MODID);

    
    

    public static void init(IEventBus modEventBus){
        COMPONENTS.register(modEventBus);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);        
    }

    private static <T extends Block> DeferredBlock<T> registerChannelBlock(String name, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties props) {
        DeferredBlock<T> holder = BLOCKS.registerBlock(name, factory, () -> props);
        registerChannelBlockItem(name, holder);
        return holder;
    }

    public static BlockItem getBlockItem(Block block, Item.Properties props) {
        if (block instanceof BaseLightBlock || block instanceof BaseChannelBlock) {
            return new BaseSwitchBlockItem(block, props);
        } else {
            return new UsableSwitchBlockItem(block, props);
        }
    }

    public static <B extends Block> DeferredItem<BlockItem> registerChannelBlockItem(String name, Supplier<B> block) {
        return ITEMS.registerItem(name, props -> getBlockItem(block.get(), props.useBlockDescriptionPrefix()));
    }

    public static <B extends Block, E extends BlockEntity> Supplier<BlockEntityType<E>> registerChannelBlockEntity(String name, Supplier<B> block, BlockEntitySupplier<E> be) {
        return BLOCK_ENTITIES.register(name, () -> new BlockEntityType<>(be, block.get()));
    }

    // Channel Blocks
    public static final Supplier<Block> CHANNEL_OUTPUT_BLOCK = registerChannelBlock("channel_output", ChannelOutputBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));
    public static final Supplier<BlockEntityType<ChannelOutputTile>> CHANNEL_OUTPUT_BLOCK_BE = registerChannelBlockEntity("channel_output", CHANNEL_OUTPUT_BLOCK, ChannelOutputTile::new);
    
    public static final Supplier<Block> CHANNEL_INPUT_BLOCK = registerChannelBlock("channel_input", ChannelInputBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));
    public static final Supplier<BlockEntityType<ChannelInputTile>> CHANNEL_INPUT_BLOCK_BE = registerChannelBlockEntity("channel_input", CHANNEL_INPUT_BLOCK, ChannelInputTile::new);
    
    // Switches
    public static final Supplier<Block> SWITCH_BLOCK = registerChannelBlock("switch_block", SwitchBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noCollision().noCollision());
    public static final Supplier<BlockEntityType<SwitchBlockTile>> SWITCH_BLOCK_BE = registerChannelBlockEntity("switch_block", SWITCH_BLOCK, SwitchBlockTile::new);

    public static final Supplier<Block> ESTOP_BUTTON_BLOCK = registerChannelBlock("estop_button", EStopButtonBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noCollision().noCollision());
    public static final Supplier<BlockEntityType<EStopButtonTile>> ESTOP_BUTTON_BLOCK_BE = registerChannelBlockEntity("estop_button", ESTOP_BUTTON_BLOCK, EStopButtonTile::new);

    public static final Supplier<Block> IO_SWITCH_BLOCK = registerChannelBlock("io_switch", IOSwitchBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noCollision().noCollision());
    public static final Supplier<BlockEntityType<IOSwitchTile>> IO_SWITCH_BLOCK_BE = registerChannelBlockEntity("io_switch", IO_SWITCH_BLOCK, IOSwitchTile::new);

    public static final Supplier<Block> VANILLA_SWITCH_BLOCK = registerChannelBlock("vanilla_switch_block", VanillaSwitchBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noCollision().noCollision());
    public static final Supplier<BlockEntityType<VanillaSwitchBlockTile>> VANILLA_SWITCH_BLOCK_BE = registerChannelBlockEntity("vanilla_switch_block", VANILLA_SWITCH_BLOCK, VanillaSwitchBlockTile::new);

    // Buttons
    public static final Supplier<Block> STONE_BUTTON_BLOCK = registerChannelBlock("stone_button_block", props -> new StoneButtonBlock(true, props), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noCollision().noCollision());
    public static final Supplier<BlockEntityType<StoneButtonTile>> STONE_BUTTON_BLOCK_BE = registerChannelBlockEntity("stone_button_block", STONE_BUTTON_BLOCK, StoneButtonTile::new);
    public static final Supplier<Block> BIG_BUTTON_BLOCK = registerChannelBlock("big_button", props -> new BigButtonBlock(true, props), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noCollision().noCollision());
    public static final Supplier<BlockEntityType<BigButtonTile>> BIG_BUTTON_BLOCK_BE = registerChannelBlockEntity("big_button", BIG_BUTTON_BLOCK, BigButtonTile::new);

    // Lights
    public static final Supplier<Block> LIGHT_BLOCK = registerChannelBlock("light_block", BaseLightBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).lightLevel(getLightLevel()));
    public static final Supplier<BlockEntityType<LightBlockTile>> LIGHT_BLOCK_BE = registerChannelBlockEntity("light_block", LIGHT_BLOCK, LightBlockTile::new);

    public static final Supplier<Block> LIGHT_PANEL_BLOCK = registerChannelBlock("light_panel", LightPanelBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).lightLevel(getLightLevel()));
    public static final Supplier<BlockEntityType<LightPanelTile>> LIGHT_PANEL_BLOCK_BE = registerChannelBlockEntity("light_panel", LIGHT_PANEL_BLOCK, LightPanelTile::new);

    // Data Components
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> CHANNEL = COMPONENTS.register("channel", () -> DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());

    private static ToIntFunction<BlockState> getLightLevel() {
	    return (state) -> {
	    	return (Boolean)state.getValue(BaseLightBlock.LIT) ? LIGHT_LEVEL : 0;
	    };
	}
}
