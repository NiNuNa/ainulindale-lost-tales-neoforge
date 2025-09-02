package dev.ninuna.losttales.client.gui.mapmarker.provider.custom;

import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarker;
import dev.ninuna.losttales.client.gui.mapmarker.provider.LostTalesMapMarkerProvider;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesClientMapMarker;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class LostTalesSharedMapMarkerProvider implements LostTalesMapMarkerProvider {

    @Override
    public List<LostTalesMapMarker> collectMapMarkers(Minecraft minecraft) {
        var player = minecraft.player;
        if (player == null) return List.of();

        Level level = player.level();
        var mapMarkers = new ArrayList<LostTalesMapMarker>();

        for (var mapMarker : LostTalesClientMapMarker.shared()) {
            if (!mapMarker.dim.equals(level.dimension())) continue;

            mapMarkers.add(new LostTalesMapMarker(
                    mapMarker.id, Component.literal(mapMarker.name), mapMarker.color, mapMarker.dim,
                    true, true, false,
                    0.0f,
                    mapMarker.x, mapMarker.z, mapMarker.y,
                    22, 0
            ));
        }
        return mapMarkers;
    }
}
