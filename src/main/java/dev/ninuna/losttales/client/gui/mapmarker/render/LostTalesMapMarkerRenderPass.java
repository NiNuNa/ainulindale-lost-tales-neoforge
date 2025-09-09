package dev.ninuna.losttales.client.gui.mapmarker.render;

import dev.ninuna.losttales.client.gui.LostTalesColor;
import dev.ninuna.losttales.client.gui.hud.compass.LostTalesCompassHudRenderer;
import dev.ninuna.losttales.client.gui.hud.compass.LostTalesCompassHudRenderHelper;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarkerIcon;
import dev.ninuna.losttales.client.gui.mapmarker.custom.LostTalesPositionMapMarker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

import java.util.List;

public class LostTalesMapMarkerRenderPass {

    // Focus context (computed in batch builder)
    private static LostTalesPositionMapMarker focused;
    private static float focusEmphasis;
    private static float focusPx;
    private static double fdx, fdy, fdz;

    public static void setFocusContext(LostTalesPositionMapMarker marker, float emphasis, float px, double dx, double dy, double dz) {
        focused = marker;
        focusEmphasis = emphasis;
        focusPx = px;
        fdx = dx; fdy = dy; fdz = dz;
    }

    static void drawIconsAndLabels(Minecraft mc, GuiGraphics gfx, List<LostTalesMapMarkerRenderItem> items, int compassY, int centerX) {
        // pass 1: icons
        for (LostTalesMapMarkerRenderItem it : items) {
            var pose = gfx.pose();
            pose.pushMatrix();
            pose.translate(it.x(), compassY + LostTalesCompassHudRenderer.MAP_MARKER_OFFSET_Y + LostTalesMapMarkerIcon.MAP_MARKER_ICON_HEIGHT / 2f);
            float scale = (focused != null && it.marker() == focused && it.marker().isScaleWithCenterFocus()) ? 1f + LostTalesCompassHudRenderer.MAP_MARKER_SCALE_MODIFIER * focusEmphasis : 1f;
            pose.scale(scale, scale);

            var icon = it.activeQuest() ? LostTalesMapMarkerIcon.QUEST : it.marker().getIcon();

            gfx.blit(
                    RenderPipelines.GUI_TEXTURED,
                    LostTalesMapMarkerIcon.MAP_MARKER_ICON_TEXTURE,
                    -LostTalesMapMarkerIcon.MAP_MARKER_ICON_WIDTH / 2,
                    -LostTalesMapMarkerIcon.MAP_MARKER_ICON_HEIGHT,
                    icon.getU(), icon.getV(),
                    LostTalesMapMarkerIcon.MAP_MARKER_ICON_WIDTH, LostTalesMapMarkerIcon.MAP_MARKER_ICON_HEIGHT,
                    LostTalesMapMarkerIcon.MAP_MARKER_ICON_TEXTURE_WIDTH, LostTalesMapMarkerIcon.MAP_MARKER_ICON_TEXTURE_HEIGHT,
                    it.color()
            );
            pose.popMatrix();
        }

        // pass 2: label for focused marker
        if (focused != null && focusEmphasis > 0f) {
            int nameY = compassY + LostTalesCompassHudRenderer.COMPASS_HEIGHT + LostTalesCompassHudRenderer.MAP_MARKER_NAME_LABEL_OFFSET_Y;
            int distY = compassY - mc.font.lineHeight - LostTalesCompassHudRenderer.MAP_MARKER_DISTANCE_LABEL_OFFSET_Y;
            int labelColor = LostTalesColor.WHITE.getColorWithAlpha(focusEmphasis);

            LostTalesCompassHudRenderHelper.drawStringCentered(gfx, mc, focused.getName(), focusPx, nameY, labelColor, true);

            if (focused.isShowDistanceLabel()) {
                double distBlocks = Math.sqrt(fdx*fdx + fdy*fdy + fdz*fdz);
                Component label = Component.literal(Math.round(distBlocks) + "m");
                LostTalesCompassHudRenderHelper.drawStringCentered(gfx, mc, label, focusPx, distY, labelColor, true);

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

                    float px = focusPx + (float) mc.font.width(label) / 2f + LostTalesCompassHudRenderer.MAP_MARKER_VERTICAL_ARROW_INDICATOR_OFFSET_X;
                    LostTalesCompassHudRenderHelper.drawBlit(gfx,
                            LostTalesCompassHudRenderer.COMPASS_HUD_TEXTURE,
                            px, distY,
                            u, v, w, h,
                            LostTalesCompassHudRenderer.COMPASS_HUD_TEXTURE_WIDTH, LostTalesCompassHudRenderer.COMPASS_HUD_TEXTURE_HEIGHT,
                            labelColor
                    );
                }
            }
        }

        // clear focus context for safety
        focused = null;
    }
}
