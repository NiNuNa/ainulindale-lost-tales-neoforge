package ninuna.losttales;

import ninuna.losttales.block.LostTalesBlocks;
import ninuna.losttales.block.entity.LostTalesBlockEntities;
import ninuna.losttales.config.LostTalesConfigs;
import ninuna.losttales.item.LostTalesCreativeModeTabs;
import ninuna.losttales.item.LostTalesItems;
import ninuna.losttales.sound.LostTalesSoundEvents;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(LostTales.MOD_ID)
public class LostTales {
    public static final String MOD_ID = "losttales";
    public static final Logger LOGGER = LogUtils.getLogger();

    public LostTales(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        LostTalesSoundEvents.register(modEventBus);
        LostTalesBlocks.register(modEventBus);
        LostTalesBlockEntities.register(modEventBus);
        LostTalesItems.register(modEventBus);
        LostTalesCreativeModeTabs.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (LostTales) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.CLIENT, LostTalesConfigs.CLIENT_SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, LostTalesConfigs.COMMON_SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, LostTalesConfigs.SERVER_SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}