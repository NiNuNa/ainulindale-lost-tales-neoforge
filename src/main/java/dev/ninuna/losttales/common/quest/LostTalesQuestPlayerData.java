package dev.ninuna.losttales.common.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public class LostTalesQuestPlayerData {

    public static final class QuestProgress {
        public static final Codec<QuestProgress> CODEC = RecordCodecBuilder.create(questProgressInstance -> questProgressInstance.group(
                ResourceLocation.CODEC.fieldOf("questId").forGetter(questProgress -> questProgress.questId),
                Codec.STRING.fieldOf("stageId").forGetter(questProgress -> questProgress.stageId),
                Codec.unboundedMap(Codec.STRING, Codec.INT).fieldOf("objectiveProgress").forGetter(questProgress -> questProgress.objectiveProgress)
        ).apply(questProgressInstance, QuestProgress::new));

        public final ResourceLocation questId;
        public String stageId;
        public final Map<String, Integer> objectiveProgress = new HashMap<>();

        public QuestProgress(ResourceLocation questId, String stageId, Map<String, Integer> objectiveProgress) {
            this.questId = questId;
            this.stageId = stageId;
            if (objectiveProgress != null) this.objectiveProgress.putAll(objectiveProgress);
        }
    }

    public static final MapCodec<LostTalesQuestPlayerData> MAP_CODEC = RecordCodecBuilder.mapCodec(questPlayerDataInstance -> questPlayerDataInstance.group(
            QuestProgress.CODEC.listOf().fieldOf("active").forGetter(questPlayerData -> new ArrayList<>(questPlayerData.activeQuests.values())),
            ResourceLocation.CODEC.listOf().fieldOf("completed").forGetter(questPlayerData -> new ArrayList<>(questPlayerData.completedQuests))
    ).apply(questPlayerDataInstance, (activeList, completedList) -> {
        LostTalesQuestPlayerData questPlayerData = new LostTalesQuestPlayerData();
        for (QuestProgress questProgress : activeList) questPlayerData.activeQuests.put(questProgress.questId, questProgress);
        questPlayerData.completedQuests.addAll(completedList);
        return questPlayerData;
    }));

    private final Map<ResourceLocation, QuestProgress> activeQuests = new HashMap<>();
    private final Set<ResourceLocation> completedQuests = new HashSet<>();

    public Collection<QuestProgress> getActiveQuests() {
        return activeQuests.values();
    }

    public Set<ResourceLocation> getCompletedQuests() {
        return Set.copyOf(completedQuests);
    }

    public boolean isQuestActive(ResourceLocation questId) {
        return activeQuests.get(questId) != null;
    }

    public Optional<QuestProgress> getActiveQuest(ResourceLocation questId) {
        return Optional.ofNullable(activeQuests.get(questId));
    }

    public boolean isQuestCompleted(ResourceLocation questId) {
        return completedQuests.contains(questId);
    }

    public void abandonQuest(ResourceLocation id) {
        this.activeQuests.remove(id);
    }

    public void removeCompletedQuest(ResourceLocation id) {
        this.completedQuests.remove(id);
    }

    public void clearObjectiveProgress(ResourceLocation id) {
        QuestProgress qp = this.activeQuests.get(id);
        if (qp != null) qp.objectiveProgress.clear();
    }

    public void startQuest(ResourceLocation id, String stageId) {
        QuestProgress qp = new QuestProgress(id, stageId, new java.util.HashMap<>());
        this.activeQuests.put(id, qp);
    }

    public void completeQuest(ResourceLocation id) {
        this.activeQuests.remove(id);
        this.completedQuests.add(id);
    }

    public void setStage(ResourceLocation id, String stageId) {
        QuestProgress qp = this.activeQuests.get(id);
        if (qp != null) qp.stageId = stageId;
    }

    public int addProgress(ResourceLocation id, String objectiveId, int delta) {
        QuestProgress qp = activeQuests.get(id);
        if (qp == null) return 0;
        int v = qp.objectiveProgress.getOrDefault(objectiveId, 0) + delta;
        qp.objectiveProgress.put(objectiveId, v);
        return v;
    }

    public void setProgress(ResourceLocation id, String objectiveId, int value) {
        QuestProgress questProgress = activeQuests.get(id);
        if (questProgress == null) return;
        questProgress.objectiveProgress.put(objectiveId, value);
    }
}
