package dev.ninuna.losttales.client.gui.hud.compass;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class LostTalesCompassHudRenderHelper {

    public static float normalizeDegrees(float deg) {
        float r = deg % 360f;
        return r < 0f ? r + 360f : r;
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

    // ---------- Drawing helpers (fractional positioning safe) ----------
    public static void drawStringCentered(GuiGraphics gfx, Minecraft mc, Component text, float x, float y, int argb, boolean shadow) {
        if (text == null) return;
        float left = x - mc.font.width(text) / 2f;
        int xi = Mth.floor(left);
        int yi = Mth.floor(y);
        float xFrac = left - xi;
        float yFrac = y - yi;

        var pose = gfx.pose();
        pose.pushMatrix();
        pose.translate(xFrac, yFrac);
        gfx.drawString(mc.font, text, xi, yi, argb, shadow);
        pose.popMatrix();
    }

    public static void drawBlit(GuiGraphics gfx,
                                net.minecraft.resources.ResourceLocation texture,
                                float x, float y,
                                int u, int v, int w, int h,
                                int texW, int texH,
                                int argb) {
        int xi = Mth.floor(x);
        int yi = Mth.floor(y);
        float xFrac = x - xi;
        float yFrac = y - yi;

        var pose = gfx.pose();
        pose.pushMatrix();
        pose.translate(xFrac, yFrac);
        gfx.blit(
                net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED,
                texture, xi, yi, u, v, w, h, texW, texH, argb
        );
        pose.popMatrix();
    }
}
