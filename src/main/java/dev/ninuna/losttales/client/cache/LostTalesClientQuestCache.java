package dev.ninuna.losttales.client.cache;

import dev.ninuna.losttales.common.quest.LostTalesQuestPlayerData;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LostTalesClientQuestCache {
    private static final LostTalesClientQuestCache INSTANCE = new LostTalesClientQuestCache();

    private final Map<ResourceLocation, LostTalesQuestPlayerData.QuestProgress> activeQuests = new HashMap<>();

    public static LostTalesClientQuestCache getInstance() {
        return INSTANCE;
    }

    public synchronized Map<ResourceLocation, LostTalesQuestPlayerData.QuestProgress> getActiveQuests() {
        return Collections.unmodifiableMap(this.activeQuests);
    }

    public synchronized void replaceActiveQuests(Map<ResourceLocation, LostTalesQuestPlayerData.QuestProgress> newActiveQuests) {
        this.activeQuests.clear();
        if (newActiveQuests != null) activeQuests.putAll(newActiveQuests);
    }

    public synchronized void clear() {
        this.activeQuests.clear();
    }
}
