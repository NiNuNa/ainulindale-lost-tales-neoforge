package dev.ninuna.losttales.client.gui.hud.compass;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class LostTalesCompassHudRenderHelper {

    public static float normalizeViewYaw(float viewYaw) {
        return (viewYaw % 360f + 360f) % 360f;
    }

    public static float shortestDeltaDegrees(float targetDeg, float refDeg) {
        float d = (targetDeg - refDeg + 540f) % 360f - 180f;
        return d;
    }

    public static float edgeCenterFactor(float px, float centerX, float halfWidth, float fadeEdgePx) {
        float dist = Math.abs(px - centerX);
        float fadeStart = Math.max(0f, halfWidth - fadeEdgePx);
        if (dist <= fadeStart) return 1f;
        if (dist >= halfWidth) return 0f;
        float t = (halfWidth - dist) / (halfWidth - fadeStart);
        // cosine smoothstep
        return 0.5f - 0.5f * (float) Math.cos(Math.PI * t);
    }

    public static float focusEmphasis(float centerDistPx, float focusRadiusPx) {
        float t = 1f - Math.min(1f, centerDistPx / focusRadiusPx);
        // Hermite smoothstep
        return t * t * (3f - 2f * t);
    }

    // atan2(-dx, dz) → [0,360)
    public static float angleDegToTarget(double dx, double dz) {
        float deg = (float) Math.toDegrees(Math.atan2(-dx, dz));
        return deg < 0f ? deg + 360f : deg;
    }

    // ---------- Player position (interpolated) ----------
    public record PlayerPos(double x, double y, double z) {}

    public static PlayerPos lerpPlayerPos(Player p, float partialTick) {
        double x = Mth.lerp(partialTick, p.xo, p.getX());
        double y = Mth.lerp(partialTick, p.yo, p.getY());
        double z = Mth.lerp(partialTick, p.zo, p.getZ());
        return new PlayerPos(x, y, z);
    }

    public static void drawCenteredString(GuiGraphics guiGraphics, Minecraft minecraft, Component text, float x, float y, int color, boolean drawSadow) {
        if (text == null) return;
        float left = x - minecraft.font.width(text) / 2f;
        int xi = Mth.floor(left);
        int yi = Mth.floor(y);
        float xFrac = left - xi;
        float yFrac = y - yi;

        var pose = guiGraphics.pose();
        pose.pushMatrix();
        pose.translate(xFrac, yFrac);
        guiGraphics.drawString(minecraft.font, text, xi, yi, color, drawSadow);
        pose.popMatrix();
    }

    public static void drawBlit(GuiGraphics guiGraphics, ResourceLocation texture, float x, float y, int u, int v, int width, int height, int textureWidth, int textureHeight, int color) {
        int xi = Mth.floor(x);
        int yi = Mth.floor(y);
        float xFrac = x - xi;
        float yFrac = y - yi;

        var pose = guiGraphics.pose();
        pose.pushMatrix();
        pose.translate(xFrac, yFrac);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, xi, yi, u, v, width, height, textureWidth, textureHeight, color);
        pose.popMatrix();
    }

    public static void drawBlitWithShadow(GuiGraphics guiGraphics, ResourceLocation texture, float x, float y, int u, int v, int width, int height, int textureWidth, int textureHeight, int color, int shadowColor) {
        // 1px offset shadow (bottom-right)
        drawBlit(guiGraphics, texture, x + 1, y + 1, u, v, width, height, textureWidth, textureHeight, shadowColor);
        // main icon
        drawBlit(guiGraphics, texture, x, y, u, v, width, height, textureWidth, textureHeight, color);
    }
}
