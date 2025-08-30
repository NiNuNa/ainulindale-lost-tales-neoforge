package ninuna.losttales.client.gui;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import ninuna.losttales.client.util.LostTalesGuiUtil;
import ninuna.losttales.config.LostTalesConfigs;

public class LostTalesCompassHud {

    private static final int COMPASS_WIDTH = 192;
    private static final int COMPASS_HEIGHT = 20;
    private static final int MAP_MARKER_NAME_TEXT_OFFSET_Y = 5;
    private static final int EDGE_FADE = COMPASS_WIDTH / 3;
    private static final float MIN_SCALE = 0.80f; // scale at the very edge (0.80 = 80%)

    public static void renderHud(Minecraft minecraft, GuiGraphics guiGraphics) {
        Window window = minecraft.getWindow();
        int windowWidth = window.getGuiScaledWidth();
        int windowHeight = window.getGuiScaledHeight();
        int customOffsetY = 5; //LostTalesConfigs.CLIENT.compassHudOffsetY.get();

        // Compass HUD Layout
        int compassVisibleDegreeRange = 75;
        int compassX = (windowWidth - COMPASS_WIDTH) / 2;
        int compassY = windowHeight * customOffsetY / 100;
        int compassCenterX = compassX + COMPASS_WIDTH / 2;

        // Draw Compass Texture
        guiGraphics.fill(compassX, compassY, compassX + COMPASS_WIDTH, compassY + COMPASS_HEIGHT, 0x66000000);

        float yaw = minecraft.player.getYRot();
        float normalizedYaw = ((yaw % 360) + 360) % 360;
        float pxPerDeg = (float) COMPASS_WIDTH / compassVisibleDegreeRange;

        for (int angle = 0; angle < 360; angle += 45) {
            float delta = wrapDegrees(angle - normalizedYaw);
            if (Math.abs(delta) > compassVisibleDegreeRange / 2f) continue;

            int px = compassCenterX + Math.round(delta * pxPerDeg);

            // Compute factor: 1 = center, 0 = edges
            float t = edgeCenterFactor(px, compassCenterX, COMPASS_WIDTH / 2, EDGE_FADE);

            // Fade (alpha)
            int alpha = Math.round(255f * t);
            if (alpha <= 0) continue;

            // Scale: MIN_SCALE at edge → 1.0 at center
            float scale = MIN_SCALE + (1f - MIN_SCALE) * t;

            String label = dirLabel(angle);
            int w = minecraft.font.width(label);
            int h = minecraft.font.lineHeight; // font height

            int argb = (alpha << 24) | 0xFFFFFF;

            var pose = guiGraphics.pose();
            pose.pushPose();

            // move to (px, bottom-anchor position)
            pose.translate(px, compassY + 5 + h, 0);

            // scale relative to bottom
            pose.scale(scale, scale, 1f);

            // draw baseline at y = -h so the bottom stays fixed
            guiGraphics.drawString(minecraft.font, label, -w / 2, -h, argb, false);

            pose.popPose();
        }

        // Todo: Draw name of map marker below compass.
        String mapMarkerNameText = "LOL";
        int mapMarkerNameTextX = compassCenterX - minecraft.font.width(mapMarkerNameText) / 2;
        int mapMarkerNameTextY = compassY + COMPASS_HEIGHT + MAP_MARKER_NAME_TEXT_OFFSET_Y;
        //guiGraphics.drawString(minecraft.font, mapMarkerNameText, mapMarkerNameTextX, mapMarkerNameTextY, LostTalesGuiUtil.COLOR_WHITE, true);
    }

    // Returns 1 in the fully-opaque inner region, falling linearly to 0 at the edges
    private static float edgeCenterFactor(int px, int centerX, int halfWidth, int fadeEdgePx) {
        int dist = Math.abs(px - centerX);
        int fadeStart = Math.max(0, halfWidth - fadeEdgePx);

        if (dist <= fadeStart) return 1f;     // fully visible
        if (dist >= halfWidth) return 0f;     // completely invisible

        float t = (halfWidth - dist) / (float) (halfWidth - fadeStart);
        // Optional smoother easing:
        t = 0.5f - 0.5f * (float)Math.cos(Math.PI * t);
        return t;
    }

    // Wrap a degree difference into [-180, 180)
    private static float wrapDegrees(float deg) {
        deg %= 360f;
        if (deg >= 180f) deg -= 360f;
        if (deg < -180f) deg += 360f;
        return deg;
    }

    private static String dirLabel(int angle) {
        angle = ((angle % 360) + 360) % 360;
        return switch (angle) {
            case 0 -> "S";
            case 45 -> "SW";
            case 90 -> "W";
            case 135 -> "NW";
            case 180 -> "N";
            case 225 -> "NE";
            case 270 -> "E";
            case 315 -> "SE";
            default -> "";
        };
    }
}
