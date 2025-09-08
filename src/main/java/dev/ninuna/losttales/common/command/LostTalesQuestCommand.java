package dev.ninuna.losttales.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.datapack.loader.LostTalesQuestDatapackLoader;
import dev.ninuna.losttales.common.quest.LostTalesQuestManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class LostTalesQuestCommand {

    private static final SuggestionProvider<CommandSourceStack> QUEST_IDS = (context, suggestionsBuilder) ->
            SharedSuggestionProvider.suggest(
                    LostTalesQuestDatapackLoader.getAllQuests().stream().map(quest -> quest.id().toString()), suggestionsBuilder
            );

    public LostTalesQuestCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        var questCommands = Commands.literal(LostTales.MOD_ID)
                .then(Commands.literal("quest")
                        .then(Commands.literal("list")
                                .then(Commands.literal("active")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(ctx -> {
                                                    ServerPlayer p = EntityArgument.getPlayer(ctx, "player");
                                                    var data = p.getData(dev.ninuna.losttales.common.attachment.LostTalesAttachments.PLAYER_QUESTS.get());
                                                    for (var qp : data.getActiveQuests()) {
                                                        ctx.getSource().sendSuccess(() -> Component.literal("Active: " + qp.questId + " @ stage " + qp.stageId), false);
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                                .then(Commands.literal("completed")
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(ctx -> {
                                                    ServerPlayer p = EntityArgument.getPlayer(ctx, "player");
                                                    var data = p.getData(dev.ninuna.losttales.common.attachment.LostTalesAttachments.PLAYER_QUESTS.get());
                                                    for (var id : data.getCompletedQuests()) {
                                                        ctx.getSource().sendSuccess(() -> Component.literal("Completed: " + id), false);
                                                    }
                                                    return 1;
                                                })
                                        )
                                )
                                .then(Commands.literal("all")
                                        .executes(context -> {
                                            for (var loadedQuest : LostTalesQuestDatapackLoader.getAllQuests()) {
                                                context.getSource().sendSuccess(() -> Component.literal(loadedQuest.id().toString()), false);
                                            }
                                            return 1;
                                        })
                                ).requires(src -> src.hasPermission(2))
                        )
                        // start
                        .then(Commands.literal("start").requires(src -> src.hasPermission(2))
                                .then(Commands.argument("quest_id", ResourceLocationArgument.id()).suggests(QUEST_IDS)
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(ctx -> {
                                                    ResourceLocation qid = ResourceLocationArgument.getId(ctx, "quest_id");
                                                    ServerPlayer p = EntityArgument.getPlayer(ctx, "player");
                                                    boolean ok = LostTalesQuestManager.startQuestForPlayer(qid, p);
                                                    ctx.getSource().sendSuccess(() -> Component.literal(ok
                                                            ? "Started quest " + qid
                                                            : "Cannot start quest " + qid), true);
                                                    return ok ? 1 : 0;
                                                })
                                        )
                                )
                        )
                        // complete
                        .then(Commands.literal("complete").requires(src -> src.hasPermission(2))
                                .then(Commands.argument("quest_id", ResourceLocationArgument.id()).suggests(QUEST_IDS)
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(ctx -> {
                                                    ResourceLocation qid = ResourceLocationArgument.getId(ctx, "quest_id");
                                                    ServerPlayer p = EntityArgument.getPlayer(ctx, "player");
                                                    boolean ok = LostTalesQuestManager.completeQuestForPlayer(qid, p);
                                                    ctx.getSource().sendSuccess(() -> Component.literal(ok
                                                            ? "Completed quest " + qid
                                                            : "Quest " + qid + " is not active"), true);
                                                    return ok ? 1 : 0;
                                                })
                                        )
                                )
                        )
                        // setstage
                        .then(Commands.literal("setstage").requires(src -> src.hasPermission(2))
                                .then(Commands.argument("quest_id", ResourceLocationArgument.id()).suggests(QUEST_IDS)
                                        .then(Commands.argument("stage_id", StringArgumentType.string())
                                                .then(Commands.argument("player", EntityArgument.player())
                                                        .executes(ctx -> {
                                                            ResourceLocation qid = ResourceLocationArgument.getId(ctx, "quest_id");
                                                            String sid = StringArgumentType.getString(ctx, "stage_id");
                                                            ServerPlayer p = EntityArgument.getPlayer(ctx, "player");
                                                            boolean ok = LostTalesQuestManager.setQuestStageForPlayer(qid, sid, p);
                                                            ctx.getSource().sendSuccess(() -> Component.literal(ok
                                                                    ? "Set " + qid + " to stage " + sid
                                                                    : "Failed to set stage (quest/stage may be invalid or quest not active)"), true);
                                                            return ok ? 1 : 0;
                                                        })
                                                )
                                        )
                                )
                        )
                        // abandon
                        .then(Commands.literal("abandon").requires(src -> src.hasPermission(2))
                                .then(Commands.argument("quest_id", ResourceLocationArgument.id()).suggests(QUEST_IDS)
                                        .then(Commands.argument("player", EntityArgument.player())
                                                .executes(ctx -> {
                                                    ResourceLocation qid = ResourceLocationArgument.getId(ctx, "quest_id");
                                                    ServerPlayer p = EntityArgument.getPlayer(ctx, "player");
                                                    boolean ok = LostTalesQuestManager.abandonQuestForPlayer(qid, p);
                                                    ctx.getSource().sendSuccess(() -> Component.literal(ok
                                                            ? "Abandoned quest " + qid
                                                            : "Quest " + qid + " not active"), true);
                                                    return ok ? 1 : 0;
                                                })
                                        )
                                )
                        )
                );
        dispatcher.register(questCommands);
    }
}
