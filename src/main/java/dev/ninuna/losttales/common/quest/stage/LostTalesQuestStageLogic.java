package dev.ninuna.losttales.common.quest.stage;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.attachment.LostTalesAttachments;
import dev.ninuna.losttales.common.quest.LostTalesQuest;
import dev.ninuna.losttales.common.quest.LostTalesQuestManager;
import net.minecraft.server.level.ServerPlayer;

public class LostTalesQuestStageLogic {

    public static void evaluateStage(ServerPlayer serverPlayer, LostTalesQuest quest) {
        var questPlayerData = serverPlayer.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        var qpOpt = questPlayerData.getActiveQuest(quest.id());
        if (qpOpt.isEmpty()) return;
        var qp = qpOpt.get();

        var stage = quest.stages().stream()
                .filter(s -> s.id().equals(qp.stageId))
                .findFirst().orElse(null);
        if (stage == null) return;

        boolean allDone = true;
        for (var obj : stage.objectives()) {
            if (obj.optional()) continue;
            int need = parseIntSafe(obj.params().get("count"), 1);
            int have = qp.objectiveProgress.getOrDefault(obj.id(), 0);
            if (have < need) { allDone = false; break; }
        }

        if (!allDone) return;

        // advance
        int idx = quest.stages().indexOf(stage);
        if (idx >= 0 && idx + 1 < quest.stages().size()) {
            questPlayerData.setStage(quest.id(), quest.stages().get(idx + 1).id());
        } else {
            // Last Stage: Quest Completed
            questPlayerData.completeQuest(quest.id());
            LostTales.LOGGER.info("Completed Quest: " + quest.title());
        }
        LostTalesQuestManager.syncQuestDataToPlayer(serverPlayer);
    }

    private static int parseIntSafe(String s, int dft) {
        try {return Integer.parseInt(s); } catch (Exception e) { return dft; }
    }
}
