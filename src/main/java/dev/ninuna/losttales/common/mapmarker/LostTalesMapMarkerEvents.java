package dev.ninuna.losttales.common.mapmarker;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.data.attachment.LostTalesAttachments;
import dev.ninuna.losttales.common.network.packet.LostTalesSyncMapMarkersPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

@EventBusSubscriber(modid = LostTales.MOD_ID)
public class LostTalesMapMarkerEvents {

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        var player = event.getEntity();
        syncMapMarkerDataToPlayer(player);
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        var player = event.getEntity();
        syncMapMarkerDataToPlayer(player);
    }

    @SubscribeEvent
    public static void onLevelLoad(LevelEvent.Load event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        seedLevelFromCache(level);
    }

    @SubscribeEvent
    public static void onDatapackSync(OnDatapackSyncEvent event) {
        MinecraftServer server = event.getPlayer() != null
                ? event.getPlayer().getServer()
                : event.getPlayerList().getServer();

        for (ServerLevel level : server.getAllLevels()) {
            seedLevelFromCache(level);
        }

        // Optionally push updates to all or to the reloading player:
        if (event.getPlayer() != null) {
            LostTalesMapMarkerEvents.syncMapMarkerDataToPlayer((event.getPlayer()));
        } else {
            server.getPlayerList().getPlayers().forEach(LostTalesMapMarkerEvents::syncMapMarkerDataToPlayer);
        }
    }

    private static void syncMapMarkerDataToPlayer(Player player) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        var data = serverPlayer.level().getData(LostTalesAttachments.LEVEL_MARKERS.get());
        PacketDistributor.sendToPlayer(serverPlayer, new LostTalesSyncMapMarkersPacket(true, data.all()));
    }

    private static void seedLevelFromCache(ServerLevel level) {
        var dimensionKey = level.dimension().location();
        var list = LostTalesMapMarkerDataReloadListener.BY_DIMENSION.getOrDefault(dimensionKey, List.of());
        var levelData = level.getData(LostTalesAttachments.LEVEL_MARKERS.get());

        if (levelData.all().isEmpty()) {
            levelData.addAll(list);
            LostTales.LOGGER.debug("[" + LostTales.MOD_ID + "] Seeded {} map markers into {}", list.size(), dimensionKey);
        } else {
            LostTales.LOGGER.debug("[" + LostTales.MOD_ID + "] Skipped map marker seeding; {} already has {} markers", dimensionKey, levelData.all().size());
        }
    }
}
