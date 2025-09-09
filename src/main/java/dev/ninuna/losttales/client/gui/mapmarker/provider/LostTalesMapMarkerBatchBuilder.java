package dev.ninuna.losttales.client.gui.mapmarker.provider;

import dev.ninuna.losttales.client.gui.LostTalesColor;
import dev.ninuna.losttales.client.gui.hud.compass.LostTalesCompassHudRenderer;
import dev.ninuna.losttales.client.gui.hud.compass.LostTalesCompassHudRenderHelper;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarkerIcon;
import dev.ninuna.losttales.client.gui.mapmarker.custom.LostTalesBearingMapMarker;
import dev.ninuna.losttales.client.gui.mapmarker.custom.LostTalesPositionMapMarker;
import dev.ninuna.losttales.client.gui.mapmarker.render.LostTalesMapMarkerRenderItem;
import dev.ninuna.losttales.client.gui.mapmarker.render.LostTalesMapMarkerRenderPass;
import net.minecraft.util.Mth;

import java.util.ArrayList;
import java.util.List;

public class LostTalesMapMarkerBatchBuilder {

    public static List<LostTalesMapMarkerRenderItem> build(List<LostTalesPositionMapMarker> markers, LostTalesCompassHudRenderHelper.PlayerPos playerPos, float yawDeg, float pxPerDeg, int visibleDeg, int centerX) {

        float halfWidth = LostTalesCompassHudRenderer.COMPASS_WIDTH / 2f;
        float minX = centerX - halfWidth + (float) LostTalesMapMarkerIcon.MAP_MARKER_ICON_WIDTH / 2;
        float maxX = centerX + halfWidth - (float) LostTalesMapMarkerIcon.MAP_MARKER_ICON_WIDTH / 2;

        List<LostTalesMapMarkerRenderItem> renderItems = new ArrayList<>(markers.size());

        LostTalesPositionMapMarker focused = null;
        float bestEmphasis = 0f;
        float focusedPx = 0f;
        double bestDx=0, bestDy=0, bestDz=0;

        for (LostTalesPositionMapMarker m : markers) {
            boolean isQuest = m.isHasActiveQuest();

            // world deltas
            double dx = m.getX() - playerPos.x();
            double dy = m.getY() - playerPos.y();
            double dz = m.getZ() - playerPos.z();

            // angle to target (or bearing)
            float targetDeg = (m instanceof LostTalesBearingMapMarker b) ? LostTalesCompassHudRenderHelper.normalizeDegrees(b.getBearingDegree()) : LostTalesCompassHudRenderHelper.angleDegToTarget(dx, dz);

            float delta = LostTalesCompassHudRenderHelper.shortestDeltaDegrees(targetDeg, yawDeg);

            // cull by FOV (unless quest)
            if (!isQuest && Math.abs(delta) > visibleDeg / 2f) continue;

            float rawPx = centerX + (delta * pxPerDeg);
            float px = isQuest ? Mth.clamp(rawPx, minX, maxX) : rawPx;

            // edge fade (not for quests)
            float edgeT = isQuest ? 1f :
                    LostTalesCompassHudRenderHelper.edgeCenterFactor(px, centerX, halfWidth, LostTalesCompassHudRenderer.MAP_MARKER_BEGIN_EDGE_FADE_OUT_OFFSET);
            if (edgeT <= 0f) continue;

            // focus curve
            float centerDistPx = Math.abs(px - centerX);
            float emphasis = LostTalesCompassHudRenderHelper.focusEmphasis(centerDistPx, LostTalesCompassHudRenderer.MAP_MARKER_BEGIN_CENTER_FOCUS_OFFSET);

            // distance fade (not for quests)
            double distSq = dx*dx + dy*dy + dz*dz;
            float distT = 1f;
            if (!isQuest && m.getFadeInRadius() > 0f) {
                double dist = Math.sqrt(distSq);
                double R = m.getFadeInRadius();
                if (dist > R) continue;
                float t = (float)(dist / R);
                // Hermite smoothstep
                distT = 1f - (t * t * (3f - 2f * t));
            }

            float alpha = isQuest
                    ? 1f
                    : Mth.clamp(
                    LostTalesCompassHudRenderer.MAP_MARKER_DISTANCE_FADE_IN_FLOOR_ALPHA
                            + (1f - LostTalesCompassHudRenderer.MAP_MARKER_DISTANCE_FADE_IN_FLOOR_ALPHA) * distT,
                    0f, 1f) * edgeT;

            if (alpha <= 0f) continue;

            int argb = (isQuest ? LostTalesColor.WHITE : m.getColor()).getColorWithAlpha(alpha);

            renderItems.add(new LostTalesMapMarkerRenderItem(
                    m,
                    px,
                    1f,
                    alpha,
                    argb,
                    distSq,
                    dy,
                    isQuest,
                    emphasis
            ));

            if (m.isScaleWithCenterFocus()) {
                boolean better = emphasis > bestEmphasis
                        || (emphasis == bestEmphasis && Math.abs(px - centerX) < Math.abs(focusedPx - centerX));
                if (better) {
                    bestEmphasis = emphasis;
                    focused = m;
                    focusedPx = px;
                    bestDx = dx; bestDy = dy; bestDz = dz;
                }
            }
        }

        if (renderItems.isEmpty()) return renderItems;

        // sort: distance DESC, alpha ASC, farther from center first (so centered ends on top when drawing later)
        renderItems.sort((a, b) -> {
            int c = Double.compare(b.distSq(), a.distSq());
            if (c != 0) return c;
            c = Float.compare(a.alpha(), b.alpha());
            if (c != 0) return c;
            float ac = Math.abs(a.x() - centerX);
            float bc = Math.abs(b.x() - centerX);
            return Float.compare(bc, ac);
        });

        // Ensure LostTalesBearingMapMarker always render behind everything else.
        // Because List.sort is stable, the prior distance/alpha/center ordering is preserved within each group.
        renderItems.sort((a, b) -> {
            boolean aBearing = a.marker() instanceof LostTalesBearingMapMarker;
            boolean bBearing = b.marker() instanceof LostTalesBearingMapMarker;
            // bearing first => drawn first => behind others
            return -Boolean.compare(aBearing, bBearing);
        });

        // Tag the focused marker in the batch (optional, we infer by identity)
        LostTalesMapMarkerRenderPass.setFocusContext(focused, bestEmphasis, focusedPx, bestDx, bestDy, bestDz);

        return renderItems;
    }
}
