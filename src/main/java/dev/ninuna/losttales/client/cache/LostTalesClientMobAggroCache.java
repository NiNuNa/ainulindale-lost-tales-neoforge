package dev.ninuna.losttales.client.cache;

import it.unimi.dsi.fastutil.ints.Int2LongOpenHashMap;
import net.minecraft.client.Minecraft;

import java.util.Collection;

public class LostTalesClientMobAggroCache {
    private static final Int2LongOpenHashMap LOCKED_UNTIL = new Int2LongOpenHashMap();
    private static final int TTL_TICKS = 20; // ~1s

    public static void accept(Collection<Integer> mobIds) {
        long now = tick();
        long until = now + TTL_TICKS;
        for (int id : mobIds) LOCKED_UNTIL.put(id, until);
        // prune expired
        LOCKED_UNTIL.int2LongEntrySet().removeIf(e -> e.getLongValue() <= now);
    }

    public static boolean mobIsAggro(int mobId) {
        long now = tick();
        long until = LOCKED_UNTIL.getOrDefault(mobId, 0L);
        if (until <= now) {
            if (until != 0L) LOCKED_UNTIL.remove(mobId);
            return false;
        }
        return true;
    }

    private static long tick() {
        var mc = Minecraft.getInstance();
        return mc.level != null ? mc.level.getGameTime() : 0L;
    }
}
