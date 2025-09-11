package dev.ninuna.losttales.client.cache;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LostTalesClientQuickLootCache {
    private static final Map<BlockPos, Entry> CACHE = new HashMap<>();
    private static final long TTL_MS = 250; // tune this

    public static void put(BlockPos pos, Component title, List<ItemStack> items) {
        CACHE.put(pos, new Entry(title, List.copyOf(items), System.currentTimeMillis()));
    }

    public static Entry getIfFresh(BlockPos pos) {
        Entry e = CACHE.get(pos);
        if (e == null) return null;
        if (System.currentTimeMillis() - e.timeMs > TTL_MS) return null;
        return e;
    }

    public record Entry(Component title, List<ItemStack> items, long timeMs) {
    }
}