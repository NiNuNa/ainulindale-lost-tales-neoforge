package dev.ninuna.losttales.common.quest.objective.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class LostTalesQuestObjectiveHandlers {
    private static final Map<String, LostTalesQuestObjectiveHandler> BY_TYPE = new HashMap<>();

    public static void register(LostTalesQuestObjectiveHandler objectiveHandler) {
        BY_TYPE.put(objectiveHandler.questObjectiveType(), objectiveHandler);
    }
    public static Optional<LostTalesQuestObjectiveHandler> get(String type) {
        return Optional.ofNullable(BY_TYPE.get(type));
    }
}
