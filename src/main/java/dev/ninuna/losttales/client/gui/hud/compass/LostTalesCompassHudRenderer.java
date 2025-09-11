package dev.ninuna.losttales.client.gui.hud.compass;

import com.mojang.blaze3d.platform.Window;
import dev.ninuna.losttales.client.gui.mapmarker.render.LostTalesMapMarkerRenderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.ResourceLocation;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.config.LostTalesConfigs;

public class LostTalesCompassHudRenderer {

    public static final ResourceLocation COMPASS_HUD_TEXTURE = LostTales.getResourceLocation("textures/gui/compasshud.png");

    public static final int COMPASS_HUD_TEXTURE_WIDTH = 257;
    public static final int COMPASS_HUD_TEXTURE_HEIGHT = 64;

    public static final int COMPASS_WIDTH  = COMPASS_HUD_TEXTURE_WIDTH;
    public static final int COMPASS_HEIGHT = 24;

    public static final int MAP_MARKER_OFFSET_Y = 8;
    public static final int MAP_MARKER_NAME_LABEL_OFFSET_Y = 3;
    public static final int MAP_MARKER_DISTANCE_LABEL_OFFSET_Y = 4;

    public static final float MAP_MARKER_SCALE_MODIFIER = 0.18f;
    public static final float MAP_MARKER_DISTANCE_FADE_IN_FLOOR_ALPHA = 0.4f;
    public static final float MAP_MARKER_SHADOW_ALPHA = 0.6f;

    public static final int MAP_MARKER_VERTICAL_ARROW_INDICATOR_OFFSET_X = 2;
    public static final int MAP_MARKER_BEGIN_EDGE_FADE_OUT_OFFSET = COMPASS_WIDTH / 4;
    public static final int MAP_MARKER_BEGIN_CENTER_FOCUS_OFFSET = 26;
    public static final int MAP_MARKER_END_CENTER_FOCUS_OFFSET = 60;

    private final LostTalesMapMarkerRenderManager markerManager;

    public LostTalesCompassHudRenderer() {
        this.markerManager = new LostTalesMapMarkerRenderManager();
    }

    public LostTalesCompassHudRenderer(LostTalesMapMarkerRenderManager markerManager) {
        this.markerManager = markerManager;
    }

    public void render(Minecraft minecraft, GuiGraphics guiGraphics) {
        if (minecraft.player == null) return;

        Window window = minecraft.getWindow();
        int windowGuiScaledWidth = window.getGuiScaledWidth();
        int windowGuiScaledHeight = window.getGuiScaledHeight();

        int offsetX = LostTalesConfigs.CLIENT.compassHudOffsetX.get();
        int offsetY = LostTalesConfigs.CLIENT.compassHudOffsetY.get();
        int displayRadiusDeg = LostTalesConfigs.CLIENT.compassHudDisplayRadius.get();

        int compassX = (windowGuiScaledWidth - COMPASS_WIDTH) * offsetX / 100;
        int compassY =  windowGuiScaledHeight * offsetY / 100 + minecraft.font.lineHeight + MAP_MARKER_DISTANCE_LABEL_OFFSET_Y;
        int centerX  = compassX + COMPASS_WIDTH / 2;

        float pixelPerDegree = (float) COMPASS_WIDTH / (float) displayRadiusDeg;

        float partialTick = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(!minecraft.isPaused());
        float viewYaw = minecraft.player.getViewYRot(partialTick);
        float viewYawNormalized = LostTalesCompassHudRenderHelper.normalizeViewYaw(viewYaw);

        // Draw compass texture.
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, COMPASS_HUD_TEXTURE, compassX, compassY, 0, 0, COMPASS_WIDTH, COMPASS_HEIGHT, COMPASS_HUD_TEXTURE_WIDTH, COMPASS_HUD_TEXTURE_HEIGHT);

        // Draw map markers.
        markerManager.renderCompassMapMarkers(minecraft, guiGraphics, centerX, compassY, viewYawNormalized, pixelPerDegree, displayRadiusDeg, partialTick);
    }
}
