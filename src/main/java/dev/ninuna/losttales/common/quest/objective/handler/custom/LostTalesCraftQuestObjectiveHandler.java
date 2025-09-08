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

import java.util.Map;

public class LostTalesCraftQuestObjectiveHandler implements LostTalesQuestObjectiveHandler {

    @Override
    public String questObjectiveType() {
        return "craft";
    }

    @Override
    public void validateQuestObjective(LostTalesQuest quest, LostTalesQuestObjective objective) {
        Map<String, String> objectiveParams = objective.params();

        boolean hasItem = objectiveParams.containsKey("item");
        boolean hasTag  = objectiveParams.containsKey("tag");
        boolean hasCount = objectiveParams.containsKey("count");

        if (!hasItem && !hasTag) {
            LostTales.LOGGER.warn("[{}] Objective {} in quest {} missing 'item' or 'tag' parameter for type 'craft'", LostTales.MOD_ID, objective.id(), quest.id());
        }
        if (hasCount) {
            try {
                Integer.parseInt(objectiveParams.get("count"));
            }
            catch (Exception e) {
                LostTales.LOGGER.warn("[{}] Objective {} in quest {} has non-integer 'count': {}", LostTales.MOD_ID, objective.id(), quest.id(), objectiveParams.get("count"));
            }
        }
    }

    @Override
    public void onItemCrafted(ServerPlayer serverPlayer, LostTalesQuest quest, LostTalesQuestObjective objective, ItemStack crafted) {
        Map<String, String> p = objective.params();
        String itemStr = p.get("item");
        String tagStr  = p.get("tag");
        int target = parseIntSafe(p.getOrDefault("count", "1"), 1);

        if (!matches(crafted, itemStr, tagStr, serverPlayer)) return;

        var data = serverPlayer.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        int current = data.addProgress(quest.id(), objective.id(), crafted.getCount()); // count produced
        if (current >= target) {
            data.setProgress(quest.id(), objective.id(), target); // clamp to target
            LostTalesQuestManager.evaluateStageProgressForPlayer(quest.id(), serverPlayer);
        }
    }

    private static boolean matches(ItemStack stack, String itemStr, String tagStr, ServerPlayer player) {
        if (stack.isEmpty()) return false;

        // Prefer explicit item
        if (itemStr != null && !itemStr.isBlank()) {
            var id = ResourceLocation.tryParse(itemStr);
            if (id != null) {
                // compare keys directly to avoid registry lookup pitfalls
                var keyOfCrafted = BuiltInRegistries.ITEM.getKey(stack.getItem());
                if (id.equals(keyOfCrafted)) return true;
            }
        }

        // Then tags
        if (tagStr != null && !tagStr.isBlank()) {
            // allow "#namespace:path" or "namespace:path"
            String clean = tagStr.startsWith("#") ? tagStr.substring(1) : tagStr;
            var tagId = ResourceLocation.tryParse(clean);
            if (tagId != null) {
                TagKey<Item> tag = TagKey.create(Registries.ITEM, tagId);
                if (stack.is(tag)) return true;
            }
        }

        return false;
    }

    private static int parseIntSafe(String s, int dft) {
        try { return Integer.parseInt(s); } catch (Exception e) { return dft; }
    }
}
