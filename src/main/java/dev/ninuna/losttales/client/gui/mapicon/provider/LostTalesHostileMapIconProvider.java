package dev.ninuna.losttales.client.gui.mapicon.provider;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.phys.AABB;
import dev.ninuna.losttales.client.gui.LostTalesGuiHelper;
import dev.ninuna.losttales.client.gui.mapicon.LostTalesMapIcon;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LostTalesHostileMapIconProvider implements LostTalesMapIconProvider {
    private static final float HOSTILE_SCAN_RADIUS = 32.0f;

    @Override
    public List<LostTalesMapIcon> collect(Minecraft mc) {
        if (mc.level == null || mc.player == null) return Collections.emptyList();

        AABB box = mc.player.getBoundingBox().inflate(HOSTILE_SCAN_RADIUS);
        List<Entity> list = mc.level.getEntities(mc.player, box, e -> e instanceof Enemy);

        if (list.isEmpty()) return Collections.emptyList();

        List<LostTalesMapIcon> out = new ArrayList<>(list.size());
        for (Entity e : list) {
            if (!(e instanceof LivingEntity le) || !le.isAlive()) continue;

            double dx = e.getX() - mc.player.getX();
            double dz = e.getZ() - mc.player.getZ();
            double dy = e.getY() - mc.player.getY();


            LostTalesMapIcon icon = new LostTalesMapIcon(
                    e.getDisplayName().getString(),
                    LostTalesGuiHelper.COLOR_WHITE_FADE,
                    true, true, true,
                    HOSTILE_SCAN_RADIUS,
                    null, null, null,
                    dx, dz, dy,
                    11, 0
            );
            out.add(icon);
        }
        return out;
    }
}
