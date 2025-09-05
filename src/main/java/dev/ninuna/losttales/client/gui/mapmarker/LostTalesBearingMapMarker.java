package dev.ninuna.losttales.client.gui.mapmarker;

import dev.ninuna.losttales.client.gui.LostTalesGuiColor;

import java.util.UUID;

public class LostTalesBearingMapMarker extends LostTalesPositionMapMarker {
    private final float bearingDegree;

    public LostTalesBearingMapMarker(UUID id, String name, LostTalesMapMarkerIcon icon, LostTalesGuiColor color, float bearingDeg) {
        super(id, name, icon, color, null, false, false, false, 0, 0, 0.0, 0.0, 0.0);
        this.bearingDegree = bearingDeg;
    }

    public float getBearingDegree() {
        return bearingDegree;
    }
}
