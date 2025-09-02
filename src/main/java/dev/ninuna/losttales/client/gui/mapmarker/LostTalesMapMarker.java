package dev.ninuna.losttales.client.gui.mapmarker;

import dev.ninuna.losttales.common.LostTales;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.UUID;

public record LostTalesMapMarker(
        UUID id, Component name, int color, ResourceKey<Level> dimension,
        boolean scaleWithCenterFocus, boolean showDistanceLabel, boolean hasActiveQuest,
        float fadeOutRadiusDistance,
        Double x, Double z, Double y,
        int textureU, int textureV
) {
    private static final ResourceLocation MAP_MARKER_TEXTURE = ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "textures/gui/mapmarkers.png");

    private static final int MAP_MARKER_TEXTURE_WIDTH  = 193;
    private static final int MAP_MARKER_TEXTURE_HEIGHT = 64;

    private static final int MAP_MARKER_WIDTH = 11;
    private static final int MAP_MARKER_HEIGHT = 11;

    public double getYDelta(Minecraft minecraft) {
        if (y != null) return y - minecraft.player.getY();
        return 0.0;
    }

    public static Component getMapMarkerName(String key) {
        return Component.translatable("mapMarker." + LostTales.MOD_ID + "." + key);
    }

    public static UUID getMapMarkerUUID() {
        return UUID.fromString("Lol");
    }

    public static ResourceLocation getMapMarkerTexture() {
        return MAP_MARKER_TEXTURE;
    }

    public static int getMapMarkerHeight() {
        return MAP_MARKER_HEIGHT;
    }

    public static int getMapMarkerWidth() {
        return MAP_MARKER_WIDTH;
    }

    public static int getMapMarkerTextureHeight() {
        return MAP_MARKER_TEXTURE_HEIGHT;
    }

    public static int getMapMarkerTextureWidth() {
        return MAP_MARKER_TEXTURE_WIDTH;
    }
}
