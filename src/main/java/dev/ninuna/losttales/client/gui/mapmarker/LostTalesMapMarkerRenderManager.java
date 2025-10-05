package dev.ninuna.losttales.client.gui.mapmarker;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.ninuna.losttales.client.gui.LostTalesColor;
import dev.ninuna.losttales.client.gui.hud.compass.LostTalesCompassHudRenderHelper;
import dev.ninuna.losttales.client.gui.hud.compass.mapmarker.LostTalesCompassHudMapMarkerBatchBuilder;
import dev.ninuna.losttales.client.gui.hud.compass.mapmarker.LostTalesCompassHudMapMarkerRenderItem;
import dev.ninuna.losttales.client.gui.hud.compass.mapmarker.LostTalesCompassHudMapMarkerRenderPass;
import dev.ninuna.losttales.client.gui.mapmarker.custom.LostTalesPositionMapMarker;
import dev.ninuna.losttales.client.gui.mapmarker.provider.LostTalesMapMarkerProvider;
import dev.ninuna.losttales.client.gui.mapmarker.provider.custom.LostTalesDirectionMapMarkerProvider;
import dev.ninuna.losttales.client.gui.mapmarker.provider.custom.LostTalesHostileMapMarkerProvider;
import dev.ninuna.losttales.client.gui.mapmarker.provider.custom.LostTalesSharedMapMarkerProvider;
import dev.ninuna.losttales.common.LostTales;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class LostTalesMapMarkerRenderManager {

    private final List<LostTalesMapMarkerProvider> mapMarkerProviders;

    public LostTalesMapMarkerRenderManager() {
        this.mapMarkerProviders = List.of(
                new LostTalesDirectionMapMarkerProvider(),
                new LostTalesSharedMapMarkerProvider(),
                new LostTalesHostileMapMarkerProvider()
        );
    }

    public LostTalesMapMarkerRenderManager(List<LostTalesMapMarkerProvider> mapMarkerProviders) {
        this.mapMarkerProviders = mapMarkerProviders;
    }

    public void renderMapMarkersOnCompassHud(Minecraft minecraft, GuiGraphics guiGraphics, int centerX, int compassY, float yawDeg, float pixelPerDegree, int visibleDeg, float partialTick) {
        List<LostTalesPositionMapMarker> mapMarkers = collectMapMarkers(minecraft);
        if (mapMarkers.isEmpty() || minecraft.player == null) return;

        var playerPos = LostTalesCompassHudRenderHelper.lerpPlayerPos(minecraft.player, partialTick);

        List<LostTalesCompassHudMapMarkerRenderItem> batch = LostTalesCompassHudMapMarkerBatchBuilder.build(
                mapMarkers, playerPos, yawDeg, pixelPerDegree, visibleDeg, centerX
        );

        if (batch.isEmpty()) return;

        LostTalesCompassHudMapMarkerRenderPass.drawMapMarkerIconsAndLabels(minecraft, guiGraphics, batch, compassY, centerX);
    }

    public void renderMapMarkersOnLevel(Minecraft minecraft, PoseStack poseStack, ClientLevel level) {
        List<LostTalesPositionMapMarker> mapMarkers = collectMapMarkers(minecraft);
        if (mapMarkers.isEmpty()) return;

        // Camera setup
        Camera camera = minecraft.gameRenderer.getMainCamera();
        Vec3 cameraPosition = camera.getPosition();
        EntityRenderDispatcher dispatcher = minecraft.getEntityRenderDispatcher();
        Font font = minecraft.font;

        MultiBufferSource.BufferSource bufferSource = minecraft.renderBuffers().bufferSource();

        for (var mapMarker : mapMarkers) {
            // Must be in the same dimension and have an active quest
            if (mapMarker.getDimension() != level.dimension()) continue;

            // If the class doesn’t already have this, add a getter:
            // public boolean hasActiveQuest() { return hasActiveQuest; }
            if (!mapMarker.isHasActiveQuest()) continue;

            LostTales.LOGGER.info("Render!");

            double x = mapMarker.getX() + 0.5; // center above block
            double y = mapMarker.getY() + LostTalesMapMarkerIcon.VERTICAL_OFFSET;
            double z = mapMarker.getZ() + 0.5;

            double dx = x - cameraPosition.x;
            double dy = y - cameraPosition.y;
            double dz = z - cameraPosition.z;

            double distSq = dx*dx + dy*dy + dz*dz;
            if (distSq > LostTalesMapMarkerIcon.MAP_MARKER_RENDER_MAX_RENDER_DISTANCE * LostTalesMapMarkerIcon.MAP_MARKER_RENDER_MAX_RENDER_DISTANCE) continue;

            // Fade alpha with distance (soft fade after ~24 blocks)
            float dist = (float)Math.sqrt(distSq);
            float alpha = 1.0f - Mth.clamp((dist - 24f) / 40f, 0f, 0.8f); // keeps at least 20% visible close-up

            // Billboarded text/icon: rotate to face camera, like name tags
            poseStack.pushPose();
            poseStack.translate(dx, dy, dz);
            poseStack.mulPose(dispatcher.cameraOrientation());
            // Flip Y to keep text upright like name tags
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0f));
            poseStack.scale(-LostTalesMapMarkerIcon.BASE_SCALE, -LostTalesMapMarkerIcon.BASE_SCALE, LostTalesMapMarkerIcon.BASE_SCALE);

            // Compose label: up-arrow + marker name (fallback if name missing)
            String label = "⬆";
            if (mapMarker.getName() != null && !mapMarker.getName().getString().isEmpty()) {
                label += " " + mapMarker.getName().getString();
            }

            int labelWidth = font.width(label);
            float xCenter = -labelWidth / 2f;
            float yText   = 0f;

            // Colors with alpha applied
            int a = (int)(alpha * 255f) & 0xFF;
            int color = LostTalesColor.WHITE.getColorWithAlpha(a);
            int shadow = LostTalesColor.BLACK.getColorWithAlpha(a);

            // Drop shadow for readability
            font.drawInBatch(label, xCenter + 1, yText + 1, shadow, false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 0xF000F0);
            font.drawInBatch(label, xCenter,     yText,     color,  false, poseStack.last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 0xF000F0);

            poseStack.popPose();
        }
        bufferSource.endBatch();
    }

    public void renderMapMarkersOnMap(Minecraft minecraft, PoseStack poseStack, ClientLevel level) {
        //Todo...
    }

    private List<LostTalesPositionMapMarker> collectMapMarkers(Minecraft minecraft) {
        List<LostTalesPositionMapMarker> mapMarkers = new ArrayList<>();
        for (LostTalesMapMarkerProvider provider : mapMarkerProviders) {
            try {
                List<LostTalesPositionMapMarker> collectedMapMarkers = provider.collectMapMarkers(minecraft);
                if (collectedMapMarkers != null && !collectedMapMarkers.isEmpty()) mapMarkers.addAll(collectedMapMarkers);
            } catch (Throwable ignored) {
            }
        }
        return mapMarkers;
    }
}
