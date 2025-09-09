package dev.ninuna.losttales.client.gui.mapmarker.provider;

import net.minecraft.client.Minecraft;
import dev.ninuna.losttales.client.gui.mapmarker.custom.LostTalesPositionMapMarker;

import java.util.List;

public interface LostTalesMapMarkerProvider {
    List<LostTalesPositionMapMarker> collectMapMarkers(Minecraft minecraft);
}
