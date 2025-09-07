package dev.ninuna.losttales.common.quest;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.attachment.LostTalesAttachments;
import dev.ninuna.losttales.common.network.packet.LostTalesSyncQuestsPacket;
import dev.ninuna.losttales.common.quest.objective.handler.LostTalesQuestObjectiveHandlers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;

@EventBusSubscriber(modid = LostTales.MOD_ID)
public class LostTalesQuestEvents {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof ServerPlayer serverPlayer)) return;
        Entity victim = event.getEntity();

        var questData = serverPlayer.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        for (var questProgress : questData.getActiveQuests()) {
            var questOpt = LostTalesQuestServices.quests().getQuest(questProgress.questId);
            if (questOpt.isEmpty()) continue;
            var quest = questOpt.get();

            // Current stage objectives only
            var stage = quest.stages().stream()
                    .filter(s -> s.id().equals(questProgress.stageId))
                    .findFirst().orElse(null);
            if (stage == null) continue;

            for (var obj : stage.objectives()) {
                LostTalesQuestObjectiveHandlers.get(obj.type()).ifPresent(handler -> {
                    handler.onEntityKilled(serverPlayer, quest, obj, victim);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        var data = player.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        for (var qp : data.getActiveQuests()) {
            var questOpt = LostTalesQuestServices.quests().getQuest(qp.questId);
            if (questOpt.isEmpty()) continue;
            var quest = questOpt.get();

            var stage = quest.stages().stream()
                    .filter(s -> s.id().equals(qp.stageId))
                    .findFirst().orElse(null);
            if (stage == null) continue;

            for (var obj : stage.objectives()) {
                LostTalesQuestObjectiveHandlers.get(obj.type()).ifPresent(handler -> {
                    handler.onPlayerTick(player, quest, obj);
                });
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            syncToPlayer(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            syncToPlayer(serverPlayer);
        }
    }

    public static void syncToPlayer(ServerPlayer player) {
        var data = player.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        var list = new ArrayList<>(data.getActiveQuests());
        PacketDistributor.sendToPlayer(player, new LostTalesSyncQuestsPacket(list));
    }
}
