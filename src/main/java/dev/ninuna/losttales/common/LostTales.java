package dev.ninuna.losttales.common;

import com.mojang.brigadier.CommandDispatcher;
import dev.ninuna.losttales.common.attachment.LostTalesAttachments;
import dev.ninuna.losttales.common.block.LostTalesBlocks;
import dev.ninuna.losttales.common.block.entity.LostTalesBlockEntities;
import dev.ninuna.losttales.common.command.LostTalesMapMarkerCommand;
import dev.ninuna.losttales.common.command.LostTalesQuestCommand;
import dev.ninuna.losttales.common.config.LostTalesConfigs;
import dev.ninuna.losttales.common.item.LostTalesCreativeModeTabs;
import dev.ninuna.losttales.common.item.LostTalesItems;
import dev.ninuna.losttales.common.quest.objective.handler.LostTalesQuestObjectiveHandlers;
import dev.ninuna.losttales.common.quest.objective.handler.custom.LostTalesCraftQuestObjectiveHandler;
import dev.ninuna.losttales.common.quest.objective.handler.custom.LostTalesGatherQuestObjectiveHandler;
import dev.ninuna.losttales.common.quest.objective.handler.custom.LostTalesGotoQuestObjectiveHandler;
import dev.ninuna.losttales.common.quest.objective.handler.custom.LostTalesKillQuestObjectiveHandler;
import dev.ninuna.losttales.common.sound.LostTalesSoundEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
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
        // Register quest objective handlers.
        LostTalesQuestObjectiveHandlers.register(new LostTalesKillQuestObjectiveHandler());
        LostTalesQuestObjectiveHandlers.register(new LostTalesGotoQuestObjectiveHandler());
        LostTalesQuestObjectiveHandlers.register(new LostTalesCraftQuestObjectiveHandler());
        LostTalesQuestObjectiveHandlers.register(new LostTalesGatherQuestObjectiveHandler());
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        // Register commands.
        CommandDispatcher<CommandSourceStack> commandDispatcher = event.getDispatcher();
        new LostTalesMapMarkerCommand(commandDispatcher);
        new LostTalesQuestCommand(commandDispatcher);
    }

    public static ResourceLocation getResourceLocation(String key) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, key);
    }
}
