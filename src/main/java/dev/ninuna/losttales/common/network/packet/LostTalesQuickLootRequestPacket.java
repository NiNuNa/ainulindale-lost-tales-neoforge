package dev.ninuna.losttales.common.network.packet;

import dev.ninuna.losttales.common.LostTales;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record LostTalesQuickLootRequestPacket(BlockPos pos) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<LostTalesQuickLootRequestPacket> TYPE = new CustomPacketPayload.Type<>(LostTales.getResourceLocation("quick_loot_request"));

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, LostTalesQuickLootRequestPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, LostTalesQuickLootRequestPacket::pos,
            LostTalesQuickLootRequestPacket::new
    );

    public static void handle(LostTalesQuickLootRequestPacket msg, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) ctx.player();
            if (player == null) return;
            ServerLevel level = player.level();
            BlockPos pos = msg.pos();

            // Simple anti-cheese: range check (~8 blocks)
            if (player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 64.0) return;

            Container container = resolveServerContainer(level, pos);
            if (container == null) return;

            // Snapshot items & title
            var snapshot = LostTalesSyncQuickLootHudContainerPacket.snapshotFrom(container, level, pos);

            // Send S2C reply only to requesting player
            net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(player,
                    new LostTalesSyncQuickLootHudContainerPacket(pos, snapshot.title(), snapshot.items()));
        });
    }

    private static Container resolveServerContainer(ServerLevel level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);

        // Properly merge double chests
        if (state.getBlock() instanceof ChestBlock chest) {
            // Static helper that merges halves; adjust signature if your mappings differ
            return ChestBlock.getContainer(chest, state, level, pos, false);
        }

        if (be instanceof Container c) return c;

        // Fallback: can discover e.g. minecart containers at that position
        return HopperBlockEntity.getContainerAt(level, pos);
    }
}
