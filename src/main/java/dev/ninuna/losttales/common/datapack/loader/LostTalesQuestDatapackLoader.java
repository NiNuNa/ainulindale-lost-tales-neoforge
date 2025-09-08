package dev.ninuna.losttales.common.datapack.loader;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.quest.LostTalesQuest;
import dev.ninuna.losttales.common.quest.objective.handler.LostTalesQuestObjectiveHandlers;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class LostTalesQuestDatapackLoader extends SimpleJsonResourceReloadListener<LostTalesQuest> {
    private static Map<ResourceLocation, LostTalesQuest> ALL_QUESTS = Map.of();

    public LostTalesQuestDatapackLoader() {
        super(LostTalesQuest.CODEC, FileToIdConverter.json("quest"));
    }

    @Override
    protected void apply(Map<ResourceLocation, LostTalesQuest> resources, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        ALL_QUESTS = Map.copyOf(resources);

        for (var quest : ALL_QUESTS.values()) {
            for (var stage : quest.stages()) {
                for (var objective : stage.objectives()) {
                    LostTalesQuestObjectiveHandlers.get(objective.type()).ifPresent(objectiveHandler -> {
                        try {
                            objectiveHandler.validateQuestObjective(quest, objective);
                        } catch (Exception exception) {
                            LostTales.LOGGER.warn("[{}] Invalid objective {} in quest {}: {}", LostTales.MOD_ID, objective.id(), quest.id(), exception.getMessage());
                        }
                    });
                }
            }
        }
        LostTales.LOGGER.info("[{}] Loaded {} quests from datapacks", LostTales.MOD_ID, ALL_QUESTS.size());
    }

    public static Collection<LostTalesQuest> getAllQuests() {
        return ALL_QUESTS.values();
    }

    public static Optional<LostTalesQuest> getQuest(ResourceLocation id) {
        return Optional.ofNullable(ALL_QUESTS.get(id));
    }
}
