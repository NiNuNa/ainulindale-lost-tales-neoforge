package dev.ninuna.losttales.client.gui.hud.compass.mapmarker;

import dev.ninuna.losttales.client.gui.LostTalesColor;
import dev.ninuna.losttales.client.gui.hud.compass.LostTalesCompassHudRenderer;
import dev.ninuna.losttales.client.gui.hud.compass.LostTalesCompassHudRenderHelper;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarkerIcon;
import dev.ninuna.losttales.client.gui.mapmarker.custom.LostTalesPositionMapMarker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

import java.util.List;

public class LostTalesCompassHudMapMarkerRenderPass {
    private static LostTalesPositionMapMarker focusedMapMarker;
    private static float focusEmphasis;
    private static float focusPx;
    private static double fdx, fdy, fdz;

    public static void setFocusContext(LostTalesPositionMapMarker mapMarker, float emphasis, float px, double dx, double dy, double dz) {
        focusedMapMarker = mapMarker;
        focusEmphasis = emphasis;
        focusPx = px;
        fdx = dx; fdy = dy; fdz = dz;
    }

    public static void drawMapMarkerIconsAndLabels(Minecraft minecraft, GuiGraphics guiGraphics, List<LostTalesCompassHudMapMarkerRenderItem> mapMarkerRenderItems, int compassY, int centerX) {
        for (LostTalesCompassHudMapMarkerRenderItem mapMarkerRenderItem : mapMarkerRenderItems) {
            var pose = guiGraphics.pose();
            float scale = (focusedMapMarker != null && mapMarkerRenderItem.marker() == focusedMapMarker && mapMarkerRenderItem.marker().isScaleWithCenterFocus()) ? 1f + LostTalesCompassHudRenderer.MAP_MARKER_SCALE_MODIFIER * focusEmphasis : 1f;
            var icon = mapMarkerRenderItem.activeQuest() ? LostTalesMapMarkerIcon.QUEST : mapMarkerRenderItem.marker().getIcon();
            float iconShadowAlpha = Math.min(LostTalesCompassHudRenderer.MAP_MARKER_SHADOW_ALPHA, mapMarkerRenderItem.alpha());

            pose.pushMatrix();
            pose.translate(mapMarkerRenderItem.x(), compassY + LostTalesCompassHudRenderer.MAP_MARKER_OFFSET_Y + LostTalesMapMarkerIcon.MAP_MARKER_ICON_HEIGHT / 2f);
            pose.scale(scale, scale);

            LostTalesCompassHudRenderHelper.drawBlitWithShadow(
                    guiGraphics,
                    LostTalesMapMarkerIcon.MAP_MARKER_ICON_TEXTURE,
                    (float) -LostTalesMapMarkerIcon.MAP_MARKER_ICON_WIDTH / 2, -LostTalesMapMarkerIcon.MAP_MARKER_ICON_HEIGHT,
                    icon.getU(), icon.getV(),
                    LostTalesMapMarkerIcon.MAP_MARKER_ICON_WIDTH, LostTalesMapMarkerIcon.MAP_MARKER_ICON_HEIGHT,
                    LostTalesMapMarkerIcon.MAP_MARKER_ICON_TEXTURE_WIDTH, LostTalesMapMarkerIcon.MAP_MARKER_ICON_TEXTURE_HEIGHT,
                    mapMarkerRenderItem.color(), LostTalesColor.BLACK.getColorWithAlpha(iconShadowAlpha)
            );
            pose.popMatrix();
        }

        if (focusedMapMarker != null && focusEmphasis > 0f) {
            int nameY = compassY + LostTalesCompassHudRenderer.COMPASS_HEIGHT + LostTalesCompassHudRenderer.MAP_MARKER_NAME_LABEL_OFFSET_Y;
            int distY = compassY - minecraft.font.lineHeight - LostTalesCompassHudRenderer.MAP_MARKER_DISTANCE_LABEL_OFFSET_Y;
            int labelColor = LostTalesColor.WHITE.getColorWithAlpha(focusEmphasis);

            LostTalesCompassHudRenderHelper.drawCenteredString(guiGraphics, minecraft, focusedMapMarker.getName(), focusPx, nameY, labelColor, true);

            if (focusedMapMarker.isShowDistanceLabel()) {
                double distBlocks = Math.sqrt(fdx*fdx + fdy*fdy + fdz*fdz);
                Component label = Component.literal(Math.round(distBlocks) + "m");
                float px = focusPx + (float) minecraft.font.width(label) / 2f + LostTalesCompassHudRenderer.MAP_MARKER_VERTICAL_ARROW_INDICATOR_OFFSET_X;
                float arrowShadowAlpha = Math.min(LostTalesCompassHudRenderer.MAP_MARKER_SHADOW_ALPHA, focusEmphasis);

                LostTalesCompassHudRenderHelper.drawCenteredString(guiGraphics, minecraft, label, focusPx, distY, labelColor, true);

                double deltaY = fdy;
                if (Math.abs(deltaY) >= 5) {
                    int u = 0, v = 26, h = 3, w = 5;
                    if (deltaY >= 10.0) {
                        h = 7; // up-strong
                    } else if (deltaY <= -10.0) {
                        u = 6; h = 7; // down-strong
                    } else if (deltaY <= -5.0) {
                        u = 6; // down-soft
                    }

                    LostTalesCompassHudRenderHelper.drawBlitWithShadow(guiGraphics,
                            LostTalesCompassHudRenderer.COMPASS_HUD_TEXTURE,
                            px, distY,
                            u, v, w, h,
                            LostTalesCompassHudRenderer.COMPASS_HUD_TEXTURE_WIDTH, LostTalesCompassHudRenderer.COMPASS_HUD_TEXTURE_HEIGHT,
                            labelColor,
                            LostTalesColor.BLACK.getColorWithAlpha(arrowShadowAlpha)
                    );
                }
            }
        }

        // clear focus context for safety
        focusedMapMarker = null;
    }
}
