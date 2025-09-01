package dev.ninuna.losttales.client.gui.mapicon.provider;

import net.minecraft.client.Minecraft;
import dev.ninuna.losttales.client.gui.LostTalesGuiHelper;
import dev.ninuna.losttales.client.gui.mapicon.LostTalesMapIcon;

import java.util.List;

public class LostTalesVillageMapIconProvider implements LostTalesMapIconProvider {
    private static final float VILLAGE_SCAN_RADIUS = 0.0f;

    @Override
    public List<LostTalesMapIcon> collect(Minecraft mc) {
        if (mc.level == null || mc.player == null) return List.of();

        // however you discover villages/POIs:
        // for demo, pretend we have absolute world positions (wx, wz)
        double wx = mc.player.getX() + 80;
        double wz = mc.player.getZ() - 20;
        double wy = mc.player.getY() - 15;

        return List.of(new LostTalesMapIcon(
                "Village",
                LostTalesGuiHelper.COLOR_WHITE_FADE,
                true, true, false,
                VILLAGE_SCAN_RADIUS,         /* distance fade radius -> 0 = off */
                wx, wz, wy,
                null, null, null,
                0, 0
        ));
    }
}
