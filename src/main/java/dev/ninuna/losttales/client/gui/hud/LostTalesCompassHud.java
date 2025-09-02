package dev.ninuna.losttales.client.gui.hud;

import com.mojang.blaze3d.platform.Window;
import dev.ninuna.losttales.client.gui.mapmarker.provider.custom.LostTalesSharedMapMarkerProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import dev.ninuna.losttales.client.gui.LostTalesGuiHelper;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarker;
import dev.ninuna.losttales.client.gui.mapmarker.provider.LostTalesMapMarkerProvider;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.client.gui.mapmarker.provider.custom.LostTalesDirectionMapMarkerProvider;
import dev.ninuna.losttales.client.gui.mapmarker.provider.custom.LostTalesHostileMapMarkerProvider;
import dev.ninuna.losttales.client.gui.mapmarker.provider.custom.LostTalesVillageMapMarkerProvider;
import dev.ninuna.losttales.common.config.LostTalesConfigs;

import java.util.ArrayList;
import java.util.List;

public class LostTalesCompassHud {

    private static final ResourceLocation COMPASS_HUD_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "textures/gui/compasshud.png");

    private static final int TEXTURE_COMPASS_HUD_WIDTH  = 193;
    private static final int TEXTURE_COMPASS_HUD_HEIGHT = 64;

    private static final int COMPASS_WIDTH  = TEXTURE_COMPASS_HUD_WIDTH;
    private static final int COMPASS_HEIGHT = 18;
    private static final int COMPASS_OFFSET_Y = 4;

    private static final int MAP_MARKER_OFFSET_Y = 5;
    private static final int MAP_MARKER_NAME_OFFSET_Y = 3;
    private static final float MAP_MARKER_SCALE_MODIFIER = 0.20f;

    private static final int VERTICAL_INDICATOR_OFFSET_X = 2;
    private static final int START_EDGE_FADE_OFFSET = COMPASS_WIDTH / 4;
    private static final int START_CENTER_FOCUS_OFFSET = 13;

    private static final List<LostTalesMapMarkerProvider> PROVIDERS = List.of(
            new LostTalesDirectionMapMarkerProvider(),
            new LostTalesVillageMapMarkerProvider(),
            new LostTalesSharedMapMarkerProvider(),
            new LostTalesHostileMapMarkerProvider()
    );

    public static void renderHud(Minecraft minecraft, GuiGraphics guiGraphics) {
        Window window = minecraft.getWindow();
        int windowWidth  = window.getGuiScaledWidth();
        int windowHeight = window.getGuiScaledHeight();

        int customOffsetY = LostTalesConfigs.CLIENT.compassHudOffsetY.get();
        int customOffsetX = LostTalesConfigs.CLIENT.compassHudOffsetX.get();
        int visibleDegreeRange = LostTalesConfigs.CLIENT.compassHudDisplayRadius.get();

        int compassX = (windowWidth - COMPASS_WIDTH) * customOffsetX / 100;
        int compassY = windowHeight * customOffsetY / 100 + minecraft.font.lineHeight + COMPASS_OFFSET_Y;
        int compassCenterX = compassX + COMPASS_WIDTH / 2;

        float yaw = minecraft.player.getYRot();
        float normalizedYaw = ((yaw % 360) + 360) % 360;
        float pixelPerDegree = (float) COMPASS_WIDTH / visibleDegreeRange;

        // Draw Compass Background Texture.
        guiGraphics.blit(RenderType::guiTextured, COMPASS_HUD_TEXTURE, compassX, compassY, 0, 0,
                COMPASS_WIDTH, COMPASS_HEIGHT, TEXTURE_COMPASS_HUD_WIDTH, TEXTURE_COMPASS_HUD_HEIGHT);

        // Draw Icon Textures and Name of Focused Icon.
        drawMapIcons(minecraft, guiGraphics, compassCenterX, compassY, normalizedYaw, pixelPerDegree, visibleDegreeRange);
    }

    private static void drawMapIcons(Minecraft minecraft, GuiGraphics guiGraphics, int compassCenterX, int compassY,
                                     float normalizedYaw, float pixelPerDegree, int visibleDegreeRange) {
        List<LostTalesMapMarker> icons = new ArrayList<>();
        for (LostTalesMapMarkerProvider provider : PROVIDERS) {
            try {
                List<LostTalesMapMarker> batch = provider.collectMapMarkers(minecraft);
                if (batch != null && !batch.isEmpty()) icons.addAll(batch);
            } catch (Throwable ignored) {
                /* keep HUD resilient */
            }
        }
        if (icons.isEmpty()) return;

        int halfWidth = COMPASS_WIDTH / 2;

        // Cache per-icon draw info
        record RenderItem(LostTalesMapMarker icon, int px, float edgeT, float centerEmphasis,
                          float alphaT, int color, float baseScale) {}
        List<RenderItem> renderItems = new ArrayList<>(icons.size());

        // Focus candidate tracking (single winner)
        LostTalesMapMarker bestIcon = null;
        int bestPx = 0;
        float bestEmphasis = 0f;
        double bestDx = 0, bestDy = 0, bestDz = 0;

        // -------- Pass 1: compute geometry, cache draw info, choose best candidate --------
        for (LostTalesMapMarker icon : icons) {
            // Ignore icons without world coordinates; compass needs an angle
            if (icon.x() == null || icon.z() == null) continue;

            double dx = icon.x() - minecraft.player.getX();
            double dz = icon.z() - minecraft.player.getZ();

            float targetAngle = (float) Math.toDegrees(Math.atan2(-dx, dz));
            targetAngle %= 360f;
            if (targetAngle < 0f) targetAngle += 360f;

            float delta = targetAngle - normalizedYaw;
            delta %= 360f;
            if (delta >= 180f) delta -= 360f;
            if (delta < -180f)  delta += 360f;

            if (Math.abs(delta) > visibleDegreeRange / 2f) continue;

            int px = compassCenterX + Math.round(delta * pixelPerDegree);

            float edgeT = edgeCenterFactor(px, compassCenterX, halfWidth, START_EDGE_FADE_OFFSET);
            if (edgeT <= 0f) continue;

            int centerDistPx = Math.abs(px - compassCenterX);
            float centerEmphasis = focusEmphasis(centerDistPx, START_CENTER_FOCUS_OFFSET);

            float distT = 1f;
            if (icon.fadeOutRadiusDistance() > 0f) {
                double dist = Math.hypot(dx, dz);
                float r = icon.fadeOutRadiusDistance();
                float t = Math.max(0f, Math.min(1f, (float)(dist / r)));
                distT = 1f - (t * t * (3f - 2f * t));
            }
            float alphaT = Math.max(0f, Math.min(1f, edgeT * distT));
            if (alphaT <= 0f) continue;

            float baseScale = 1.0f - (MAP_MARKER_SCALE_MODIFIER / 2) * (1 - edgeT);
            int color = LostTalesGuiHelper.GuiColor.WHITE.getColorArgb(alphaT);

            renderItems.add(new RenderItem(icon, px, edgeT, centerEmphasis, alphaT, color, baseScale));

            // candidate can be ANY visible icon
            boolean better = centerEmphasis > bestEmphasis;
            if (!better && centerEmphasis == bestEmphasis) {
                int bestDist = Math.abs(bestPx - compassCenterX);
                better = centerDistPx < bestDist;
            }
            if (better) {
                bestEmphasis = centerEmphasis;
                bestIcon = icon;
                bestPx = px;

                bestDx = dx;
                bestDy = icon.y() != null ? icon.y() - minecraft.player.getY() : 0.0;
                bestDz = dz;
            }
        }

        if (renderItems.isEmpty()) return;

        // -------- Pass 2: draw icons; scale bump ONLY for bestIcon --------
        for (RenderItem ri : renderItems) {
            LostTalesMapMarker icon = ri.icon();
            float scale = ri.baseScale();
            if (bestIcon != null && icon == bestIcon && icon.scaleWithCenterFocus()) {
                scale = ri.baseScale() * (1f + MAP_MARKER_SCALE_MODIFIER * bestEmphasis);
            }

            var pose = guiGraphics.pose();
            pose.pushPose();
            pose.translate(ri.px(),
                    compassY + MAP_MARKER_OFFSET_Y + LostTalesMapMarker.getMapMarkerHeight() / 2f, 0);
            pose.scale(scale, scale, 1f);
            guiGraphics.blit(RenderType::guiTextured, LostTalesMapMarker.getMapMarkerTexture(),
                    -LostTalesMapMarker.getMapMarkerWidth() / 2, -LostTalesMapMarker.getMapMarkerHeight(),
                    icon.textureU(), icon.textureV(),
                    LostTalesMapMarker.getMapMarkerWidth(), LostTalesMapMarker.getMapMarkerHeight(),
                    LostTalesMapMarker.getMapMarkerTextureWidth(), LostTalesMapMarker.getMapMarkerTextureHeight(),
                    ri.color());
            pose.popPose();
        }

        // -------- Labels for the single focused icon --------
        if (bestIcon != null && bestEmphasis > 0f) {
            int mapMarkerNameLabelY = compassY + COMPASS_HEIGHT + MAP_MARKER_NAME_OFFSET_Y;
            int mapMarkerDistanceLabelY = compassY - minecraft.font.lineHeight - COMPASS_OFFSET_Y;

            int labelColor = LostTalesGuiHelper.GuiColor.WHITE.getColorArgb(bestEmphasis);

            // name
            drawCenteredText(guiGraphics, minecraft, bestIcon.name(), compassCenterX, mapMarkerNameLabelY, labelColor);

            // distance / vertical indicator (based on Y delta only; no hasVertical() gate anymore)
            if (bestIcon.showDistanceLabel()) {
                double distBlocks = Math.sqrt(bestDx * bestDx + bestDy * bestDy + bestDz * bestDz);
                Component label = Component.literal(Math.round(distBlocks) + "m");
                drawCenteredText(guiGraphics, minecraft, label, bestPx, mapMarkerDistanceLabelY, labelColor);

                // Show indicator when there's a meaningful vertical delta
                double deltaY = bestIcon.getYDelta(minecraft);
                if (Math.abs(deltaY) >= 5) {
                    int indicatorU = 0, indicatorV = 20, indicatorHeight = 3, indicatorWidth = 5;
                    if (deltaY >= 10.0) {
                        indicatorHeight = 7; // up-strong
                    } else if (deltaY <= -10.0) {
                        indicatorU = 6;      // down-strong
                        indicatorHeight = 7;
                    } else if (deltaY <= -5.0) {
                        indicatorU = 6;      // down-soft
                    }
                    guiGraphics.blit(RenderType::guiTextured, COMPASS_HUD_TEXTURE,
                            bestPx + minecraft.font.width(label) / 2 + VERTICAL_INDICATOR_OFFSET_X,
                            mapMarkerDistanceLabelY,
                            indicatorU, indicatorV, indicatorWidth, indicatorHeight,
                            TEXTURE_COMPASS_HUD_WIDTH, TEXTURE_COMPASS_HUD_HEIGHT, labelColor);
                }
            }
        }
    }

    // 1 inside, then smooth to 0 toward edges
    private static float edgeCenterFactor(int px, int centerX, int halfWidth, int fadeEdgePx) {
        int dist = Math.abs(px - centerX);
        int fadeStart = Math.max(0, halfWidth - fadeEdgePx);

        if (dist <= fadeStart) return 1f;
        if (dist >= halfWidth) return 0f;

        float t = (halfWidth - dist) / (float) (halfWidth - fadeStart);
        t = 0.5f - 0.5f * (float) Math.cos(Math.PI * t);
        return t;
    }

    // focus curve near center (0..1), Hermite smoothstep
    private static float focusEmphasis(int centerDistPx, int focusRadiusPx) {
        float t = 1f - Math.min(1f, centerDistPx / (float) focusRadiusPx);
        return t * t * (3f - 2f * t);
    }

    private static void drawCenteredText(GuiGraphics g, Minecraft mc, Component text, int cx, int y, int argb) {
        if (text == null) return;
        int x = cx - mc.font.width(text) / 2;
        g.drawString(mc.font, text, x, y, argb, true);
    }
}
