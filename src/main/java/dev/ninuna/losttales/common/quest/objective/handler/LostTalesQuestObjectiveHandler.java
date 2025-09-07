package dev.ninuna.losttales.common.quest.objective.handler;

import dev.ninuna.losttales.common.quest.LostTalesQuest;
import dev.ninuna.losttales.common.quest.objective.LostTalesQuestObjective;
import net.minecraft.server.level.ServerPlayer;

public interface LostTalesQuestObjectiveHandler {
    String type();

    default void validate(LostTalesQuest quest, LostTalesQuestObjective obj) {}
    default void onEntityKilled(ServerPlayer killer, LostTalesQuest quest, LostTalesQuestObjective obj, net.minecraft.world.entity.Entity victim) {}
    default void onPlayerTick(ServerPlayer player, LostTalesQuest quest, LostTalesQuestObjective obj) {}
}
