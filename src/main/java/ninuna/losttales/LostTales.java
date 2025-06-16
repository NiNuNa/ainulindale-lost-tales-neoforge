package ninuna.losttales;

import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import ninuna.losttales.block.LostTalesBlocks;
import ninuna.losttales.client.gui.screen.LostTalesQuestJournalScreen;
import ninuna.losttales.config.LostTalesConfig;
import ninuna.losttales.item.LostTalesCreativeModeTabs;
import ninuna.losttales.item.LostTalesItems;
import ninuna.losttales.util.LostTalesKeyMappings;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(LostTales.MOD_ID)
public class LostTales {
    public static final String MOD_ID = "losttales";
    private static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public LostTales(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        LostTalesCreativeModeTabs.register(modEventBus);
        LostTalesItems.register(modEventBus);
        LostTalesBlocks.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (LostTales) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, LostTalesConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (LostTalesConfig.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(LostTalesConfig.magicNumberIntroduction + LostTalesConfig.magicNumber);

        LostTalesConfig.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.COMBAT) {
            event.accept(LostTalesItems.TEST_ITEM);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        if (LostTalesKeyMappings.QUEST_JOURNAL_MAPPING.get().consumeClick()) {
            // Open and display quest journal screen
            LOGGER.info(MOD_ID + ": " + Minecraft.getInstance().getUser().getName() + " opened the quest journal");
            Minecraft.getInstance().setScreen(new LostTalesQuestJournalScreen(Minecraft.getInstance().screen));
        }
        else if (LostTalesKeyMappings.TOGGLE_HUD_MAPPING.get().consumeClick()) {
            // Toggle lost tales hud
            LOGGER.info("LOL!");
            //Todo: Pop Up "Hud: Enabled/Disabled"
            //Todo: Toggle Hud
        }
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}