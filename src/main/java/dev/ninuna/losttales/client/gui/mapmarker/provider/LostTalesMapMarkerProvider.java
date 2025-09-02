package dev.ninuna.losttales.client.gui.mapmarker.provider;

import net.minecraft.client.Minecraft;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarker;

import java.util.List;

public interface LostTalesMapMarkerProvider {
    List<LostTalesMapMarker> collectMapMarkers(Minecraft minecraft);
}
