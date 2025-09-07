package dev.ninuna.losttales.common.quest.stage;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ninuna.losttales.common.quest.objective.LostTalesQuestObjective;

import java.util.List;

public record LostTalesQuestStage(
        String id,
        List<LostTalesQuestObjective> objectives
) {
    public static final Codec<LostTalesQuestStage> CODEC = RecordCodecBuilder.create(questStageInstance -> questStageInstance.group(
            Codec.STRING.fieldOf("id").forGetter(LostTalesQuestStage::id),
            LostTalesQuestObjective.CODEC.listOf().fieldOf("objectives").forGetter(LostTalesQuestStage::objectives)
    ).apply(questStageInstance, LostTalesQuestStage::new));
}
