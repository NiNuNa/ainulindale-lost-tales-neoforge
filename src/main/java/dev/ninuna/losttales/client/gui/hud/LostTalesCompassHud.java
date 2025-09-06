package dev.ninuna.losttales.client.gui.hud;

import com.mojang.blaze3d.platform.Window;
import dev.ninuna.losttales.client.gui.LostTalesGuiColor;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesBearingMapMarker;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarkerIcon;
import dev.ninuna.losttales.client.gui.mapmarker.provider.custom.LostTalesSharedMapMarkerProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesPositionMapMarker;
import dev.ninuna.losttales.client.gui.mapmarker.provider.LostTalesMapMarkerProvider;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.client.gui.mapmarker.provider.custom.LostTalesDirectionMapMarkerProvider;
import dev.ninuna.losttales.client.gui.mapmarker.provider.custom.LostTalesHostileMapMarkerProvider;
import dev.ninuna.losttales.common.config.LostTalesConfigs;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class LostTalesCompassHud {

    private static final ResourceLocation COMPASS_HUD_TEXTURE = LostTales.getResourceLocation("textures/gui/compasshud.png");

    private static final int TEXTURE_COMPASS_HUD_WIDTH  = 193;
    private static final int TEXTURE_COMPASS_HUD_HEIGHT = 64;

    private static final int COMPASS_WIDTH  = TEXTURE_COMPASS_HUD_WIDTH;
    private static final int COMPASS_HEIGHT = 18;
    private static final int COMPASS_OFFSET_Y = 4;

    private static final int MAP_MARKER_OFFSET_Y = 5;
    private static final int MAP_MARKER_NAME_OFFSET_Y = 3;

    private static final float MAP_MARKER_SCALE_MODIFIER = 0.18f;
    private static final float MAP_MARKER_DISTANCE_FADE_IN_FLOOR_ALPHA = 0.4f;

    private static final int MAP_MARKER_VERTICAL_INDICATOR_OFFSET_X = 2;
    private static final int MAP_MARKER_BEGIN_EDGE_FADE_OUT_OFFSET = COMPASS_WIDTH / 4;
    private static final int MAP_MARKER_BEGIN_CENTER_FOCUS_OFFSET = 22;

    private static final List<LostTalesMapMarkerProvider> PROVIDERS = List.of(
            new LostTalesDirectionMapMarkerProvider(),
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
        float pixelPerDegree = (float) COMPASS_WIDTH / (float) visibleDegreeRange;

        int compassX = (windowWidth - COMPASS_WIDTH) * customOffsetX / 100;
        int compassY = windowHeight * customOffsetY / 100 + minecraft.font.lineHeight + COMPASS_OFFSET_Y;
        int compassCenterX = compassX + COMPASS_WIDTH / 2;

        float partialTick = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(!minecraft.isPaused());
        float viewYaw = minecraft.player.getViewYRot(partialTick);
        float normalizedViewYaw = ((viewYaw % 360f) + 360f) % 360f;

        // Draw Compass HUD Background
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, COMPASS_HUD_TEXTURE, compassX, compassY, 0, 0,
                COMPASS_WIDTH, COMPASS_HEIGHT, TEXTURE_COMPASS_HUD_WIDTH, TEXTURE_COMPASS_HUD_HEIGHT);

        // Draw Map Markers
        drawMapMarkers(minecraft, guiGraphics, compassCenterX, compassY, normalizedViewYaw, pixelPerDegree, visibleDegreeRange, partialTick);
    }

    private static void drawMapMarkers(Minecraft minecraft, GuiGraphics guiGraphics, int compassCenterX, int compassY, float normalizedViewYaw, float pixelPerDegree, int visibleDegreeRange, float partialTick) {
        List<LostTalesPositionMapMarker> mapMarkers = new ArrayList<>();
        for (LostTalesMapMarkerProvider mapMarkerProvider : PROVIDERS) {
            try {
                List<LostTalesPositionMapMarker> batch = mapMarkerProvider.collectMapMarkers(minecraft);
                if (batch != null && !batch.isEmpty()) mapMarkers.addAll(batch);
            } catch (Throwable ignored) { /* keep HUD resilient */ }
        }
        if (mapMarkers.isEmpty()) return;

        if (minecraft.player == null) return;
        var player = minecraft.player;

        // Interpolated player position (prevents strafe jitter)
        double playerPosX = Mth.lerp(partialTick, player.xo, player.getX());
        double playerPosY = Mth.lerp(partialTick, player.yo, player.getY());
        double playerPosZ = Mth.lerp(partialTick, player.zo, player.getZ());

        // Draw cache
        record RenderItem(
                LostTalesPositionMapMarker icon,
                float px,
                float centerEmphasis,
                float alphaT,
                int color,
                double distSq,
                double dy,
                boolean isActiveQuest
        ) {}
        List<RenderItem> renderItems = new ArrayList<>(mapMarkers.size());

        // Focus candidate
        LostTalesPositionMapMarker focusedMapMarker = null;
        float bestPx = 0f;
        float bestEmphasis = 0f;
        double bestDx = 0, bestDy = 0, bestDz = 0;

        float halfWidth = COMPASS_WIDTH / 2f;
        float minX = compassCenterX - halfWidth + (float) LostTalesMapMarkerIcon.MAP_MARKER_ICON_WIDTH / 2;
        float maxX = compassCenterX + halfWidth - (float) LostTalesMapMarkerIcon.MAP_MARKER_ICON_WIDTH / 2;

        // Pass 1: geometry + pick focus
        for (LostTalesPositionMapMarker mapMarker : mapMarkers) {
            boolean isActiveQuest = mapMarker.isHasActiveQuest();

            // World coords for angle
            double ix = mapMarker.getX();
            double iy = mapMarker.getY();
            double iz = mapMarker.getZ();

            double dx = ix - playerPosX;
            double dy = iy - playerPosY;
            double dz = iz - playerPosZ;

            // Angle to target (degrees) OR fixed bearing (for bearing markers)
            float targetAngle;
            if (mapMarker instanceof LostTalesBearingMapMarker bearing) {
                targetAngle = bearing.getBearingDegree();
                if (targetAngle < 0f) targetAngle = (targetAngle % 360f + 360f) % 360f;
                else if (targetAngle >= 360f) targetAngle = targetAngle % 360f;
            } else {
                targetAngle = (float) Math.toDegrees(Math.atan2(-dx, dz));
                if (targetAngle < 0f) targetAngle += 360f;
            }

            // Shortest signed delta to camera yaw
            float delta = (targetAngle - normalizedViewYaw + 540f) % 360f - 180f;

            // Visibility culling by degree range (skip this for active quests)
            if (!isActiveQuest && Math.abs(delta) > visibleDegreeRange / 2f) continue;

            // Screen x (clamp to edges for active quests)
            float rawPx = compassCenterX + (delta * pixelPerDegree);
            float pxF = isActiveQuest ? Mth.clamp(rawPx, minX, maxX) : rawPx;

            // Edge fade (disabled for active quests)
            float edgeT = isActiveQuest ? 1f : edgeCenterFactor(pxF, compassCenterX, halfWidth, MAP_MARKER_BEGIN_EDGE_FADE_OUT_OFFSET);
            if (edgeT <= 0f) continue;

            // Focus emphasis (unchanged – only affects scale of the focused one)
            float centerDistPx = Math.abs(pxF - compassCenterX);
            float centerEmphasis = focusEmphasis(centerDistPx, MAP_MARKER_BEGIN_CENTER_FOCUS_OFFSET);

            // Distance fade / cutoff (disabled for active quests)
            float distT = 1f;
            double distSq = dx * dx + dy * dy + dz * dz;
            if (!isActiveQuest && mapMarker.getFadeInRadius() > 0f) {
                double dist = Math.sqrt(distSq);
                double fadeInRadius = mapMarker.getFadeInRadius();
                if (dist > fadeInRadius) continue;
                float t = (float) (dist / fadeInRadius);
                distT = 1f - (t * t * (3f - 2f * t));
            }

            // Alpha: no edge fade and no distance fade for active quests (always fully visible)
            float distanceAlpha = MAP_MARKER_DISTANCE_FADE_IN_FLOOR_ALPHA + (1f - MAP_MARKER_DISTANCE_FADE_IN_FLOOR_ALPHA) * distT;
            float alphaT = isActiveQuest ? 1f : Mth.clamp(edgeT * distanceAlpha, 0f, 1f);
            if (alphaT <= 0f) continue;

            int color = mapMarker.getColor().getColorWithAlpha(alphaT);

            renderItems.add(new RenderItem(mapMarker, pxF, centerEmphasis, alphaT, color, distSq, dy, isActiveQuest));

            // Pick focus (unchanged)
            boolean better = centerEmphasis > bestEmphasis;
            if (!better && centerEmphasis == bestEmphasis) {
                better = Math.abs(pxF - compassCenterX) < Math.abs(bestPx - compassCenterX);
            }
            if (better) {
                bestEmphasis = centerEmphasis;
                focusedMapMarker = mapMarker;
                bestPx = pxF;

                bestDx = dx;
                bestDy = dy;
                bestDz = dz;
            }
        }

        if (renderItems.isEmpty()) return;

        // Sort: distance DESC (far first), then alpha ASC, then farther from center first (so centered ends up on top)
        renderItems.sort((a, b) -> {
            int c = Double.compare(b.distSq(), a.distSq());
            if (c != 0) return c;
            c = Float.compare(a.alphaT(), b.alphaT());
            if (c != 0) return c;
            float ac = Math.abs(a.px() - compassCenterX);
            float bc = Math.abs(b.px() - compassCenterX);
            return Float.compare(bc, ac);
        });

        // Pass 2: draw icons
        for (RenderItem renderItem : renderItems) {
            LostTalesPositionMapMarker icon = renderItem.icon();
            float mapMarkerScale = 1.0f;
            if (focusedMapMarker != null && icon == focusedMapMarker && icon.isScaleWithCenterFocus()) {
                mapMarkerScale *= (1f + MAP_MARKER_SCALE_MODIFIER * renderItem.centerEmphasis());
            }

            // 👇 Swap the icon if this is an active quest marker
            var actualIcon = icon.getIcon();
            if (renderItem.isActiveQuest()) {
                actualIcon = LostTalesMapMarkerIcon.QUEST;
            }

            var pose = guiGraphics.pose();
            pose.pushMatrix();
            pose.translate(renderItem.px(), compassY + MAP_MARKER_OFFSET_Y + LostTalesMapMarkerIcon.MAP_MARKER_ICON_HEIGHT / 2f);
            pose.scale(mapMarkerScale, mapMarkerScale);
            guiGraphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    LostTalesMapMarkerIcon.MAP_MARKER_ICON_TEXTURE,
                    -LostTalesMapMarkerIcon.MAP_MARKER_ICON_WIDTH / 2,
                    -LostTalesMapMarkerIcon.MAP_MARKER_ICON_HEIGHT,
                    actualIcon.getU(), actualIcon.getV(),
                    LostTalesMapMarkerIcon.MAP_MARKER_ICON_WIDTH, LostTalesMapMarkerIcon.MAP_MARKER_ICON_HEIGHT,
                    LostTalesMapMarkerIcon.MAP_MARKER_ICON_TEXTURE_WIDTH, LostTalesMapMarkerIcon.MAP_MARKER_ICON_TEXTURE_HEIGHT,
                    renderItem.color()
            );
            pose.popMatrix();
        }

        // Focused label (name + distance at center, unchanged)
        if (focusedMapMarker != null && bestEmphasis > 0f) {
            int mapMarkerNameLabelY = compassY + COMPASS_HEIGHT + MAP_MARKER_NAME_OFFSET_Y;
            int mapMarkerDistanceLabelY = compassY - minecraft.font.lineHeight - COMPASS_OFFSET_Y;
            int labelColor = LostTalesGuiColor.WHITE.getColorWithAlpha(bestEmphasis);

            // Name centered on compass center
            drawString(guiGraphics, minecraft, focusedMapMarker.getName(), bestPx, mapMarkerNameLabelY, labelColor, true);

            if (focusedMapMarker.isShowDistanceLabel()) {
                double distBlocks = Math.sqrt(bestDx * bestDx + bestDy * bestDy + bestDz * bestDz);
                Component label = Component.literal(Math.round(distBlocks) + "m");

                drawString(guiGraphics, minecraft, label, bestPx, mapMarkerDistanceLabelY, labelColor, true);

                double deltaY = bestDy;
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
                    drawBlit(
                            guiGraphics,
                            COMPASS_HUD_TEXTURE,
                            bestPx + (float) minecraft.font.width(label) / 2 + MAP_MARKER_VERTICAL_INDICATOR_OFFSET_X,
                            mapMarkerDistanceLabelY,
                            indicatorU, indicatorV, indicatorWidth, indicatorHeight,
                            TEXTURE_COMPASS_HUD_WIDTH, TEXTURE_COMPASS_HUD_HEIGHT,
                            labelColor
                    );
                }
            }
        }
    }

    // 1 inside, smooth to 0 toward edges (cosine smoothstep)
    private static float edgeCenterFactor(float px, float centerX, float halfWidth, float fadeEdgePx) {
        float dist = Math.abs(px - centerX);
        float fadeStart = Math.max(0f, halfWidth - fadeEdgePx);

        if (dist <= fadeStart) return 1f;
        if (dist >= halfWidth) return 0f;

        float t = (halfWidth - dist) / (halfWidth - fadeStart);
        return 0.5f - 0.5f * (float) Math.cos(Math.PI * t);
    }

    // Focus curve near center (0..1), Hermite smoothstep
    private static float focusEmphasis(float centerDistPx, float focusRadiusPx) {
        float t = 1f - Math.min(1f, centerDistPx / focusRadiusPx);
        return t * t * (3f - 2f * t);
    }

    private static void drawString(GuiGraphics guiGraphics, Minecraft minecraft, Component text, float x, float y, int argb, boolean shadow) {
        if (text == null) return;
        x = x - minecraft.font.width(text) / 2f;

        int xInt = Mth.floor(x);
        int yInt = Mth.floor(y);
        float xFrac = x - xInt;
        float yFrac = y - yInt;

        var pose = guiGraphics.pose();
        pose.pushMatrix();
        pose.translate(xFrac, yFrac);
        guiGraphics.drawString(minecraft.font, text, xInt, yInt, argb, shadow);
        pose.popMatrix();
    }

    private static void drawBlit(GuiGraphics guiGraphics, ResourceLocation resourceLocation, float x, float y, int u, int v, int width, int height, int textureWidth, int textureHeight, int color) {
        int xInt = Mth.floor(x);
        int yInt = Mth.floor(y);
        float xFrac = x - xInt;
        float yFrac = y - yInt;

        var pose = guiGraphics.pose();
        pose.pushMatrix();
        pose.translate(xFrac, yFrac);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, resourceLocation, xInt, yInt, u, v, width, height, textureWidth, textureHeight, color);
        pose.popMatrix();
    }
}
