package dev.ninuna.losttales.common.quest.objective.handler.custom;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.attachment.LostTalesAttachments;
import dev.ninuna.losttales.common.quest.LostTalesQuest;
import dev.ninuna.losttales.common.quest.stage.LostTalesQuestStageLogic;
import dev.ninuna.losttales.common.quest.objective.LostTalesQuestObjective;
import dev.ninuna.losttales.common.quest.objective.handler.LostTalesQuestObjectiveHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class LostTalesGotoQuestObjectiveHandler implements LostTalesQuestObjectiveHandler {

    @Override
    public String type() {
        return "goto";
    }

    @Override
    public void onPlayerTick(ServerPlayer player, LostTalesQuest quest, LostTalesQuestObjective obj) {
        var p = obj.params();
        double x = Double.parseDouble(p.getOrDefault("x", "0"));
        double y = Double.parseDouble(p.getOrDefault("y", "0"));
        double z = Double.parseDouble(p.getOrDefault("z", "0"));
        double radius = Double.parseDouble(p.getOrDefault("radius", "3"));
        String dimStr = p.getOrDefault("dimension", "minecraft:overworld");


        ResourceKey<Level> targetDim = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(dimStr));
        ServerLevel sl = player.level();
        if (sl.dimension() != targetDim) return;
        double dx = player.getX() - x, dy = player.getY() - y, dz = player.getZ() - z;
        if (dx*dx + dy*dy + dz*dz <= radius * radius) {
            var data = player.getData(LostTalesAttachments.PLAYER_QUESTS.get());
            int before = data.addProgress(quest.id(), obj.id(), 0);
            if (before >= 1) return;

            data.setProgress(quest.id(), obj.id(), 1);
            LostTalesQuestStageLogic.evaluateStage(player, quest);

            LostTales.LOGGER.info("[{}] GOTO {} reached.", LostTales.MOD_ID, obj.id());
        }
    }
}
