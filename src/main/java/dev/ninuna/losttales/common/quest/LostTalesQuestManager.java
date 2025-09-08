package dev.ninuna.losttales.common.quest;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.attachment.LostTalesAttachments;
import dev.ninuna.losttales.common.datapack.loader.LostTalesQuestDatapackLoader;
import dev.ninuna.losttales.common.network.packet.LostTalesSyncQuestsPacket;
import dev.ninuna.losttales.common.quest.stage.LostTalesQuestStage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Optional;

public class LostTalesQuestManager {

    /** Try to start quest for player. Returns true on success. */
    public static boolean startQuestForPlayer(ResourceLocation questId, ServerPlayer serverPlayer) {
        var questOptional = LostTalesQuestDatapackLoader.getQuest(questId);
        if (questOptional.isEmpty()) return false;

        if (!questCanBeStartedForPlayer(questId, serverPlayer)) return false;

        var data = serverPlayer.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        LostTalesQuest quest = questOptional.get();

        // start at first stage
        String firstStage = quest.stages().isEmpty() ? "" : quest.stages().getFirst().id();
        data.startQuest(questId, firstStage);

        syncQuestDataToPlayer(serverPlayer);
        return true;
    }

    /** Mark an entire quest as completed (no matter the current stage). */
    public static boolean completeQuestForPlayer(ResourceLocation questId, ServerPlayer serverPlayer) {
        LostTalesQuestPlayerData questPlayerData = serverPlayer.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        if (!questPlayerData.isQuestActive(questId)) return false;

        questPlayerData.completeQuest(questId);
        syncQuestDataToPlayer(serverPlayer);
        return true;
    }

    /** Remove a completed quest from a player and make it available again. */
    public static boolean removeCompletedQuestForPlayer(ResourceLocation questId, ServerPlayer serverPlayer) {
        LostTalesQuestPlayerData questPlayerData = serverPlayer.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        if (!questPlayerData.isQuestCompleted(questId)) return false;

        questPlayerData.removeCompletedQuest(questId);
        syncQuestDataToPlayer(serverPlayer);
        return true;
    }

    /** Force set the active stage for a quest. Returns true if changed. */
    public static boolean setQuestStageForPlayer(ResourceLocation questId, String stageId, ServerPlayer serverPlayer) {
        var questOptional = LostTalesQuestDatapackLoader.getQuest(questId);
        if (questOptional.isEmpty()) return false;

        LostTalesQuest quest = questOptional.get();
        Optional<LostTalesQuestStage> target = quest.stages().stream().filter(stage -> stage.id().equals(stageId)).findFirst();
        if (target.isEmpty()) return false;

        LostTalesQuestPlayerData questPlayerData = serverPlayer.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        if (!questPlayerData.isQuestActive(questId)) return false;

        questPlayerData.setStage(questId, stageId);
        // clear per-objective progress from previous stage when forcing a stage
        questPlayerData.clearObjectiveProgress(questId);

        syncQuestDataToPlayer(serverPlayer);
        return true;
    }

    /** Abandon quest (removes quest from active quest list). For non-repeatable quests, it remains uncompleted. */
    public static boolean abandonQuestForPlayer(ResourceLocation questId, ServerPlayer serverPlayer) {
        LostTalesQuestPlayerData questPlayerData = serverPlayer.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        if (!questPlayerData.isQuestActive(questId)) return false;

        questPlayerData.abandonQuest(questId);
        syncQuestDataToPlayer(serverPlayer);
        return true;
    }

    /** Quest can be started if it's not currently active AND if it's repeatable OR not completed yet. */
    public static boolean questCanBeStartedForPlayer(ResourceLocation questId, ServerPlayer serverPlayer) {
        LostTalesQuestPlayerData questPlayerData = serverPlayer.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        if (questPlayerData.isQuestActive(questId)) return false;

        var questOptional = LostTalesQuestDatapackLoader.getQuest(questId);
        if (questOptional.isEmpty()) return false;

        LostTalesQuest quest = questOptional.get();
        return quest.repeatable() || !questPlayerData.isQuestCompleted(questId);
    }

    /** Force a client quest data resync (call on login/respawn, or after any mutation). */
    public static void syncQuestDataToPlayer(ServerPlayer serverPlayer) {
        LostTalesQuestPlayerData questPlayerData = serverPlayer.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        var activeQuests = new ArrayList<>(questPlayerData.getActiveQuests());
        PacketDistributor.sendToPlayer(serverPlayer, new LostTalesSyncQuestsPacket(activeQuests));
    }

    /** Evaluate quest stage progress for player (advance to next stage if possible). */
    public static void evaluateStageProgressForPlayer(ResourceLocation questId, ServerPlayer serverPlayer) {
        LostTalesQuestPlayerData questPlayerData = serverPlayer.getData(LostTalesAttachments.PLAYER_QUESTS.get());

        var questProgressOptional = questPlayerData.getActiveQuest(questId);
        if (questProgressOptional.isEmpty()) return;
        LostTalesQuestPlayerData.QuestProgress questProgress = questProgressOptional.get();

        var questOptional = LostTalesQuestDatapackLoader.getQuest(questId);
        if (questOptional.isEmpty()) return;
        LostTalesQuest quest = questOptional.get();

        var stage = quest.stages().stream().filter(lostTalesQuestStage -> lostTalesQuestStage.id().equals(questProgress.stageId)).findFirst().orElse(null);
        if (stage == null) return;

        boolean allDone = true;
        for (var objective : stage.objectives()) {
            if (objective.optional()) continue;

            int need = parseIntSafe(objective.params().get("count"), 1);
            int have = questProgress.objectiveProgress.getOrDefault(objective.id(), 0);

            if (have < need) {
                allDone = false; break;
            }
        }
        if (!allDone) return;

        // Advance to next stage.
        int stageIndex = quest.stages().indexOf(stage);
        if (stageIndex >= 0 && stageIndex + 1 < quest.stages().size()) {
            questPlayerData.setStage(quest.id(), quest.stages().get(stageIndex + 1).id());
        } else {
            // Last Stage: Quest Completed
            questPlayerData.completeQuest(quest.id());
            LostTales.LOGGER.info("Completed Quest: " + quest.title());
        }
        LostTalesQuestManager.syncQuestDataToPlayer(serverPlayer);
    }

    private static int parseIntSafe(String s, int dft) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return dft;
        }
    }
}
