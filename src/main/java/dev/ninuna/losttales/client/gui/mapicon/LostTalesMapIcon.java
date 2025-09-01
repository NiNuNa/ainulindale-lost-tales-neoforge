package dev.ninuna.losttales.client.gui.mapicon;

import net.minecraft.client.Minecraft;

public record LostTalesMapIcon(
        String name,
        int color,
        boolean scaleWithCenterFocus,
        boolean showDistanceLabel,
        boolean hasActiveQuest,
        float fadeOutRadiusDistance,
        Double worldX, Double worldZ, Double worldY,
        Double relativeX, Double relativeZ, Double relativeY,
        int textureU, int textureV
) {
    public static LostTalesMapIcon mapIconWithPreDefinedAngle(String name, int color, int textureU, int textureV, float angle) {
        double rad = Math.toRadians(angle);
        double dx = -Math.sin(rad);
        double dz =  Math.cos(rad);
        return new LostTalesMapIcon(name, color, false, false, false, 0f, null, null, null, dx, dz, null, textureU, textureV);
    }

    public boolean hasRelativePosition() {
        return relativeX != null && relativeZ != null;
    }

    public boolean hasVertical() {
        return relativeY != null || worldY != null;
    }

    public double verticalDelta(Minecraft minecraft) {
        if (relativeY != null) return relativeY;
        if (worldY != null) return worldY - minecraft.player.getY();
        return 0.0;
    }
}
