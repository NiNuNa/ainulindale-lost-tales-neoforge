package dev.ninuna.losttales.client.gui.mapmarker;

import dev.ninuna.losttales.client.gui.LostTalesGuiColor;
import dev.ninuna.losttales.common.LostTales;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.*;

public class LostTalesPositionMapMarker {
    private final UUID uuid;
    private final Component name;
    private final LostTalesMapMarkerIcon icon;
    private final LostTalesGuiColor color;
    private final ResourceKey<Level> dimension;

    private final boolean scaleWithCenterFocus;
    private final boolean showDistanceLabel;
    private final boolean hasActiveQuest;

    private final double fadeInRadius;
    private final double unlockRadius;
    private final double x;
    private final double y;
    private final double z;

    public LostTalesPositionMapMarker(UUID uuid, Component name, LostTalesMapMarkerIcon icon, LostTalesGuiColor color, ResourceKey<Level> dimension,
                                      boolean scaleWithCenterFocus, boolean showDistanceLabel, boolean hasActiveQuest, double fadeInRadius, double unlockRadius, double x, double y, double z) {
        this.uuid = uuid;
        this.name = name;
        this.icon = icon;
        this.color = color;
        this.dimension = dimension;
        this.scaleWithCenterFocus = scaleWithCenterFocus;
        this.showDistanceLabel = showDistanceLabel;
        this.hasActiveQuest = hasActiveQuest;
        this.fadeInRadius = fadeInRadius;
        this.unlockRadius = unlockRadius;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public LostTalesPositionMapMarker(UUID uuid, String name, LostTalesMapMarkerIcon icon, LostTalesGuiColor color, ResourceKey<Level> dimension,
                                      boolean scaleWithCenterFocus, boolean showDistanceLabel, boolean hasActiveQuest, double fadeInRadius, double unlockRadius, double x, double y, double z) {
        this.uuid = uuid;
        this.name = Component.translatable("mapMarker." + LostTales.MOD_ID + "." + name);
        this.icon = icon;
        this.color = color;
        this.dimension = dimension;
        this.scaleWithCenterFocus = scaleWithCenterFocus;
        this.showDistanceLabel = showDistanceLabel;
        this.hasActiveQuest = hasActiveQuest;
        this.fadeInRadius = fadeInRadius;
        this.unlockRadius = unlockRadius;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Component getName() {
        return name;
    }

    public LostTalesMapMarkerIcon getIcon() {
        return icon;
    }

    public LostTalesGuiColor getColor() {
        return color;
    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    public boolean isScaleWithCenterFocus() {
        return scaleWithCenterFocus;
    }

    public boolean isShowDistanceLabel() {
        return showDistanceLabel;
    }

    public boolean isHasActiveQuest() {
        return hasActiveQuest;
    }

    public double getFadeInRadius() {
        return fadeInRadius;
    }

    public double getUnlockRadius() {
        return unlockRadius;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
}
