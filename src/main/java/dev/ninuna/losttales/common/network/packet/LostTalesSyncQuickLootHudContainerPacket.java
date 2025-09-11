package dev.ninuna.losttales.common.network.packet;

import dev.ninuna.losttales.client.cache.LostTalesClientQuickLootCache;
import dev.ninuna.losttales.common.LostTales;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record LostTalesSyncQuickLootHudContainerPacket(BlockPos pos, Component title, List<ItemStack> items) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<LostTalesSyncQuickLootHudContainerPacket> TYPE = new CustomPacketPayload.Type<>(LostTales.getResourceLocation("quick_loot_sync"));

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, LostTalesSyncQuickLootHudContainerPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            LostTalesSyncQuickLootHudContainerPacket::pos,
            ComponentSerialization.STREAM_CODEC,
            LostTalesSyncQuickLootHudContainerPacket::title,
            ByteBufCodecs.collection(java.util.ArrayList::new, ItemStack.OPTIONAL_STREAM_CODEC),
            LostTalesSyncQuickLootHudContainerPacket::items,
            LostTalesSyncQuickLootHudContainerPacket::new
    );

    public static void handle(LostTalesSyncQuickLootHudContainerPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> LostTalesClientQuickLootCache.put(msg.pos(), msg.title(), msg.items()));
    }

    public static Snapshot snapshotFrom(Container container, Level level, BlockPos pos) {
        int n = container.getContainerSize();
        List<ItemStack> copy = new ArrayList<>(n);
        for (int i = 0; i < n; i++) copy.add(container.getItem(i).copy());

        Component title = (container instanceof net.minecraft.world.Nameable name)
                ? name.getDisplayName()
                : net.minecraft.network.chat.Component.translatable(level.getBlockState(pos).getBlock().getDescriptionId());

        return new Snapshot(title, copy);
    }

    public record Snapshot(Component title, List<ItemStack> items) {}
}
