package dev.ninuna.losttales.client.gui.mapmarker;

import dev.ninuna.losttales.client.gui.LostTalesGuiColor;
import dev.ninuna.losttales.common.LostTales;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.*;

public record LostTalesMapMarker(
        UUID id, Component name, LostTalesMapMarkerIcon icon, LostTalesGuiColor color,
        ResourceKey<Level> dimension,
        boolean scaleWithCenterFocus, boolean showDistanceLabel, boolean hasActiveQuest,
        double fadeInRadius, double unlockRadius,
        Double x, Double z, Double y
) {
    public double getDeltaY(Minecraft minecraft) {
        if (y != null) return y - minecraft.player.getY();
        return 0.0;
    }

    public static Component getMapMarkerName(String key) {
        return Component.translatable("mapMarker." + LostTales.MOD_ID + "." + key);
    }
}
