package dev.ninuna.losttales.client.gui.mapmarker;

import dev.ninuna.losttales.client.gui.LostTalesGuiColor;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.mapmarker.LostTalesMapMarkerData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.*;

public record LostTalesMapMarker(
        UUID id, Component name, LostTalesMapMarkerIcon icon, LostTalesGuiColor color,
        ResourceKey<Level> dimension,
        boolean scaleWithCenterFocus, boolean showDistanceLabel, boolean hasActiveQuest,
        double fadeOutRadiusDistance,
        Double x, Double z, Double y
) {
    private static final List<LostTalesMapMarkerData.Entry> SHARED = new ArrayList<>();
    private static final List<LostTalesMapMarkerData.Entry> PERSONAL = new ArrayList<>();

    public double getDeltaY(Minecraft minecraft) {
        if (y != null) return y - minecraft.player.getY();
        return 0.0;
    }

    public static Component getMapMarkerName(String key) {
        return Component.translatable("mapMarker." + LostTales.MOD_ID + "." + key);
    }

    public static synchronized void setShared(Collection<LostTalesMapMarkerData.Entry> entries) {
        SHARED.clear();
        SHARED.addAll(entries);
    }

    public static synchronized void setPersonal(Collection<LostTalesMapMarkerData.Entry> entries) {
        PERSONAL.clear();
        PERSONAL.addAll(entries);
    }

    public static synchronized List<LostTalesMapMarkerData.Entry> shared() {
        return Collections.unmodifiableList(SHARED);
    }

    public static synchronized List<LostTalesMapMarkerData.Entry> personal() {
        return Collections.unmodifiableList(PERSONAL);
    }

    public static synchronized void clearAll() {
        SHARED.clear();
        PERSONAL.clear();
    }
}
