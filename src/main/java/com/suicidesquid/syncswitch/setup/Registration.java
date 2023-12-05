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

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.BlockEntityType.BlockEntitySupplier;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Registration {
    private static final int LIGHT_LEVEL = 15;

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SynchronousSwitches.MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SynchronousSwitches.MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, SynchronousSwitches.MODID);

    

    public static void init(){
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        BLOCK_ENTITIES.register(bus);        
    }

    public static final Item.Properties ITEM_PROPERTIES = new Item.Properties().tab(ModSetup.ITEM_GROUP);

    private static <T extends Block> RegistryObject<T> registerChannelBlock(String name, Supplier<T> block){
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerChannelBlockItem(toReturn);
        return toReturn;
    }

    public static BlockItem getBlockItem(Block block){
        if (block instanceof BaseLightBlock || block instanceof BaseChannelBlock){
            return new BaseSwitchBlockItem(block, ITEM_PROPERTIES);
        } else {
            return new UsableSwitchBlockItem(block, ITEM_PROPERTIES);
        }
    }

    public static <B extends Block> RegistryObject<Item> registerChannelBlockItem(RegistryObject<B> block) {
        return ITEMS.register(block.getId().getPath(), () -> getBlockItem(block.get()));
    }

    public static <B extends Block, E extends BlockEntity> RegistryObject<BlockEntityType<E>> registerChannelBlockEntity(RegistryObject<B> block, BlockEntitySupplier<E> be){
        return BLOCK_ENTITIES.register(block.getId().getPath(), () -> BlockEntityType.Builder.of(be, block.get()).build(null));
    }

    // Channel Blocks
    public static final RegistryObject<Block> CHANNEL_OUTPUT_BLOCK = registerChannelBlock("channel_output",() -> new ChannelOutputBlock(BlockBehaviour.Properties.of(Material.STONE)));
    public static final RegistryObject<BlockEntityType<ChannelOutputTile>> CHANNEL_OUTPUT_BLOCK_BE = registerChannelBlockEntity(CHANNEL_OUTPUT_BLOCK, ChannelOutputTile::new);
    
    public static final RegistryObject<Block> CHANNEL_INPUT_BLOCK = registerChannelBlock("channel_input",() -> new ChannelInputBlock(BlockBehaviour.Properties.of(Material.STONE)));
    public static final RegistryObject<BlockEntityType<ChannelInputTile>> CHANNEL_INPUT_BLOCK_BE = registerChannelBlockEntity(CHANNEL_INPUT_BLOCK, ChannelInputTile::new);
    
    // Switches
    public static final RegistryObject<Block> SWITCH_BLOCK = registerChannelBlock("switch_block",() -> new SwitchBlock(BlockBehaviour.Properties.of(Material.STONE).noCollission().noCollission()));
    public static final RegistryObject<BlockEntityType<SwitchBlockTile>> SWITCH_BLOCK_BE = registerChannelBlockEntity(SWITCH_BLOCK, SwitchBlockTile::new);

    public static final RegistryObject<Block> BIG_BUTTON_BLOCK = registerChannelBlock("big_button",() -> new BigButtonBlock(BlockBehaviour.Properties.of(Material.STONE).noCollission().noCollission()));
    public static final RegistryObject<BlockEntityType<BigButtonTile>> BIG_BUTTON_BLOCK_BE = registerChannelBlockEntity(BIG_BUTTON_BLOCK, BigButtonTile::new);

    public static final RegistryObject<Block> ESTOP_BUTTON_BLOCK = registerChannelBlock("estop_button",() -> new EStopButtonBlock(BlockBehaviour.Properties.of(Material.STONE).noCollission().noCollission()));
    public static final RegistryObject<BlockEntityType<EStopButtonTile>> ESTOP_BUTTON_BLOCK_BE = registerChannelBlockEntity(ESTOP_BUTTON_BLOCK, EStopButtonTile::new);

    public static final RegistryObject<Block> IO_SWITCH_BLOCK = registerChannelBlock("io_switch",() -> new IOSwitchBlock(BlockBehaviour.Properties.of(Material.STONE).noCollission().noCollission()));
    public static final RegistryObject<BlockEntityType<IOSwitchTile>> IO_SWITCH_BLOCK_BE = registerChannelBlockEntity(IO_SWITCH_BLOCK, IOSwitchTile::new);

    public static final RegistryObject<Block> VANILLA_SWITCH_BLOCK = registerChannelBlock("vanilla_switch_block",() -> new VanillaSwitchBlock(BlockBehaviour.Properties.of(Material.STONE).noCollission().noCollission()));
    public static final RegistryObject<BlockEntityType<VanillaSwitchBlockTile>> VANILLA_SWITCH_BLOCK_BE = registerChannelBlockEntity(VANILLA_SWITCH_BLOCK, VanillaSwitchBlockTile::new);

    // Buttons
    public static final RegistryObject<Block> STONE_BUTTON_BLOCK = registerChannelBlock("stone_button_block",() -> new StoneButtonBlock(true, BlockBehaviour.Properties.of(Material.STONE).noCollission().noCollission()));
    public static final RegistryObject<BlockEntityType<StoneButtonTile>> STONE_BUTTON_BLOCK_BE = registerChannelBlockEntity(STONE_BUTTON_BLOCK, StoneButtonTile::new);

    // Lights
    public static final RegistryObject<Block> LIGHT_BLOCK = registerChannelBlock("light_block",() -> new BaseLightBlock(BlockBehaviour.Properties.of(Material.STONE).lightLevel(getLightLevel())));
    public static final RegistryObject<BlockEntityType<LightBlockTile>> LIGHT_BLOCK_BE = registerChannelBlockEntity(LIGHT_BLOCK, LightBlockTile::new);

    public static final RegistryObject<Block> LIGHT_PANEL_BLOCK = registerChannelBlock("light_panel",() -> new LightPanelBlock(BlockBehaviour.Properties.of(Material.STONE).lightLevel(getLightLevel())));
    public static final RegistryObject<BlockEntityType<LightPanelTile>> LIGHT_PANEL_BLOCK_BE = registerChannelBlockEntity(LIGHT_PANEL_BLOCK, LightPanelTile::new);


    private static ToIntFunction<BlockState> getLightLevel() {
	    return (state) -> {
	    	return (Boolean)state.getValue(BaseLightBlock.LIT) ? LIGHT_LEVEL : 0;
	    };
	}
}
