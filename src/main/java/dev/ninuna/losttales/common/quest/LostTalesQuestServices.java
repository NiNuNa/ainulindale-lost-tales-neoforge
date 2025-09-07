package dev.ninuna.losttales.common.quest;

import dev.ninuna.losttales.common.datapack.loader.LostTalesQuestDatapackLoader;

public class LostTalesQuestServices {
    private static LostTalesQuestDatapackLoader QUESTS;

    public static void set(LostTalesQuestDatapackLoader loader) {
        QUESTS = loader;
    }

    public static LostTalesQuestDatapackLoader quests() {
        return QUESTS;
    }
}
