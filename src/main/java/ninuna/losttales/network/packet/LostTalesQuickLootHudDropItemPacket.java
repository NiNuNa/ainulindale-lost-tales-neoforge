package ninuna.losttales.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import ninuna.losttales.LostTales;

public record LostTalesQuickLootHudDropItemPacket(int x, int y, int z, int selectedIndex) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<LostTalesQuickLootHudDropItemPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "quick_loot_hud_server"));
    public static final StreamCodec<ByteBuf, LostTalesQuickLootHudDropItemPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, LostTalesQuickLootHudDropItemPacket::x,
            ByteBufCodecs.VAR_INT, LostTalesQuickLootHudDropItemPacket::y,
            ByteBufCodecs.VAR_INT, LostTalesQuickLootHudDropItemPacket::z,
            ByteBufCodecs.VAR_INT, LostTalesQuickLootHudDropItemPacket::selectedIndex,
            LostTalesQuickLootHudDropItemPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            Level level = player.level();

            if (level.getBlockEntity(new BlockPos(x, y, z)) instanceof Container container) {
                ItemStack stack = container.getItem(selectedIndex);
                if (!stack.isEmpty()) {
                    container.setItem(selectedIndex, ItemStack.EMPTY);

                    ItemEntity entity = new ItemEntity(level, x + 0.5, y + 1.0, z + 0.5, stack.copy());
                    level.addFreshEntity(entity);
                    container.setChanged();
                    LostTales.LOGGER.info("Dropped item: " + stack.getDisplayName().getString());
                }
            }
        });
    }
}
