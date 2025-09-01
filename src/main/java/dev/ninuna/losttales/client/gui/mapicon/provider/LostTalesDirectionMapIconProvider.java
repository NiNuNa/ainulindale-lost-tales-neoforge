package dev.ninuna.losttales.client.gui.mapicon.provider;

import net.minecraft.client.Minecraft;
import dev.ninuna.losttales.client.gui.LostTalesGuiHelper;
import dev.ninuna.losttales.client.gui.mapicon.LostTalesMapIcon;

import java.util.ArrayList;
import java.util.List;

public class LostTalesDirectionMapIconProvider implements LostTalesMapIconProvider {
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
    public List<LostTalesMapIcon> collect(Minecraft mc) {
        List<LostTalesMapIcon> out = new ArrayList<>(Direction.values().length);

        for (Direction d : Direction.values()) {
            out.add(LostTalesMapIcon.mapIconWithPreDefinedAngle(
                    d.fullName,
                    LostTalesGuiHelper.COLOR_WHITE_FADE,
                    d.u, d.v,
                    d.angleDeg
            ));
        }
        return out;
    }
}
