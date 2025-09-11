package dev.ninuna.losttales.client.gui.mapmarker.render;

import dev.ninuna.losttales.client.gui.hud.compass.LostTalesCompassHudRenderHelper;
import dev.ninuna.losttales.client.gui.mapmarker.provider.LostTalesMapMarkerBatchBuilder;
import dev.ninuna.losttales.client.gui.mapmarker.custom.LostTalesPositionMapMarker;
import dev.ninuna.losttales.client.gui.mapmarker.provider.LostTalesMapMarkerProvider;
import dev.ninuna.losttales.client.gui.mapmarker.provider.custom.LostTalesDirectionMapMarkerProvider;
import dev.ninuna.losttales.client.gui.mapmarker.provider.custom.LostTalesHostileMapMarkerProvider;
import dev.ninuna.losttales.client.gui.mapmarker.provider.custom.LostTalesSharedMapMarkerProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

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

    public void renderCompassMapMarkers(Minecraft minecraft, GuiGraphics guiGraphics, int centerX, int compassY, float yawDeg, float pixelPerDegree, int visibleDeg, float partialTick) {
        List<LostTalesPositionMapMarker> mapMarkers = collectMapMarkers(minecraft);
        if (mapMarkers.isEmpty() || minecraft.player == null) return;

        var playerPos = LostTalesCompassHudRenderHelper.lerpPlayerPos(minecraft.player, partialTick);

        List<LostTalesMapMarkerRenderItem> batch = LostTalesMapMarkerBatchBuilder.build(
                mapMarkers, playerPos, yawDeg, pixelPerDegree, visibleDeg, centerX
        );
        if (batch.isEmpty()) return;

        LostTalesMapMarkerRenderPass.drawMapMarkerIconsAndLabels(minecraft, guiGraphics, batch, compassY, centerX);
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
