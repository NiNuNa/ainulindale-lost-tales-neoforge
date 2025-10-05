package dev.ninuna.losttales.client.gui.hud.compass.mapmarker;

import dev.ninuna.losttales.client.gui.mapmarker.custom.LostTalesPositionMapMarker;

public record LostTalesCompassHudMapMarkerRenderItem(
        LostTalesPositionMapMarker marker,
        float x,
        float scale,
        float alpha,
        int color,
        double distSq,
        double dy,
        boolean activeQuest,
        float emphasis
) {}
