package dev.ninuna.losttales.common.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.ninuna.losttales.common.quest.stage.LostTalesQuestStage;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public record LostTalesQuest(
        ResourceLocation id,
        String title,
        String description,
        boolean repeatable,
        Map<String, String> journalLog,
        List<LostTalesQuestStage> stages
) {
    public static final Codec<LostTalesQuest> CODEC = RecordCodecBuilder.create(questInstance -> questInstance.group(
            ResourceLocation.CODEC.fieldOf("id").forGetter(LostTalesQuest::id),
            Codec.STRING.fieldOf("title").forGetter(LostTalesQuest::title),
            Codec.STRING.fieldOf("description").forGetter(LostTalesQuest::description),
            Codec.BOOL.optionalFieldOf("repeatable", false).forGetter(LostTalesQuest::repeatable),
            Codec.unboundedMap(Codec.STRING, Codec.STRING).fieldOf("journalLog").forGetter(LostTalesQuest::journalLog),
            LostTalesQuestStage.CODEC.listOf().fieldOf("stages").forGetter(LostTalesQuest::stages)
    ).apply(questInstance, LostTalesQuest::new));
}
