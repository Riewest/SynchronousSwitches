package com.suicidesquid.syncswitch.setup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    private static final List<BlockWithEntity<?, ?>> SWITCHES = new ArrayList<>();
    public static List<BlockWithEntity<?, ?>> getSwitches() { return Collections.unmodifiableList(SWITCHES); }

    private static final List<BlockWithEntity<?, ?>> BUTTONS = new ArrayList<>();
    public static List<BlockWithEntity<?, ?>> getButtons() { return Collections.unmodifiableList(BUTTONS); }

    private static final List<BlockWithEntity<?, ?>> LIGHTS = new ArrayList<>();
    public static List<BlockWithEntity<?, ?>> getLights() { return Collections.unmodifiableList(LIGHTS); }

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

    public record BlockWithEntity<T extends Block, E extends BlockEntity>(
        DeferredBlock<T> block,
        Supplier<BlockEntityType<E>> blockEntity
    ) implements Supplier<T> {
        @Override
        public T get() {
            return block.get();
        }
    }

    private static <T extends Block> DeferredBlock<T> registerChannelBlock(String name, Function<BlockBehaviour.Properties, T> factory, BlockBehaviour.Properties props) {
        DeferredBlock<T> holder = BLOCKS.registerBlock(name, factory, () -> props);
        registerChannelBlockItem(name, holder);
        return holder;
    }

    public static <T extends Block, E extends BlockEntity> BlockWithEntity<T, E> registerChannelBlockWithEntity(String name, Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties props, BlockEntitySupplier<E> beFactory) {
        DeferredBlock<T> block = registerChannelBlock(name, blockFactory, props);
        Supplier<BlockEntityType<E>> be = registerChannelBlockEntity(name, block, beFactory);
        return new BlockWithEntity<>(block, be);
    }
    
    public static <T extends Block, E extends BlockEntity> BlockWithEntity<T, E> registerSwitch(String name, Function<BlockBehaviour.Properties, T> blockFactory, BlockEntitySupplier<E> beFactory) {
        DeferredBlock<T> block = registerChannelBlock(name, blockFactory, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noCollision());
        Supplier<BlockEntityType<E>> be = registerChannelBlockEntity(name, block, beFactory);
        BlockWithEntity<T, E> entry = new BlockWithEntity<>(block, be);
        SWITCHES.add(entry);
        return entry;
    }
    
    public static <T extends Block, E extends BlockEntity> BlockWithEntity<T, E> registerButton(String name, Function<BlockBehaviour.Properties, T> blockFactory, BlockEntitySupplier<E> beFactory) {
        DeferredBlock<T> block = registerChannelBlock(name, blockFactory, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noCollision());
        Supplier<BlockEntityType<E>> be = registerChannelBlockEntity(name, block, beFactory);
        BlockWithEntity<T, E> entry = new BlockWithEntity<>(block, be);
        BUTTONS.add(entry);
        return entry;
    }
    
    public static <T extends Block, E extends BlockEntity> BlockWithEntity<T, E> registerLight(String name, Function<BlockBehaviour.Properties, T> blockFactory, BlockEntitySupplier<E> beFactory) {
        DeferredBlock<T> block = registerChannelBlock(name, blockFactory, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).lightLevel(getLightLevel()));
        Supplier<BlockEntityType<E>> be = registerChannelBlockEntity(name, block, beFactory);
        BlockWithEntity<T, E> entry = new BlockWithEntity<>(block, be);
        LIGHTS.add(entry);
        return entry;
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
    public static final BlockWithEntity<ChannelOutputBlock, ChannelOutputTile> CHANNEL_OUTPUT_BLOCK = registerChannelBlockWithEntity("channel_output", ChannelOutputBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE), ChannelOutputTile::new);
    public static final BlockWithEntity<ChannelInputBlock, ChannelInputTile> CHANNEL_INPUT_BLOCK = registerChannelBlockWithEntity("channel_input", ChannelInputBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE), ChannelInputTile::new);

    // Switches
    public static final BlockWithEntity<SwitchBlock, SwitchBlockTile> SWITCH_BLOCK = registerSwitch("switch_block", SwitchBlock::new, SwitchBlockTile::new);
    public static final BlockWithEntity<EStopButtonBlock, EStopButtonTile> ESTOP_BUTTON_BLOCK = registerSwitch("estop_button", EStopButtonBlock::new, EStopButtonTile::new);
    public static final BlockWithEntity<IOSwitchBlock, IOSwitchTile> IO_SWITCH_BLOCK = registerSwitch("io_switch", IOSwitchBlock::new, IOSwitchTile::new);
    public static final BlockWithEntity<VanillaSwitchBlock, VanillaSwitchBlockTile> VANILLA_SWITCH_BLOCK = registerSwitch("vanilla_switch_block", VanillaSwitchBlock::new, VanillaSwitchBlockTile::new);

    // Buttons
    public static final BlockWithEntity<BigButtonBlock, BigButtonTile> BIG_BUTTON_BLOCK = registerButton("big_button", BigButtonBlock::new, BigButtonTile::new);
    public static final BlockWithEntity<StoneButtonBlock, StoneButtonTile> STONE_BUTTON_BLOCK = registerButton("stone_button_block", StoneButtonBlock::new, StoneButtonTile::new);
    

    // Lights
    public static final BlockWithEntity<BaseLightBlock, LightBlockTile> LIGHT_BLOCK = registerLight("light_block", BaseLightBlock::new, LightBlockTile::new);
    public static final BlockWithEntity<LightPanelBlock, LightPanelTile> LIGHT_PANEL_BLOCK = registerLight("light_panel", LightPanelBlock::new, LightPanelTile::new);

    // Data Components
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> CHANNEL = COMPONENTS.register("channel", () -> DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());

    private static ToIntFunction<BlockState> getLightLevel() {
	    return (state) -> {
	    	return (Boolean)state.getValue(BaseLightBlock.LIT) ? LIGHT_LEVEL : 0;
	    };
	}
}
