package dev.ninuna.losttales.client.gui.mapmarker.provider.custom;

import dev.ninuna.losttales.client.gui.mapmarker.LostTalesPositionMapMarker;
import dev.ninuna.losttales.client.gui.mapmarker.provider.LostTalesMapMarkerProvider;
import dev.ninuna.losttales.common.mapmarker.LostTalesMapMarkerDataStore;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class LostTalesSharedMapMarkerProvider implements LostTalesMapMarkerProvider {

    @Override
    public List<LostTalesPositionMapMarker> collectMapMarkers(Minecraft minecraft) {
        if (minecraft == null || minecraft.level == null || minecraft.player == null) return List.of();

        var player = minecraft.player;
        Level level = player.level();
        var mapMarkers = new ArrayList<LostTalesPositionMapMarker>();

        for (var mapMarker : LostTalesMapMarkerDataStore.shared()) {
            if (!mapMarker.dimension.equals(level.dimension())) continue;

            mapMarkers.add(new LostTalesPositionMapMarker(
                    mapMarker.id,
                    Component.literal(mapMarker.name),
                    mapMarker.icon,
                    mapMarker.color,
                    mapMarker.dimension,
                    true,
                    true,
                    false,
                    mapMarker.fadeInRadius,
                    mapMarker.unlockRadius,
                    mapMarker.x,
                    mapMarker.y,
                    mapMarker.z
            ));
        }
        return mapMarkers;
    }
}
