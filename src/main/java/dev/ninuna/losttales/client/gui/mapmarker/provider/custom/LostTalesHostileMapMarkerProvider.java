package dev.ninuna.losttales.client.gui.mapmarker.provider.custom;

import dev.ninuna.losttales.client.gui.LostTalesGuiColor;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarkerIcon;
import dev.ninuna.losttales.client.gui.mapmarker.provider.LostTalesMapMarkerProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.phys.AABB;
import dev.ninuna.losttales.client.gui.LostTalesGuiHelper;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LostTalesHostileMapMarkerProvider implements LostTalesMapMarkerProvider {
    private static final float HOSTILE_SCAN_RADIUS = 32.0f;

    @Override
    public List<LostTalesMapMarker> collectMapMarkers(Minecraft minecraft) {
        if (minecraft.level == null || minecraft.player == null) return Collections.emptyList();

        AABB box = minecraft.player.getBoundingBox().inflate(HOSTILE_SCAN_RADIUS);
        List<Entity> list = minecraft.level.getEntities(minecraft.player, box, e -> e instanceof Enemy);

        if (list.isEmpty()) return Collections.emptyList();

        List<LostTalesMapMarker> mapMarkers = new ArrayList<>(list.size());
        for (Entity entity : list) {
            if (!(entity instanceof LivingEntity le) || !le.isAlive()) continue;

            double dx = entity.getX();
            double dz = entity.getZ();
            double dy = entity.getY();

            LostTalesMapMarker mapMarker = new LostTalesMapMarker(
                    entity.getUUID(), entity.getDisplayName(), LostTalesMapMarkerIcon.HOSTILE ,LostTalesGuiColor.WHITE,
                    entity.level().dimension(),
                    true, true, true,
                    HOSTILE_SCAN_RADIUS, 0,
                    dx, dz, dy
            );
            mapMarkers.add(mapMarker);
        }
        return mapMarkers;
    }
}
