package dev.ninuna.losttales.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.attachment.LostTalesAttachments;
import dev.ninuna.losttales.common.quest.LostTalesQuest;
import dev.ninuna.losttales.common.quest.LostTalesQuestPlayerData;
import dev.ninuna.losttales.common.quest.LostTalesQuestServices;
import dev.ninuna.losttales.common.quest.objective.LostTalesQuestObjective;
import dev.ninuna.losttales.common.quest.stage.LostTalesQuestStage;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LostTalesQuestCommand {

    private static final SuggestionProvider<CommandSourceStack> QUEST_IDS = (ctx, builder) -> {
        var quests = LostTalesQuestServices.quests();
        if (quests == null) return SharedSuggestionProvider.suggest(Stream.of(), builder);
        return SharedSuggestionProvider.suggest(quests.getQuests().stream().map(quest -> quest.id().toString()), builder);
    };

    private static final SuggestionProvider<CommandSourceStack> STAGE_IDS = (ctx, builder) -> {
        var questId = ResourceLocationArgument.getId(ctx, "quest_id");
        Optional<LostTalesQuest> quest = parseQuest(questId);
        return quest.map(lostTalesQuest -> SharedSuggestionProvider.suggest(lostTalesQuest.stages().stream().map(LostTalesQuestStage::id), builder)).orElseGet(() -> SharedSuggestionProvider.suggest(Stream.of(), builder));
    };

    private static final SuggestionProvider<CommandSourceStack> OBJECTIVE_IDS = (ctx, builder) -> {
        var questId = ResourceLocationArgument.getId(ctx, "quest_id");
        Optional<LostTalesQuest> lostTalesQuest = parseQuest(questId);
        return lostTalesQuest.map(quest -> SharedSuggestionProvider.suggest(quest.stages().stream().flatMap(stage -> stage.objectives().stream().map(LostTalesQuestObjective::id)), builder)).orElseGet(() -> SharedSuggestionProvider.suggest(Stream.of(), builder));
    };

    public LostTalesQuestCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> questCommands =
                Commands.literal("quest")
                        .then(Commands.literal("ids")
                                .executes(LostTalesQuestCommand::listIds)
                        )
                        .then(Commands.literal("list")
                                .executes(LostTalesQuestCommand::listSelf)
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(ctx -> listOther(ctx, EntityArgument.getPlayer(ctx, "player")))
                                )
                        )
                        .then(Commands.literal("start")
                                .then(Commands.argument("quest_id", ResourceLocationArgument.id()).suggests(QUEST_IDS)
                                        .executes(ctx -> {
                                            var id = ResourceLocationArgument.getId(ctx, "quest_id");
                                            return startFor(ctx, ctx.getSource().getPlayerOrException(), id.toString());
                                        })
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(ctx -> {
                                                    var id = ResourceLocationArgument.getId(ctx, "quest_id");
                                                    return startFor(ctx, EntityArgument.getPlayer(ctx, "player"), id.toString());
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("setstage")
                                .then(Commands.argument("quest_id", ResourceLocationArgument.id()).suggests(QUEST_IDS)
                                        .then(Commands.argument("stage_id", StringArgumentType.word()).suggests(STAGE_IDS)
                                                .executes(ctx -> {
                                                    var id = ResourceLocationArgument.getId(ctx, "quest_id");
                                                    var stage = StringArgumentType.getString(ctx, "stage_id");
                                                    return setStageFor(ctx, ctx.getSource().getPlayerOrException(), id.toString(), stage);
                                                })
                                                .then(Commands.argument("player", EntityArgument.player())
                                                        .executes(ctx -> {
                                                            var id = ResourceLocationArgument.getId(ctx, "quest_id");
                                                            var stage = StringArgumentType.getString(ctx, "stage_id");
                                                            return setStageFor(ctx, EntityArgument.getPlayer(ctx, "player"), id.toString(), stage);
                                                        })
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("addprogress")
                                .then(Commands.argument("quest_id", ResourceLocationArgument.id()).suggests(QUEST_IDS)
                                        .then(Commands.argument("objective_id", StringArgumentType.word()).suggests(OBJECTIVE_IDS)
                                                .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                                        .executes(ctx -> {
                                                            var id = ResourceLocationArgument.getId(ctx, "quest_id");
                                                            var obj = StringArgumentType.getString(ctx, "objective_id");
                                                            int amt = IntegerArgumentType.getInteger(ctx, "amount");
                                                            return addProgressFor(ctx, ctx.getSource().getPlayerOrException(), id.toString(), obj, amt);
                                                        })
                                                        .then(Commands.argument("player", EntityArgument.player())
                                                                .executes(ctx -> {
                                                                    var id = ResourceLocationArgument.getId(ctx, "quest_id");
                                                                    var obj = StringArgumentType.getString(ctx, "objective_id");
                                                                    int amt = IntegerArgumentType.getInteger(ctx, "amount");
                                                                    return addProgressFor(ctx, EntityArgument.getPlayer(ctx, "player"), id.toString(), obj, amt);
                                                                })
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("reset")
                                .then(Commands.argument("quest_id", ResourceLocationArgument.id()).suggests(QUEST_IDS)
                                        .executes(ctx -> {
                                            var id = ResourceLocationArgument.getId(ctx, "quest_id");
                                            return resetFor(ctx, ctx.getSource().getPlayerOrException(), id.toString());
                                        })
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(ctx -> {
                                                    var id = ResourceLocationArgument.getId(ctx, "quest_id");
                                                    return resetFor(ctx, EntityArgument.getPlayer(ctx, "player"), id.toString());
                                                })
                                        )
                                )
                        )
                        .then(Commands.literal("complete")
                                .then(Commands.argument("quest_id", ResourceLocationArgument.id()).suggests(QUEST_IDS)
                                        .executes(ctx -> {
                                            var id = ResourceLocationArgument.getId(ctx, "quest_id");
                                            return completeFor(ctx, ctx.getSource().getPlayerOrException(), id.toString());
                                        })
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(ctx -> {
                                                    var id = ResourceLocationArgument.getId(ctx, "quest_id");
                                                    return completeFor(ctx, EntityArgument.getPlayer(ctx, "player"), id.toString());
                                                })
                                        )
                                )
                        );

        LiteralArgumentBuilder<CommandSourceStack> root =
                Commands.literal(LostTales.MOD_ID)
                        .requires(src -> src.hasPermission(2))
                        .then(questCommands);
        dispatcher.register(root);
    }

    private static Optional<LostTalesQuest> parseQuest(ResourceLocation questId) {
        var loader = LostTalesQuestServices.quests();
        if (loader == null) return Optional.empty();
        return loader.getQuest(questId);
    }

    private static int listIds(CommandContext<CommandSourceStack> ctx) {
        var loader = LostTalesQuestServices.quests();
        if (loader == null) {
            ctx.getSource().sendFailure(Component.literal("No quest loader present (are quests registered server-side?)"));
            return 0;
        }
        var ids = loader.getQuests().stream().map(q -> q.id().toString()).sorted().toList();
        if (ids.isEmpty()) {
            ctx.getSource().sendSuccess(() -> Component.literal("No quests loaded."), true);
        } else {
            ctx.getSource().sendSuccess(() -> Component.literal("Loaded quests (" + ids.size() + "):"), true);
            for (String id : ids) ctx.getSource().sendSuccess(() -> Component.literal("- " + id), false);
        }
        return 1;
    }

    private static int listSelf(CommandContext<CommandSourceStack> ctx) {
        try {
            var player = ctx.getSource().getPlayerOrException();
            return listOther(ctx, player);
        } catch (Exception e) {
            ctx.getSource().sendFailure(Component.literal("Player required."));
            return 0;
        }
    }

    private static int listOther(CommandContext<CommandSourceStack> ctx, ServerPlayer player) {
        var data = player.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        var active = data.getActiveQuests();
        if (active.isEmpty()) {
            ctx.getSource().sendSuccess(() -> Component.literal("No active quests for " + player.getScoreboardName()), true);
        } else {
            ctx.getSource().sendSuccess(() -> Component.literal("Active quests for " + player.getScoreboardName() + ":"), true);
            for (LostTalesQuestPlayerData.QuestProgress qp : active) {
                ctx.getSource().sendSuccess(() -> Component.literal("- " + qp.questId + " @ " + qp.stageId), false);
            }
        }
        return 1;
    }

    // fuzzy “did you mean …?” list for unknown IDs
    private static List<String> nearestQuestIds(String typed) {
        var loader = LostTalesQuestServices.quests();
        if (loader == null) return List.of();
        List<String> all = loader.getQuests().stream().map(q -> q.id().toString()).toList();
        record Pair(String id, int d) {}
        return all.stream()
                .map(id -> new Pair(id, ld(typed, id)))
                .sorted(Comparator.comparingInt(p -> p.d))
                .limit(5)
                .map(p -> p.id)
                .collect(Collectors.toList());
    }

    // plain Levenshtein distance
    private static int ld(String a, String b) {
        int n = a.length(), m = b.length();
        int[] dp = new int[m + 1];
        for (int j = 0; j <= m; j++) dp[j] = j;
        for (int i = 1; i <= n; i++) {
            int prev = dp[0];
            dp[0] = i;
            for (int j = 1; j <= m; j++) {
                int tmp = dp[j];
                int cost = (a.charAt(i - 1) == b.charAt(j - 1)) ? 0 : 1;
                dp[j] = Math.min(Math.min(dp[j] + 1, dp[j - 1] + 1), prev + cost);
                prev = tmp;
            }
        }
        return dp[m];
    }

    private static int startFor(CommandContext<CommandSourceStack> ctx, ServerPlayer player, String questIdStr) {
        var id = ResourceLocation.parse(questIdStr);
        var qOpt = parseQuest(id);
        if (qOpt.isEmpty()) {
            var tips = String.join(", ", nearestQuestIds(questIdStr));
            ctx.getSource().sendFailure(Component.literal("Unknown quest: " + questIdStr + (tips.isEmpty() ? "" : "  (did you mean: " + tips + ")")));
            return 0;
        }
        var quest = qOpt.get();
        var data = player.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        if (data.isQuestCompleted(quest.id())) {
            ctx.getSource().sendFailure(Component.literal("Quest already completed: " + quest.id()));
            return 0;
        }
        var firstStage = quest.stages().isEmpty() ? null : quest.stages().get(0).id();
        if (firstStage == null) {
            ctx.getSource().sendFailure(Component.literal("Quest has no stages: " + quest.id()));
            return 0;
        }
        var existing = data.getQuest(quest.id());
        if (existing.isPresent()) {
            data.setStage(quest.id(), firstStage);
        } else {
            data.startQuest(quest.id(), firstStage);
        }
        ctx.getSource().sendSuccess(() -> Component.literal("Started quest " + quest.id() + " for " + player.getScoreboardName()), true);
        return 1;
    }

    private static int setStageFor(CommandContext<CommandSourceStack> ctx, ServerPlayer player, String questIdStr, String stageId) {
        var id = ResourceLocation.parse(questIdStr);
        var qOpt = parseQuest(id);
        if (qOpt.isEmpty()) {
            var tips = String.join(", ", nearestQuestIds(questIdStr));
            ctx.getSource().sendFailure(Component.literal("Unknown quest: " + questIdStr + (tips.isEmpty() ? "" : "  (did you mean: " + tips + ")")));
            return 0;
        }
        var quest = qOpt.get();
        var stageExists = quest.stages().stream().anyMatch(s -> s.id().equals(stageId));
        if (!stageExists) { ctx.getSource().sendFailure(Component.literal("Unknown stage '" + stageId + "' for quest " + quest.id())); return 0; }
        var data = player.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        var existing = data.getQuest(quest.id());
        if (existing.isEmpty()) {
            data.startQuest(quest.id(), stageId);
        } else {
            data.setStage(quest.id(), stageId);
        }
        ctx.getSource().sendSuccess(() -> Component.literal("Set stage of " + quest.id() + " to '" + stageId + "' for " + player.getScoreboardName()), true);
        return 1;
    }

    private static int addProgressFor(CommandContext<CommandSourceStack> ctx, ServerPlayer player, String questIdStr, String objectiveId, int amount) {
        var id = ResourceLocation.parse(questIdStr);
        var qOpt = parseQuest(id);
        if (qOpt.isEmpty()) {
            var tips = String.join(", ", nearestQuestIds(questIdStr));
            ctx.getSource().sendFailure(Component.literal("Unknown quest: " + questIdStr + (tips.isEmpty() ? "" : "  (did you mean: " + tips + ")")));
            return 0;
        }
        var quest = qOpt.get();
        var data = player.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        var existing = data.getQuest(quest.id());
        if (existing.isEmpty()) { ctx.getSource().sendFailure(Component.literal("Quest is not active for player.")); return 0; }
        int now = data.addProgress(quest.id(), objectiveId, amount);
        ctx.getSource().sendSuccess(() -> Component.literal("Objective '" + objectiveId + "': " + now + " (+" + amount + ")"), true);
        return 1;
    }

    private static int resetFor(CommandContext<CommandSourceStack> ctx, ServerPlayer player, String questIdStr) {
        var id = ResourceLocation.parse(questIdStr);
        var qOpt = parseQuest(id);
        if (qOpt.isEmpty()) {
            var tips = String.join(", ", nearestQuestIds(questIdStr));
            ctx.getSource().sendFailure(Component.literal("Unknown quest: " + questIdStr + (tips.isEmpty() ? "" : "  (did you mean: " + tips + ")")));
            return 0;
        }
        var quest = qOpt.get();
        var data = player.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        var firstStage = quest.stages().isEmpty() ? null : quest.stages().get(0).id();
        if (firstStage == null) { ctx.getSource().sendFailure(Component.literal("Quest has no stages: " + quest.id())); return 0; }
        var existing = data.getQuest(quest.id());
        if (existing.isEmpty()) {
            data.startQuest(quest.id(), firstStage);
        } else {
            data.setStage(quest.id(), firstStage);
        }
        ctx.getSource().sendSuccess(() -> Component.literal("Reset (restarted) quest " + quest.id() + " for " + player.getScoreboardName()), true);
        return 1;
    }

    private static int completeFor(CommandContext<CommandSourceStack> ctx, ServerPlayer player, String questIdStr) {
        var id = ResourceLocation.parse(questIdStr);
        var qOpt = parseQuest(id);
        if (qOpt.isEmpty()) {
            var tips = String.join(", ", nearestQuestIds(questIdStr));
            ctx.getSource().sendFailure(Component.literal("Unknown quest: " + questIdStr + (tips.isEmpty() ? "" : "  (did you mean: " + tips + ")")));
            return 0;
        }
        var quest = qOpt.get();
        var data = player.getData(LostTalesAttachments.PLAYER_QUESTS.get());
        data.completeQuest(quest.id());
        ctx.getSource().sendSuccess(() -> Component.literal("Marked quest " + quest.id() + " complete for " + player.getScoreboardName()), true);
        return 1;
    }
}
