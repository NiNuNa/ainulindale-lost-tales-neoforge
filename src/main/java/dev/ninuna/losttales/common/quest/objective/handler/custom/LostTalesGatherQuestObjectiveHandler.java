package dev.ninuna.losttales.common.quest.objective.handler.custom;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.attachment.LostTalesAttachments;
import dev.ninuna.losttales.common.quest.LostTalesQuest;
import dev.ninuna.losttales.common.quest.LostTalesQuestManager;
import dev.ninuna.losttales.common.quest.objective.LostTalesQuestObjective;
import dev.ninuna.losttales.common.quest.objective.handler.LostTalesQuestObjectiveHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

public class LostTalesGatherQuestObjectiveHandler implements LostTalesQuestObjectiveHandler {

    @Override
    public String questObjectiveType() {
        return "gather";
    }

    @Override
    public void validateQuestObjective(LostTalesQuest quest, LostTalesQuestObjective objective) {
        objective.params().getOrDefault("item", "");
        parseIntSafe(objective.params().getOrDefault("count", "1"), 1);
    }

    @Override
    public void onItemPickedUp(ServerPlayer serverPlayer, LostTalesQuest quest, LostTalesQuestObjective objective, ItemStack pickedUp) {
        if (pickedUp == null || pickedUp.isEmpty()) return;

        // item can be "namespace:item", "#namespace:tag", or comma-separated list of either.
        String itemSpec = objective.params().getOrDefault("item", "");
        if (itemSpec.isBlank()) return;

        int targetCount = parseIntSafe(objective.params().getOrDefault("count", "1"), 1);

        if (!matches(pickedUp, itemSpec)) return;

        var data = serverPlayer.getData(LostTalesAttachments.PLAYER_QUESTS.get());

        // Don't exceed target to reduce spam; still evaluate for completion.
        int before = data.addProgress(quest.id(), objective.id(), 0);
        if (before < targetCount) {
            int now = data.addProgress(quest.id(), objective.id(), Math.min(pickedUp.getCount(), targetCount - before));
            LostTalesQuestManager.evaluateStageProgressForPlayer(quest.id(), serverPlayer);
            LostTales.LOGGER.info("[{}] GATHER progress {} -> {}/{}", LostTales.MOD_ID, objective.id(), now, targetCount);
        }
    }

    private static boolean matches(ItemStack stack, String itemOrTagCsv) {
        return Arrays.stream(itemOrTagCsv.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .anyMatch(spec -> {
                    if (spec.startsWith("#")) {
                        String clean = spec.substring(1);
                        ResourceLocation tagId = ResourceLocation.tryParse(clean);
                        if (tagId == null) return false;
                        TagKey<Item> tag = TagKey.create(Registries.ITEM, tagId);
                        return stack.is(tag);
                    } else {
                        ResourceLocation id = ResourceLocation.tryParse(spec);
                        if (id == null) return false;
                        Item target = BuiltInRegistries.ITEM.get(id).get().value();
                        return stack.is(target);
                    }
                });
    }

    private static int parseIntSafe(String s, int dft) {
        try { return Integer.parseInt(s); } catch (Exception e) { return dft; }
    }
}
