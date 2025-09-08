package dev.ninuna.losttales.common.quest.objective.handler;

import dev.ninuna.losttales.common.quest.LostTalesQuest;
import dev.ninuna.losttales.common.quest.objective.LostTalesQuestObjective;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public interface LostTalesQuestObjectiveHandler {
    String questObjectiveType();

    default void validateQuestObjective(LostTalesQuest quest, LostTalesQuestObjective objective) {}

    default void onEntityKilled(ServerPlayer killer, LostTalesQuest quest, LostTalesQuestObjective objective, Entity victim) {}

    default void onPlayerTick(ServerPlayer serverPlayer, LostTalesQuest quest, LostTalesQuestObjective objective) {}

    default void onItemCrafted(ServerPlayer serverPlayer, LostTalesQuest quest, LostTalesQuestObjective objective, ItemStack crafted) {}

    default void onItemPickedUp(ServerPlayer serverPlayer, LostTalesQuest quest, LostTalesQuestObjective objective, ItemStack pickedUp) {}
}
