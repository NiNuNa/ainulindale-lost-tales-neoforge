package dev.ninuna.losttales.common.quest.stage;

import dev.ninuna.losttales.common.attachment.LostTalesAttachments;
import dev.ninuna.losttales.common.quest.LostTalesQuest;
import net.minecraft.server.level.ServerPlayer;

public class LostTalesQuestStageLogic {

    public static void evaluateStage(ServerPlayer player, LostTalesQuest quest) {
        var data = player.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        var qpOpt = data.getQuest(quest.id());
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
            data.setStage(quest.id(), quest.stages().get(idx + 1).id());
        } else {
            // last stage
            data.completeQuest(quest.id());
        }
    }

    private static int parseIntSafe(String s, int dft) {
        try { return Integer.parseInt(s); } catch (Exception e) { return dft; }
    }
}
