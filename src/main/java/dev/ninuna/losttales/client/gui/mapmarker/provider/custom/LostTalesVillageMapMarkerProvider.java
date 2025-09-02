package dev.ninuna.losttales.client.gui.mapmarker.provider.custom;

import dev.ninuna.losttales.client.gui.mapmarker.provider.LostTalesMapMarkerProvider;
import net.minecraft.client.Minecraft;
import dev.ninuna.losttales.client.gui.LostTalesGuiHelper;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarker;

import java.util.List;
import java.util.UUID;

public class LostTalesVillageMapMarkerProvider implements LostTalesMapMarkerProvider {
    private static final float VILLAGE_SCAN_RADIUS = 0.0f;

    @Override
    public List<LostTalesMapMarker> collectMapMarkers(Minecraft minecraft) {
        if (minecraft.level == null || minecraft.player == null) return List.of();

        // however you discover villages/POIs:
        // for demo, pretend we have absolute world positions (wx, wz)
        double wx = minecraft.player.getX() + 80;
        double wz = minecraft.player.getZ() - 20;
        double wy = minecraft.player.getY() - 15;

        return List.of(new LostTalesMapMarker(
                UUID.randomUUID(), LostTalesMapMarker.getMapMarkerName("village"), LostTalesGuiHelper.GuiColor.WHITE.getColorRgb(), minecraft.player.level().dimension(),
                true, true, false,
                VILLAGE_SCAN_RADIUS,         /* distance fade radius -> 0 = off */
                wx, wz, wy,
                0, 0
        ));
    }
}
