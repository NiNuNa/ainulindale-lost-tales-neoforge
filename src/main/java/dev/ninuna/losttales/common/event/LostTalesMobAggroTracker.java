package dev.ninuna.losttales.common.event;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.network.packet.LostTalesLockedOnTargetPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@EventBusSubscriber(modid = LostTales.MOD_ID)
public class LostTalesMobAggroTracker {
    public static final double AGGRO_MOB_SCAN_RADIUS = 48.0;
    private static final int PERIOD_TICKS = 10;

    private LostTalesMobAggroTracker() {}

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer sp)) return;
        if (!(sp.level() instanceof ServerLevel level)) return;
        if (sp.tickCount % PERIOD_TICKS != 0) return;

        AABB box = sp.getBoundingBox().inflate(AGGRO_MOB_SCAN_RADIUS);
        List<Integer> ids = new ArrayList<>(32);

        for (Entity ent : level.getEntities(sp, box, en -> en instanceof Mob)) {
            Mob mob = (Mob) ent;
            boolean locked = mob.getTarget() == sp;

            if (!locked && mob instanceof NeutralMob neutral) {
                UUID anger = neutral.getPersistentAngerTarget();
                if ((anger != null && anger.equals(sp.getUUID())) || neutral.isAngryAt(sp, level)) {
                    locked = true;
                }
            }

            if (locked) ids.add(mob.getId());
        }

        PacketDistributor.sendToPlayer(sp, new LostTalesLockedOnTargetPacket(ids));
    }
}
