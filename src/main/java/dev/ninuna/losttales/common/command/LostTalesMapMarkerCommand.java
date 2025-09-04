package dev.ninuna.losttales.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import dev.ninuna.losttales.client.gui.LostTalesGuiColor;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarkerIcon;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.data.attachement.LostTalesAttachments;
import dev.ninuna.losttales.common.mapmarker.LostTalesMapMarkerData;
import dev.ninuna.losttales.common.network.packet.LostTalesSyncMapMarkersPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LostTalesMapMarkerCommand {

    public LostTalesMapMarkerCommand(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(
                Commands.literal(LostTales.MOD_ID)
                        .then(Commands.literal("map_marker")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                        .then(Commands.literal("add")
                                .then(Commands.argument("name", StringArgumentType.greedyString())
                                        .executes(context -> addAtPlayer(context, StringArgumentType.getString(context, "name"), "#FF5555")) // default color
                                )
                                .then(Commands.argument("name", StringArgumentType.word())
                                        .then(Commands.argument("color", StringArgumentType.word())   // "#RRGGBB" or "0xRRGGBB" or "RRGGBB"
                                                .executes(ctx -> addAtPlayer(ctx,
                                                        StringArgumentType.getString(ctx, "name"),
                                                        StringArgumentType.getString(ctx, "color")))
                                                .then(Commands.argument("x", DoubleArgumentType.doubleArg())
                                                        .then(Commands.argument("y", DoubleArgumentType.doubleArg())
                                                                .then(Commands.argument("z", DoubleArgumentType.doubleArg())
                                                                        .executes(ctx -> addAtPos(ctx,
                                                                                StringArgumentType.getString(ctx, "name"),
                                                                                StringArgumentType.getString(ctx, "color"),
                                                                                DoubleArgumentType.getDouble(ctx, "x"),
                                                                                DoubleArgumentType.getDouble(ctx, "y"),
                                                                                DoubleArgumentType.getDouble(ctx, "z")
                                                                        ))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(Commands.literal("remove")
                                .then(Commands.argument("name/uuid", StringArgumentType.word())
                                        .suggests((ctx, builder) -> {
                                            var data = ctx.getSource().getLevel().getData(LostTalesAttachments.LEVEL_MARKERS.get());
                                            data.all().forEach(e -> builder.suggest(e.name));
                                            return builder.buildFuture();
                                        })
                                        .executes(this::removeMapMarker)
                                )
                        )
                        .then(Commands.literal("list")
                                .executes(this::listMapMarkers)
                        )
                )
        );
    }

    private int addAtPlayer(CommandContext<CommandSourceStack> ctx, String name, String colorStr) {
        ServerPlayer player = ctx.getSource().getPlayer();
        if (player == null) return 0;
        Vec3 p = player.position();
        return addCommon(ctx, name, colorStr, p.x, p.y, p.z);
    }

    private int addAtPos(CommandContext<CommandSourceStack> ctx, String name, String colorStr,
                         double x, double y, double z) {
        return addCommon(ctx, name, colorStr, x, y, z);
    }

    private int addCommon(CommandContext<CommandSourceStack> ctx, String name, String colorStr,
                          double x, double y, double z) {
        ServerLevel level = ctx.getSource().getLevel();
        LostTalesMapMarkerData data = level.getData(LostTalesAttachments.LEVEL_MARKERS.get());
        int color = parseColor(colorStr).orElse(0xFF5555);
        LostTalesGuiColor color1 = LostTalesGuiColor.BLUE;

        LostTalesMapMarkerData.Entry entry = new LostTalesMapMarkerData.Entry(
                UUID.randomUUID(),
                name,
                LostTalesMapMarkerIcon.N,
                color1,
                level.dimension(),
                x, y, z, 0, 0
        );

        boolean added = data.add(entry);
        if (!added) {
            ctx.getSource().sendFailure(Component.literal("A marker with that UUID already exists (unexpected). Try again."));
            return 0;
        }

        // broadcast full level markers to everyone
        syncLevelMarkersToAll(level.getServer(), data);

        ctx.getSource().sendSuccess(() ->
                Component.literal("Added marker “" + name + "” at " +
                        String.format("(%.1f, %.1f, %.1f)", x, y, z) + " color " + hex(color)), true);

        return 1;
    }

    private int removeMapMarker(CommandContext<CommandSourceStack> ctx) {
        ServerLevel level = ctx.getSource().getLevel();
        LostTalesMapMarkerData data = level.getData(LostTalesAttachments.LEVEL_MARKERS.get());

        String key = StringArgumentType.getString(ctx, "name/uuid").trim();
        boolean removed = tryRemoveByUUID(data, key) || data.removeByNameFirst(key);

        if (!removed) {
            ctx.getSource().sendFailure(Component.literal("No marker found for “" + key + "”."));
            return 0;
        }

        syncLevelMarkersToAll(level.getServer(), data);
        ctx.getSource().sendSuccess(() -> Component.literal("Removed marker “" + key + "”."), true);
        return 1;
    }

    private int listMapMarkers(CommandContext<CommandSourceStack> ctx) {
        ServerLevel level = ctx.getSource().getLevel();
        LostTalesMapMarkerData data = level.getData(LostTalesAttachments.LEVEL_MARKERS.get());
        List<LostTalesMapMarkerData.Entry> all = new ArrayList<>(data.all());

        if (all.isEmpty()) {
            ctx.getSource().sendSuccess(() -> Component.literal("No shared markers set."), false);
            return 1;
        }

        ctx.getSource().sendSuccess(() -> Component.literal("Shared markers (" + all.size() + "):"), false);
        for (LostTalesMapMarkerData.Entry e : all) {
            ctx.getSource().sendSuccess(() ->
                    Component.literal("• " + e.name + " " + e.id + " @ " +
                            String.format("(%.1f, %.1f, %.1f)", e.x, e.y, e.z) +
                            " " + e.dimension.location() + " " + hex(e.color.getColorRgb())), false);
        }
        return 1;
    }

    private static void syncLevelMarkersToAll(MinecraftServer server, LostTalesMapMarkerData data) {
        PacketDistributor.sendToAllPlayers(new LostTalesSyncMapMarkersPacket(true, data.all()));
    }

    private static Optional<Integer> parseColor(String s) {
        String t = s.trim();
        try {
            int rgb;
            if (t.startsWith("#")) {
                rgb = Integer.parseUnsignedInt(t.substring(1), 16);
            } else if (t.startsWith("0x") || t.startsWith("0X")) {
                rgb = Integer.parseUnsignedInt(t.substring(2), 16);
            } else {
                rgb = Integer.parseUnsignedInt(t, 16);
            }
            // force opaque alpha
            int argb = (0xFF << 24) | (rgb & 0xFFFFFF);
            return Optional.of(argb);
        } catch (Exception ignored) {
            return Optional.of(0xFFFF5555); // sensible opaque default
        }
    }

    private static String hex(int argb) {
        return String.format("#%08X", argb);
    }

    private static boolean tryRemoveByUUID(LostTalesMapMarkerData data, String s) {
        try {
            UUID id = UUID.fromString(s);
            return data.remove(id);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
