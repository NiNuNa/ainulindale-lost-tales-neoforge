package ninuna.losttales.client.gui.mapiconprovider;

import net.minecraft.client.Minecraft;
import ninuna.losttales.client.gui.LostTalesCompassHud;
import ninuna.losttales.client.util.LostTalesGuiUtil;

import java.util.List;

public class LostTalesVillageMapIconProvider implements LostTalesCompassHud.IconProvider {
    private static final float VILLAGE_SCAN_RADIUS = 0.0f;

    @Override
    public List<LostTalesCompassHud.MapIcon> collect(Minecraft mc) {
        if (mc.level == null || mc.player == null) return List.of();

        // however you discover villages/POIs:
        // for demo, pretend we have absolute world positions (wx, wz)
        double wx = mc.player.getX() + 80;
        double wz = mc.player.getZ() - 20;
        double wy = mc.player.getY() - 0;

        return List.of(new LostTalesCompassHud.MapIcon(
                "Village",
                LostTalesGuiUtil.COLOR_WHITE_FADE,
                true, true,
                VILLAGE_SCAN_RADIUS,         /* distance fade radius -> 0 = off */
                wx, wz, wy,
                null, null, null,
                0, 0,
                null
        ));
    }
}
