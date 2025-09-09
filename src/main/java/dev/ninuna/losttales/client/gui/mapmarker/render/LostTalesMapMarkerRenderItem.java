package dev.ninuna.losttales.client.gui.mapmarker.render;

import dev.ninuna.losttales.client.gui.mapmarker.custom.LostTalesPositionMapMarker;

public record LostTalesMapMarkerRenderItem (
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
