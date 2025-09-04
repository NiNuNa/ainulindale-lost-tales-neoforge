package dev.ninuna.losttales.client.gui.mapmarker.provider.custom;

import dev.ninuna.losttales.client.gui.LostTalesGuiColor;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarkerIcon;
import dev.ninuna.losttales.client.gui.mapmarker.provider.LostTalesMapMarkerProvider;
import net.minecraft.client.Minecraft;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarker;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LostTalesDirectionMapMarkerProvider implements LostTalesMapMarkerProvider {

    private enum PrincipalDirection {
        N   (LostTalesMapMarker.getMapMarkerName("north"),      180f,   LostTalesMapMarkerIcon.N),
        NE  (LostTalesMapMarker.getMapMarkerName("northEast"),  225f,   LostTalesMapMarkerIcon.NE),
        E   (LostTalesMapMarker.getMapMarkerName("east"),       270f,   LostTalesMapMarkerIcon.E),
        SE  (LostTalesMapMarker.getMapMarkerName("southEast"),  315f,   LostTalesMapMarkerIcon.SE),
        S   (LostTalesMapMarker.getMapMarkerName("south"),      0f,     LostTalesMapMarkerIcon.S),
        SW  (LostTalesMapMarker.getMapMarkerName("southWest"),  45f,    LostTalesMapMarkerIcon.SW),
        W   (LostTalesMapMarker.getMapMarkerName("west"),       90f,    LostTalesMapMarkerIcon.W),
        NW  (LostTalesMapMarker.getMapMarkerName("northWest"),  135f,   LostTalesMapMarkerIcon.NW);

        private final Component name;
        private final float angleDeg;
        private final LostTalesMapMarkerIcon mapMarkerIcon;

        PrincipalDirection(Component name, float angleDeg, LostTalesMapMarkerIcon mapMarkerIcon) {
            this.name = name;
            this.angleDeg = angleDeg;
            this.mapMarkerIcon = mapMarkerIcon;
        }

        public LostTalesMapMarkerIcon getMapMarkerIcon() {
            return mapMarkerIcon;
        }
    }

    @Override
    public List<LostTalesMapMarker> collectMapMarkers(Minecraft minecraft) {
        List<LostTalesMapMarker> mapMarkers = new ArrayList<>(PrincipalDirection.values().length);

        for (PrincipalDirection principalDirection : PrincipalDirection.values()) {
            double rad = Math.toRadians(principalDirection.angleDeg);
            double x = -Math.sin(rad);
            double z =  Math.cos(rad);

            mapMarkers.add(new LostTalesMapMarker(
                    UUID.randomUUID(), principalDirection.name, principalDirection.getMapMarkerIcon(), LostTalesGuiColor.WHITE,
                    minecraft.level.dimension(),
                    false, false, false,
                    0,
                    x + minecraft.player.getX() , z + minecraft.player.getZ(), minecraft.player.getY()
            ));
        }
        return mapMarkers;
    }
}
