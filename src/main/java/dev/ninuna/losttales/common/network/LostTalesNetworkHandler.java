package dev.ninuna.losttales.common.network;

import dev.ninuna.losttales.common.network.packet.LostTalesLockedOnTargetPacket;
import dev.ninuna.losttales.common.network.packet.LostTalesSyncMapMarkersPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.network.packet.LostTalesQuickLootHudDropItemClientPacket;
import dev.ninuna.losttales.common.network.packet.LostTalesQuickLootHudDropItemPacket;

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
                LostTalesLockedOnTargetPacket.TYPE, LostTalesLockedOnTargetPacket.STREAM_CODEC, LostTalesLockedOnTargetPacket::handle
        );
    }
}
