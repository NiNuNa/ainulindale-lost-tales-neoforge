package dev.ninuna.losttales.common.quest.objective;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;

public record LostTalesQuestObjective(
        String id,
        String type,
        String description,
        boolean optional,
        Map<String, String> params
) {
    public static final Codec<LostTalesQuestObjective> CODEC = RecordCodecBuilder.create(questObjectiveInstance -> questObjectiveInstance.group(
            Codec.STRING.fieldOf("id").forGetter(LostTalesQuestObjective::id),
            Codec.STRING.fieldOf("type").forGetter(LostTalesQuestObjective::type),
            Codec.STRING.fieldOf("description").forGetter(LostTalesQuestObjective::description),
            Codec.BOOL.optionalFieldOf("optional", false).forGetter(LostTalesQuestObjective::optional),
            Codec.unboundedMap(Codec.STRING, Codec.STRING).optionalFieldOf("params", Map.of()).forGetter(LostTalesQuestObjective::params)
    ).apply(questObjectiveInstance, LostTalesQuestObjective::new));
}
