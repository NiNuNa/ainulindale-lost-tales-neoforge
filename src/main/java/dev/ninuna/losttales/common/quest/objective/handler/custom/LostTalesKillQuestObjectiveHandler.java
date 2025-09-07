package dev.ninuna.losttales.common.quest.objective.handler.custom;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.attachment.LostTalesAttachments;
import dev.ninuna.losttales.common.quest.LostTalesQuest;
import dev.ninuna.losttales.common.quest.stage.LostTalesQuestStageLogic;
import dev.ninuna.losttales.common.quest.objective.LostTalesQuestObjective;
import dev.ninuna.losttales.common.quest.objective.handler.LostTalesQuestObjectiveHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public class LostTalesKillQuestObjectiveHandler implements LostTalesQuestObjectiveHandler {

    @Override
    public String type() {
        return "kill";
    }

    @Override
    public void onEntityKilled(ServerPlayer killer, LostTalesQuest quest, LostTalesQuestObjective obj, Entity victim) {
        // Only count kills that match the configured entity (if provided)
        String entityStr = obj.params().getOrDefault("entity", "");
        if (!entityStr.isEmpty()) {
            try {
                ResourceLocation id = ResourceLocation.parse(entityStr);
                EntityType<?> expected = EntityType.byString(id.toString()).orElse(null);
                if (expected == null || victim.getType() != expected) {
                    return; // Not the right type
                }
            } catch (Exception e) {
                LostTales.LOGGER.warn("[{}] Invalid entity id '{}' in quest {} objective {}", LostTales.MOD_ID, entityStr, quest.id(), obj.id());
                return;
            }
        }

        String target = obj.params().getOrDefault("count", "1");
        int targetCount = Integer.parseInt(target);

        var data = killer.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        // don't exceed target to reduce spam; still evaluate
        int before = data.addProgress(quest.id(), obj.id(), 0);
        if (before < targetCount) {
            int now = data.addProgress(quest.id(), obj.id(), 1);
            LostTalesQuestStageLogic.evaluateStage(killer, quest);
            LostTales.LOGGER.info("[{}] KILL progress {} -> {}/{}", LostTales.MOD_ID, obj.id(), now, target);
        }
    }
}
