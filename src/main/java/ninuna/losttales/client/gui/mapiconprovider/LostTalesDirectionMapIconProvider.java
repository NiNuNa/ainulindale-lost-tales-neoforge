package ninuna.losttales.client.gui.mapiconprovider;

import net.minecraft.client.Minecraft;
import ninuna.losttales.client.gui.LostTalesCompassHud;
import ninuna.losttales.client.util.LostTalesGuiUtil;

import java.util.ArrayList;
import java.util.List;

public class LostTalesDirectionMapIconProvider implements LostTalesCompassHud.IconProvider {
    private enum Direction {
        N   ("North",180f,0,0),
        NE  ("North-East",225f,0,0),
        E   ("East",270f,0,0),
        SE  ("South-East",315f,0,0),
        S   ("South",0f,0,0),
        SW  ("South-West",45f,0,0),
        W   ("West",90f,0,0),
        NW  ("North-West",135f,0,0);

        final String fullName;
        final float angleDeg;
        final int u, v;

        Direction (String fullName, float angleDeg, int u, int v) {
            this.fullName = fullName;
            this.angleDeg = angleDeg;
            this.u = u;
            this.v = v;
        }
    }

    @Override
    public List<LostTalesCompassHud.MapIcon> collect(Minecraft mc) {
        List<LostTalesCompassHud.MapIcon> out = new ArrayList<>(Direction.values().length);

        for (Direction d : Direction.values()) {
            out.add(LostTalesCompassHud.MapIcon.mapIconAtAngle(
                    d.fullName,
                    LostTalesGuiUtil.COLOR_WHITE_FADE,
                    d.u, d.v,
                    d.angleDeg,
                    null
            ));
        }
        return out;
    }
}
