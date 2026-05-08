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
import com.suicidesquid.syncswitch.tiles.Base.BaseButtonTile;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelBlockTile;
import com.suicidesquid.syncswitch.tiles.Base.BaseChannelTile;
import com.suicidesquid.syncswitch.tiles.Base.BaseLightTile;

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

    @SuppressWarnings("unchecked")
    private static <B extends Block, E extends BlockEntity> Supplier<BlockEntityType<E>> registerBlockEntityWithRef(
            String name, Supplier<B> block, Function<Supplier<BlockEntityType<E>>, BlockEntitySupplier<E>> fn) {
        Supplier<BlockEntityType<E>>[] ref = new Supplier[1];
        ref[0] = BLOCK_ENTITIES.register(name, () -> new BlockEntityType<>(fn.apply(ref[0]), block.get()));
        return ref[0];
    }

    public static <T extends Block> BlockWithEntity<T, BaseChannelBlockTile> registerIOBlock(String name, Function<BlockBehaviour.Properties, T> blockFactory, BlockBehaviour.Properties props) {
        DeferredBlock<T> block = registerChannelBlock(name, blockFactory, props);
        Supplier<BlockEntityType<BaseChannelBlockTile>> be = registerBlockEntityWithRef(name, block,
                type -> (pos, state) -> new BaseChannelBlockTile(type.get(), pos, state));
        return new BlockWithEntity<>(block, be);
    }

    public static <T extends Block> BlockWithEntity<T, BaseChannelTile> registerSwitch(String name, Function<BlockBehaviour.Properties, T> blockFactory) {
        DeferredBlock<T> block = registerChannelBlock(name, blockFactory, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noCollision());
        Supplier<BlockEntityType<BaseChannelTile>> be = registerBlockEntityWithRef(name, block,
                type -> (pos, state) -> new BaseChannelTile(type.get(), pos, state));
        BlockWithEntity<T, BaseChannelTile> entry = new BlockWithEntity<>(block, be);
        SWITCHES.add(entry);
        return entry;
    }

    public static <T extends Block> BlockWithEntity<T, BaseButtonTile> registerButton(String name, Function<BlockBehaviour.Properties, T> blockFactory) {
        DeferredBlock<T> block = registerChannelBlock(name, blockFactory, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noCollision());
        Supplier<BlockEntityType<BaseButtonTile>> be = registerBlockEntityWithRef(name, block,
                type -> (pos, state) -> new BaseButtonTile(type.get(), pos, state));
        BlockWithEntity<T, BaseButtonTile> entry = new BlockWithEntity<>(block, be);
        BUTTONS.add(entry);
        return entry;
    }

    public static <T extends Block> BlockWithEntity<T, BaseLightTile> registerLight(String name, Function<BlockBehaviour.Properties, T> blockFactory) {
        DeferredBlock<T> block = registerChannelBlock(name, blockFactory, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).lightLevel(getLightLevel()));
        Supplier<BlockEntityType<BaseLightTile>> be = registerBlockEntityWithRef(name, block,
                type -> (pos, state) -> new BaseLightTile(type.get(), pos, state));
        BlockWithEntity<T, BaseLightTile> entry = new BlockWithEntity<>(block, be);
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

    // Channel Blocks
    public static final BlockWithEntity<ChannelOutputBlock, BaseChannelBlockTile> CHANNEL_OUTPUT_BLOCK = registerIOBlock("channel_output", ChannelOutputBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));
    public static final BlockWithEntity<ChannelInputBlock, BaseChannelBlockTile> CHANNEL_INPUT_BLOCK = registerIOBlock("channel_input", ChannelInputBlock::new, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE));

    // Switches
    public static final BlockWithEntity<SwitchBlock, BaseChannelTile> SWITCH_BLOCK = registerSwitch("switch_block", SwitchBlock::new);
    public static final BlockWithEntity<EStopButtonBlock, BaseChannelTile> ESTOP_BUTTON_BLOCK = registerSwitch("estop_button", EStopButtonBlock::new);
    public static final BlockWithEntity<IOSwitchBlock, BaseChannelTile> IO_SWITCH_BLOCK = registerSwitch("io_switch", IOSwitchBlock::new);
    public static final BlockWithEntity<VanillaSwitchBlock, BaseChannelTile> VANILLA_SWITCH_BLOCK = registerSwitch("vanilla_switch_block", VanillaSwitchBlock::new);

    // Buttons
    public static final BlockWithEntity<BigButtonBlock, BaseButtonTile> BIG_BUTTON_BLOCK = registerButton("big_button", BigButtonBlock::new);
    public static final BlockWithEntity<StoneButtonBlock, BaseButtonTile> STONE_BUTTON_BLOCK = registerButton("stone_button_block", StoneButtonBlock::new);

    // Lights
    public static final BlockWithEntity<BaseLightBlock, BaseLightTile> LIGHT_BLOCK = registerLight("light_block", BaseLightBlock::new);
    public static final BlockWithEntity<LightPanelBlock, BaseLightTile> LIGHT_PANEL_BLOCK = registerLight("light_panel", LightPanelBlock::new);

    // Data Components
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> CHANNEL = COMPONENTS.register("channel", () -> DataComponentType.<String>builder().persistent(Codec.STRING).networkSynchronized(ByteBufCodecs.STRING_UTF8).build());

    private static ToIntFunction<BlockState> getLightLevel() {
	    return (state) -> {
	    	return (Boolean)state.getValue(BaseLightBlock.LIT) ? LIGHT_LEVEL : 0;
	    };
	}
}
