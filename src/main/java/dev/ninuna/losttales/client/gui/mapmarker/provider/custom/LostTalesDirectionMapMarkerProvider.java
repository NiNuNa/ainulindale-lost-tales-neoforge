package dev.ninuna.losttales.client.gui.mapmarker.provider.custom;

import dev.ninuna.losttales.client.gui.LostTalesColor;
import dev.ninuna.losttales.client.gui.mapmarker.custom.LostTalesBearingMapMarker;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarkerIcon;
import dev.ninuna.losttales.client.gui.mapmarker.provider.LostTalesMapMarkerProvider;
import dev.ninuna.losttales.common.LostTales;
import net.minecraft.client.Minecraft;
import dev.ninuna.losttales.client.gui.mapmarker.custom.LostTalesPositionMapMarker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LostTalesDirectionMapMarkerProvider implements LostTalesMapMarkerProvider {

    private enum PrincipalDirection {
        N   ("north",      180f,   LostTalesMapMarkerIcon.N),
        NE  ("northEast",  225f,   LostTalesMapMarkerIcon.NE),
        E   ("east",       270f,   LostTalesMapMarkerIcon.E),
        SE  ("southEast",  315f,   LostTalesMapMarkerIcon.SE),
        S   ("south",      0f,     LostTalesMapMarkerIcon.S),
        SW  ("southWest",  45f,    LostTalesMapMarkerIcon.SW),
        W   ("west",       90f,    LostTalesMapMarkerIcon.W),
        NW  ("northWest",  135f,   LostTalesMapMarkerIcon.NW);

        private final float bearingDegree;
        private final LostTalesMapMarkerIcon mapMarkerIcon;
        private final UUID uuid;

        PrincipalDirection(String key, float angleDeg, LostTalesMapMarkerIcon mapMarkerIcon) {
            this.bearingDegree = angleDeg;
            this.mapMarkerIcon = mapMarkerIcon;
            this.uuid = UUID.nameUUIDFromBytes((LostTales.MOD_ID + ":direction:" + key).getBytes());
        }
    }

    @Override
    public List<LostTalesPositionMapMarker> collectMapMarkers(Minecraft minecraft) {
        var level = minecraft.level;
        if (level == null) return List.of();

        var mapMarkers = new ArrayList<LostTalesPositionMapMarker>(PrincipalDirection.values().length);
        for (var principalDirection : PrincipalDirection.values()) {
            mapMarkers.add(new LostTalesBearingMapMarker(principalDirection.uuid, null, principalDirection.mapMarkerIcon, LostTalesColor.WHITE, principalDirection.bearingDegree));
        }
        return mapMarkers;
    }
}
