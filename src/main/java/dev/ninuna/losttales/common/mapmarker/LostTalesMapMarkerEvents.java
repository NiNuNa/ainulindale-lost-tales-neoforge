package dev.ninuna.losttales.common.mapmarker;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.attachement.LostTalesAttachments;
import dev.ninuna.losttales.common.network.packet.LostTalesSyncMapMarkersPacket;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;

@EventBusSubscriber(modid = LostTales.MOD_ID)
public class LostTalesMapMarkerEvents {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
        syncMapMarkerData(serverPlayer);
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
        LostTales.LOGGER.info("CHANGED DIM");
        syncMapMarkerData(serverPlayer);
    }

    private static void syncMapMarkerData(ServerPlayer serverPlayer) {
        var data = serverPlayer.serverLevel().getData(LostTalesAttachments.LEVEL_MARKERS.get());
        PacketDistributor.sendToPlayer(serverPlayer, new LostTalesSyncMapMarkersPacket(true, data.all()));
    }
}
