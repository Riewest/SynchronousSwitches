package com.suicidesquid.syncswitch.setup;

import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import com.suicidesquid.syncswitch.SynchronousSwitches;
import com.suicidesquid.syncswitch.blocks.ChannelInputBlock;
import com.suicidesquid.syncswitch.blocks.ChannelOutputBlock;
import com.suicidesquid.syncswitch.blocks.LightPanelBlock;
import com.suicidesquid.syncswitch.blocks.Switches.BigButtonBlock;
import com.suicidesquid.syncswitch.blocks.Switches.EStopButtonBlock;
import com.suicidesquid.syncswitch.blocks.Switches.IOSwitchBlock;
import com.suicidesquid.syncswitch.blocks.Switches.SwitchBlock;
import com.suicidesquid.syncswitch.blocks.Switches.VanillaSwitchBlock;
import com.suicidesquid.syncswitch.blocks.base.BaseChannelBlock;
import com.suicidesquid.syncswitch.blocks.base.BaseLightBlock;
import com.suicidesquid.syncswitch.blocks.buttons.StoneButtonBlock;
import com.suicidesquid.syncswitch.items.BaseSwitchBlockItem;
import com.suicidesquid.syncswitch.items.UsableSwitchBlockItem;
import com.suicidesquid.syncswitch.tiles.ChannelInputTile;
import com.suicidesquid.syncswitch.tiles.ChannelOutputTile;
import com.suicidesquid.syncswitch.tiles.LightBlockTile;
import com.suicidesquid.syncswitch.tiles.LightPanelTile;
import com.suicidesquid.syncswitch.tiles.Buttons.StoneButtonTile;
import com.suicidesquid.syncswitch.tiles.Switches.BigButtonTile;
import com.suicidesquid.syncswitch.tiles.Switches.EStopButtonTile;
import com.suicidesquid.syncswitch.tiles.Switches.IOSwitchTile;
import com.suicidesquid.syncswitch.tiles.Switches.SwitchBlockTile;
import com.suicidesquid.syncswitch.tiles.Switches.VanillaSwitchBlockTile;

import net.minecraft.core.registries.BuiltInRegistries;
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
import net.neoforged.neoforge.registries.DeferredRegister;

public class Registration {
    private static final int LIGHT_LEVEL = 15;

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(SynchronousSwitches.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.createItems(SynchronousSwitches.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, SynchronousSwitches.MODID);

    

    public static void init(IEventBus modEventBus){
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);        
    }

    public static final Item.Properties ITEM_PROPERTIES = new Item.Properties();

    private static <T extends Block> Supplier<T> registerChannelBlock(String name, Supplier<T> block){
        Supplier<T> toReturn = BLOCKS.register(name, block);
        registerChannelBlockItem(name, toReturn);
        return toReturn;
    }

    public static BlockItem getBlockItem(Block block){
        if (block instanceof BaseLightBlock || block instanceof BaseChannelBlock){
            return new BaseSwitchBlockItem(block, ITEM_PROPERTIES);
        } else {
            return new UsableSwitchBlockItem(block, ITEM_PROPERTIES);
        }
    }

    public static <B extends Block> Supplier<Item> registerChannelBlockItem(String name, Supplier<B> block) {
        
        return ITEMS.register(name, () -> getBlockItem(block.get()));
    }

    public static <B extends Block, E extends BlockEntity> Supplier<BlockEntityType<E>> registerChannelBlockEntity(String name, Supplier<B> block, BlockEntitySupplier<E> be){
        return BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(be, block.get()).build(null));
    }

    // Channel Blocks
    public static final Supplier<Block> CHANNEL_OUTPUT_BLOCK = registerChannelBlock("channel_output",() -> new ChannelOutputBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final Supplier<BlockEntityType<ChannelOutputTile>> CHANNEL_OUTPUT_BLOCK_BE = registerChannelBlockEntity("channel_output", CHANNEL_OUTPUT_BLOCK, ChannelOutputTile::new);
    
    public static final Supplier<Block> CHANNEL_INPUT_BLOCK = registerChannelBlock("channel_input",() -> new ChannelInputBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)));
    public static final Supplier<BlockEntityType<ChannelInputTile>> CHANNEL_INPUT_BLOCK_BE = registerChannelBlockEntity("channel_input", CHANNEL_INPUT_BLOCK, ChannelInputTile::new);
    
    // Switches
    public static final Supplier<Block> SWITCH_BLOCK = registerChannelBlock("switch_block",() -> new SwitchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noCollission().noCollission()));
    public static final Supplier<BlockEntityType<SwitchBlockTile>> SWITCH_BLOCK_BE = registerChannelBlockEntity("switch_block",SWITCH_BLOCK, SwitchBlockTile::new);

    public static final Supplier<Block> BIG_BUTTON_BLOCK = registerChannelBlock("big_button",() -> new BigButtonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noCollission().noCollission()));
    public static final Supplier<BlockEntityType<BigButtonTile>> BIG_BUTTON_BLOCK_BE = registerChannelBlockEntity("big_button", BIG_BUTTON_BLOCK, BigButtonTile::new);

    public static final Supplier<Block> ESTOP_BUTTON_BLOCK = registerChannelBlock("estop_button",() -> new EStopButtonBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noCollission().noCollission()));
    public static final Supplier<BlockEntityType<EStopButtonTile>> ESTOP_BUTTON_BLOCK_BE = registerChannelBlockEntity("estop_button",ESTOP_BUTTON_BLOCK, EStopButtonTile::new);

    public static final Supplier<Block> IO_SWITCH_BLOCK = registerChannelBlock("io_switch",() -> new IOSwitchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noCollission().noCollission()));
    public static final Supplier<BlockEntityType<IOSwitchTile>> IO_SWITCH_BLOCK_BE = registerChannelBlockEntity("io_switch", IO_SWITCH_BLOCK, IOSwitchTile::new);

    public static final Supplier<Block> VANILLA_SWITCH_BLOCK = registerChannelBlock("vanilla_switch_block",() -> new VanillaSwitchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noCollission().noCollission()));
    public static final Supplier<BlockEntityType<VanillaSwitchBlockTile>> VANILLA_SWITCH_BLOCK_BE = registerChannelBlockEntity("vanilla_switch_block", VANILLA_SWITCH_BLOCK, VanillaSwitchBlockTile::new);

    // Buttons
    public static final Supplier<Block> STONE_BUTTON_BLOCK = registerChannelBlock("stone_button_block",() -> new StoneButtonBlock(true, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noCollission().noCollission()));
    public static final Supplier<BlockEntityType<StoneButtonTile>> STONE_BUTTON_BLOCK_BE = registerChannelBlockEntity("stone_button_block", STONE_BUTTON_BLOCK, StoneButtonTile::new);

    // Lights
    public static final Supplier<Block> LIGHT_BLOCK = registerChannelBlock("light_block",() -> new BaseLightBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).lightLevel(getLightLevel())));
    public static final Supplier<BlockEntityType<LightBlockTile>> LIGHT_BLOCK_BE = registerChannelBlockEntity("light_block", LIGHT_BLOCK, LightBlockTile::new);

    public static final Supplier<Block> LIGHT_PANEL_BLOCK = registerChannelBlock("light_panel",() -> new LightPanelBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).lightLevel(getLightLevel())));
    public static final Supplier<BlockEntityType<LightPanelTile>> LIGHT_PANEL_BLOCK_BE = registerChannelBlockEntity("light_panel", LIGHT_PANEL_BLOCK, LightPanelTile::new);


    private static ToIntFunction<BlockState> getLightLevel() {
	    return (state) -> {
	    	return (Boolean)state.getValue(BaseLightBlock.LIT) ? LIGHT_LEVEL : 0;
	    };
	}
}
