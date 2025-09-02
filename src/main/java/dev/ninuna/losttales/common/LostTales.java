package dev.ninuna.losttales.common;

import com.mojang.brigadier.CommandDispatcher;
import dev.ninuna.losttales.common.attachement.LostTalesAttachments;
import dev.ninuna.losttales.common.block.LostTalesBlocks;
import dev.ninuna.losttales.common.block.entity.LostTalesBlockEntities;
import dev.ninuna.losttales.common.command.LostTalesMapMarkerCommand;
import dev.ninuna.losttales.common.config.LostTalesConfigs;
import dev.ninuna.losttales.common.item.LostTalesCreativeModeTabs;
import dev.ninuna.losttales.common.item.LostTalesItems;
import dev.ninuna.losttales.common.sound.LostTalesSoundEvents;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

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
        LostTalesAttachments.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (LostTales) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        modContainer.registerConfig(ModConfig.Type.CLIENT, LostTalesConfigs.CLIENT_SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, LostTalesConfigs.COMMON_SPEC);
        modContainer.registerConfig(ModConfig.Type.SERVER, LostTalesConfigs.SERVER_SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        new LostTalesMapMarkerCommand(commandDispatcher);
    }
}
