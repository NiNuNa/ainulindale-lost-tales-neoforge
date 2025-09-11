package dev.ninuna.losttales.client.gui.mapmarker;

import com.mojang.serialization.Codec;
import dev.ninuna.losttales.common.LostTales;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum LostTalesMapMarkerIcon implements StringRepresentable {
    N               (0, 19),
    NE              (19, 19),
    E               (38, 19),
    SE              (57, 19),
    S               (76, 19),
    SW              (95, 19),
    W               (114, 19),
    NW              (133, 19),
    QUEST           (0, 0),
    HOSTILE         (19, 0),
    FORT            (0, 38),
    UNDISCOVERED    (38, 0),
    TAVERN          (19, 38);

    public static final Codec<LostTalesMapMarkerIcon> CODEC = StringRepresentable.fromEnum(LostTalesMapMarkerIcon::values);

    public static final ResourceLocation MAP_MARKER_ICON_TEXTURE = LostTales.getResourceLocation("textures/gui/mapmarkericons.png");

    public static final int MAP_MARKER_ICON_TEXTURE_WIDTH  = 207;
    public static final int MAP_MARKER_ICON_TEXTURE_HEIGHT = 64;

    public static final int MAP_MARKER_ICON_WIDTH = 17;
    public static final int MAP_MARKER_ICON_HEIGHT = 17;

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
