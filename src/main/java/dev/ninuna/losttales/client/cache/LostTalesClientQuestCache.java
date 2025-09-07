package dev.ninuna.losttales.client.cache;

import dev.ninuna.losttales.common.quest.LostTalesQuestPlayerData;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LostTalesClientQuestCache {
    private static final LostTalesClientQuestCache INSTANCE = new LostTalesClientQuestCache();
    private final Map<ResourceLocation, LostTalesQuestPlayerData.QuestProgress> active = new HashMap<>();

    public static LostTalesClientQuestCache get() {
        return INSTANCE;
    }

    public synchronized Map<ResourceLocation, LostTalesQuestPlayerData.QuestProgress> getActive() {
        return Collections.unmodifiableMap(active);
    }

    public synchronized void replaceAll(Map<ResourceLocation, LostTalesQuestPlayerData.QuestProgress> newActive) {
        active.clear();
        if (newActive != null) active.putAll(newActive);
    }

    public synchronized void clear() {
        active.clear();
    }
}
