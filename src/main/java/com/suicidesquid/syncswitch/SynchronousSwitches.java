package com.suicidesquid.syncswitch;

import com.mojang.logging.LogUtils;
import com.suicidesquid.syncswitch.setup.ClientSetup;
import com.suicidesquid.syncswitch.setup.ModSetup;
import com.suicidesquid.syncswitch.setup.Registration;

import net.minecraft.client.Minecraft;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(SynchronousSwitches.MODID)
public class SynchronousSwitches {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "syncswitch";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under
    // the "examplemod" namespace
    // public static final DeferredRegister<Block> BLOCKS =
    // DeferredRegister.create(BuiltInRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under
    // the "examplemod" namespace
    // public static final DeferredRegister<Item> ITEMS =
    // DeferredRegister.create(BuiltInRegistries.ITEMS, MODID);

    // Creates a new Block with the id "examplemod:example_block", combining the
    // namespace and path
    // public static final RegistryObject<Block> EXAMPLE_BLOCK =
    // BLOCKS.register("example_block", () -> new
    // Block(BlockBehaviour.Properties.of(Material.STONE)));
    // Creates a new BlockItem with the id "examplemod:example_block", combining the
    // namespace and path
    // public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM =
    // ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new
    // Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

    public SynchronousSwitches(IEventBus modEventBus, Dist dist) {
        Registration.init(modEventBus);

        // Register 'ModSetup::init' to be called at mod setup time (server and client)
        modEventBus.addListener(ModSetup::init);
        // Register 'ClientSetup::init' to be called at mod setup time (client only)
        if (dist.isClient()) {
            modEventBus.addListener(ClientSetup::init);
        }
        modEventBus.addListener(this::addCreative);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS) {
            for (DeferredHolder<Item, ? extends Item> regItem : Registration.ITEMS.getEntries()) {
                event.accept(regItem.get());
            }
        }
    }

    // // You can use EventBusSubscriber to automatically register all static methods
    // // in the class annotated with @SubscribeEvent
    // @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    // public static class ClientModEvents {
    //     @SubscribeEvent
    //     public static void onClientSetup(FMLClientSetupEvent event) {
    //         // Some client setup code
    //         LOGGER.info("HELLO FROM CLIENT SETUP");
    //         LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    //     }
    // }
}
