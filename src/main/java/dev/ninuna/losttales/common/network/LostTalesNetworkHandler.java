package dev.ninuna.losttales.common.network;

import dev.ninuna.losttales.common.network.packet.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import dev.ninuna.losttales.common.LostTales;

@EventBusSubscriber(modid = LostTales.MOD_ID)
public class LostTalesNetworkHandler {

    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        event.registrar(LostTales.MOD_ID).playToServer(
                LostTalesQuickLootHudDropItemPacket.TYPE, LostTalesQuickLootHudDropItemPacket.STREAM_CODEC, LostTalesQuickLootHudDropItemPacket::handle
        );

        event.registrar(LostTales.MOD_ID).playToClient(
                LostTalesQuickLootHudDropItemClientPacket.TYPE, LostTalesQuickLootHudDropItemClientPacket.STREAM_CODEC, LostTalesQuickLootHudDropItemClientPacket::handle
        );

        event.registrar(LostTales.MOD_ID).playToClient(
                LostTalesSyncMapMarkersPacket.TYPE, LostTalesSyncMapMarkersPacket.STREAM_CODEC, LostTalesSyncMapMarkersPacket::handle
        );

        event.registrar(LostTales.MOD_ID).playToClient(
                LostTalesSyncMobAggroPacket.TYPE, LostTalesSyncMobAggroPacket.STREAM_CODEC, LostTalesSyncMobAggroPacket::handle
        );

        event.registrar(LostTales.MOD_ID).playToClient(
                LostTalesSyncQuestsPacket.TYPE, LostTalesSyncQuestsPacket.STREAM_CODEC, LostTalesSyncQuestsPacket::handle
        );
    }
}
