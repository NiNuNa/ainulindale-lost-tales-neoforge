package dev.ninuna.losttales.client.gui.mapmarker;

import com.mojang.serialization.Codec;
import dev.ninuna.losttales.common.LostTales;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum LostTalesMapMarkerIcon implements StringRepresentable {
    N           (0, 0),
    NE          (0, 0),
    E           (0, 0),
    SE          (0, 0),
    S           (0, 0),
    SW          (0, 0),
    W           (0, 0),
    NW          (0, 0),
    HOSTILE     (11, 0),
    FORT        (44, 0),
    TAVERN      (0, 0);


    public static final Codec<LostTalesMapMarkerIcon> CODEC = StringRepresentable.fromEnum(LostTalesMapMarkerIcon::values);

    public static final ResourceLocation MAP_MARKER_ICON_TEXTURE = ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "textures/gui/mapmarkericons.png");

    public static final int MAP_MARKER_ICON_TEXTURE_WIDTH  = 193;
    public static final int MAP_MARKER_ICON_TEXTURE_HEIGHT = 64;

    public static final int MAP_MARKER_ICON_WIDTH = 11;
    public static final int MAP_MARKER_ICON_HEIGHT = 11;

    private final int u;
    private final int v;

    LostTalesMapMarkerIcon(int u, int v) {
        this.u = u;
        this.v = v;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
