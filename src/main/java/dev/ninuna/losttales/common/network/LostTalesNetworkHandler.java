package dev.ninuna.losttales.common.network;

import dev.ninuna.losttales.common.network.packet.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import dev.ninuna.losttales.common.LostTales;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = LostTales.MOD_ID)
public class LostTalesNetworkHandler {
    public static final String VERSION = "1";

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar payloadRegistrar = event.registrar(LostTales.MOD_ID).versioned(VERSION);

        // Quick loot hud network packets.
        payloadRegistrar.playToServer(LostTalesQuickLootHudDropItemPacket.TYPE, LostTalesQuickLootHudDropItemPacket.STREAM_CODEC, LostTalesQuickLootHudDropItemPacket::handle);
        payloadRegistrar.playToClient(LostTalesQuickLootHudDropItemClientPacket.TYPE, LostTalesQuickLootHudDropItemClientPacket.STREAM_CODEC, LostTalesQuickLootHudDropItemClientPacket::handle);
        payloadRegistrar.playToServer(LostTalesQuickLootRequestPacket.TYPE, LostTalesQuickLootRequestPacket.STREAM_CODEC, LostTalesQuickLootRequestPacket::handle);
        payloadRegistrar.playToClient(LostTalesSyncQuickLootHudContainerPacket.TYPE, LostTalesSyncQuickLootHudContainerPacket.STREAM_CODEC, LostTalesSyncQuickLootHudContainerPacket::handle);

        // Map marker network packets.
        payloadRegistrar.playToClient(LostTalesSyncMapMarkersPacket.TYPE, LostTalesSyncMapMarkersPacket.STREAM_CODEC, LostTalesSyncMapMarkersPacket::handle);
        payloadRegistrar.playToClient(LostTalesSyncMobAggroPacket.TYPE, LostTalesSyncMobAggroPacket.STREAM_CODEC, LostTalesSyncMobAggroPacket::handle);

        // Quest network packets.
        payloadRegistrar.playToClient(LostTalesSyncQuestsPacket.TYPE, LostTalesSyncQuestsPacket.STREAM_CODEC, LostTalesSyncQuestsPacket::handle);
    }
}
