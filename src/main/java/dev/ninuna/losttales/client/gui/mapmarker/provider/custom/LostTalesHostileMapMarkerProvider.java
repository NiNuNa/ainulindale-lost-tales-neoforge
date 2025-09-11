package dev.ninuna.losttales.client.gui.mapmarker.provider.custom;

import dev.ninuna.losttales.client.gui.LostTalesColor;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarkerIcon;
import dev.ninuna.losttales.client.gui.mapmarker.custom.LostTalesPositionMapMarker;
import dev.ninuna.losttales.client.gui.mapmarker.provider.LostTalesMapMarkerProvider;
import dev.ninuna.losttales.common.event.LostTalesMobAggroTracker;
import dev.ninuna.losttales.client.cache.LostTalesClientMobAggroCache;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LostTalesHostileMapMarkerProvider implements LostTalesMapMarkerProvider {

    @Override
    public List<LostTalesPositionMapMarker> collectMapMarkers(Minecraft minecraft) {
        if (minecraft.level == null || minecraft.player == null) return Collections.emptyList();

        Player player = minecraft.player;
        AABB box = player.getBoundingBox().inflate(LostTalesMobAggroTracker.AGGRO_MOB_SCAN_RADIUS);

        List<Entity> nearby = minecraft.level.getEntities(player, box, entity -> entity instanceof Mob mob && mob.isAlive());
        if (nearby.isEmpty()) return Collections.emptyList();

        List<LostTalesPositionMapMarker> mapMarkers = new ArrayList<>(nearby.size());
        for (Entity entity : nearby) {
            // Only render if the server says this entity is "locked on" the player
            if (!LostTalesClientMobAggroCache.mobIsAggro(entity.getId())) continue;

            // Build your marker (adjust args to match your constructor if it differs)
            LostTalesPositionMapMarker marker = new LostTalesPositionMapMarker(
                    entity.getUUID(),
                    entity.getDisplayName(),
                    LostTalesMapMarkerIcon.HOSTILE,
                    LostTalesColor.WHITE,
                    entity.level().dimension(),
                    true, true, false,
                    (float) LostTalesMobAggroTracker.AGGRO_MOB_SCAN_RADIUS, 0,
                    entity.getX(), entity.getY(), entity.getZ()
            );
            mapMarkers.add(marker);
        }
        return mapMarkers;
    }
}
