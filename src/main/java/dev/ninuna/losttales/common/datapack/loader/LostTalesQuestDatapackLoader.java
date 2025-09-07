package dev.ninuna.losttales.common.datapack.loader;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.quest.LostTalesQuest;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class LostTalesQuestDatapackLoader extends SimpleJsonResourceReloadListener<LostTalesQuest> {
    private Map<ResourceLocation, LostTalesQuest> quests = Map.of();

    public LostTalesQuestDatapackLoader() {
        super(LostTalesQuest.CODEC, FileToIdConverter.json("quest"));
    }

    @Override
    protected void apply(Map<ResourceLocation, LostTalesQuest> resources, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        this.quests = Map.copyOf(resources);
        LostTales.LOGGER.info("[{}] Loaded {} quests from datapacks", LostTales.MOD_ID, quests.size());
    }

    public Collection<LostTalesQuest> getQuests() {
        return quests.values();
    }

    public Optional<LostTalesQuest> getQuest(ResourceLocation id) {
        return Optional.ofNullable(quests.get(id));
    }
}
