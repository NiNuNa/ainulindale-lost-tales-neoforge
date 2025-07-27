package ninuna.losttales.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import ninuna.losttales.LostTales;
import ninuna.losttales.network.packet.LostTalesQuickLootHudDropItemClientPacket;
import ninuna.losttales.network.packet.LostTalesQuickLootHudDropItemPacket;

@EventBusSubscriber(modid = LostTales.MOD_ID)
public class LostTalesNetworkHandler {

    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        event.registrar(LostTales.MOD_ID).playToServer(
                LostTalesQuickLootHudDropItemPacket.TYPE,
                LostTalesQuickLootHudDropItemPacket.STREAM_CODEC,
                LostTalesQuickLootHudDropItemPacket::handle
        );

        event.registrar(LostTales.MOD_ID).playToClient(
                LostTalesQuickLootHudDropItemClientPacket.TYPE,
                LostTalesQuickLootHudDropItemClientPacket.STREAM_CODEC,
                LostTalesQuickLootHudDropItemClientPacket::handle
        );
    }
}
