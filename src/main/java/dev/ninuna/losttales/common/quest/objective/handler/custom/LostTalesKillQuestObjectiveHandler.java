package dev.ninuna.losttales.common.quest.objective.handler.custom;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.attachment.LostTalesAttachments;
import dev.ninuna.losttales.common.quest.LostTalesQuest;
import dev.ninuna.losttales.common.quest.LostTalesQuestManager;
import dev.ninuna.losttales.common.quest.objective.LostTalesQuestObjective;
import dev.ninuna.losttales.common.quest.objective.handler.LostTalesQuestObjectiveHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.Map;

public class LostTalesKillQuestObjectiveHandler implements LostTalesQuestObjectiveHandler {

    @Override
    public String questObjectiveType() {
        return "kill";
    }

    @Override
    public void validateQuestObjective(LostTalesQuest quest, LostTalesQuestObjective objective) {
        Map<String, String> p = objective.params();
        boolean hasEntity = p.containsKey("entity");
        boolean hasTag = p.containsKey("tag");
        if (!hasEntity && !hasTag) {
            LostTales.LOGGER.warn("[{}] Objective {} in quest {} missing 'entity' or 'tag' parameter for type 'kill'",
                    LostTales.MOD_ID, objective.id(), quest.id());
        }
        if (p.containsKey("count")) {
            try { Integer.parseInt(p.get("count")); }
            catch (Exception e) {
                LostTales.LOGGER.warn("[{}] Objective {} in quest {} has non-integer 'count': {}",
                        LostTales.MOD_ID, objective.id(), quest.id(), p.get("count"));
            }
        }
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
            LostTalesQuestManager.evaluateStageProgressForPlayer(quest.id(), killer);
            LostTales.LOGGER.info("[{}] KILL progress {} -> {}/{}", LostTales.MOD_ID, obj.id(), now, target);
        }
    }
}
