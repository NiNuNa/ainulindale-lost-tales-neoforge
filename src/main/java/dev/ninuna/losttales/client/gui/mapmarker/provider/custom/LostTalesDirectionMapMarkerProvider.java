package dev.ninuna.losttales.client.gui.mapmarker.provider.custom;

import dev.ninuna.losttales.client.gui.mapmarker.provider.LostTalesMapMarkerProvider;
import net.minecraft.client.Minecraft;
import dev.ninuna.losttales.client.gui.LostTalesGuiHelper;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarker;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LostTalesDirectionMapMarkerProvider implements LostTalesMapMarkerProvider {

    private enum PrincipalDirection {
        N   (LostTalesMapMarker.getMapMarkerName("north"),      180f,   0,  0),
        NE  (LostTalesMapMarker.getMapMarkerName("northEast"),  225f,   0,  0),
        E   (LostTalesMapMarker.getMapMarkerName("east"),       270f,   0,  0),
        SE  (LostTalesMapMarker.getMapMarkerName("southEast"),  315f,   0,  0),
        S   (LostTalesMapMarker.getMapMarkerName("south"),      0f,     0,  0),
        SW  (LostTalesMapMarker.getMapMarkerName("southWest"),  45f,    0,  0),
        W   (LostTalesMapMarker.getMapMarkerName("west"),       90f,    0,  0),
        NW  (LostTalesMapMarker.getMapMarkerName("northWest"),  135f,   0,  0);

        private final Component name;
        final float angleDeg;
        final int u, v;

        PrincipalDirection(Component name, float angleDeg, int u, int v) {
            this.name = name;
            this.angleDeg = angleDeg;
            this.u = u;
            this.v = v;
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
                    UUID.randomUUID(), principalDirection.name, LostTalesGuiHelper.GuiColor.WHITE.getColorArgb(0.0f), minecraft.level.dimension(),
                    false, false, false,
                    0,
                    x + minecraft.player.getX() , z + minecraft.player.getZ(), minecraft.player.getY(),
                    principalDirection.u, principalDirection.v
            ));
        }
        return mapMarkers;
    }
}
