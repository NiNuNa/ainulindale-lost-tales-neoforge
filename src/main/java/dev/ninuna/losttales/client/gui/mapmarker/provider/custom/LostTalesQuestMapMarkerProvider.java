package dev.ninuna.losttales.client.gui.mapmarker.provider.custom;

import dev.ninuna.losttales.client.gui.mapmarker.provider.LostTalesMapMarkerProvider;
import net.minecraft.client.Minecraft;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarker;

import java.util.List;

public class LostTalesQuestMapMarkerProvider implements LostTalesMapMarkerProvider {

    @Override
    public List<LostTalesMapMarker> collectMapMarkers(Minecraft minecraft) {
        return List.of();
    }
}
