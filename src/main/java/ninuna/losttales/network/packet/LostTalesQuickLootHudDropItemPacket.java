package ninuna.losttales.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import ninuna.losttales.LostTales;

public record LostTalesQuickLootHudDropItemPacket(int x, int y, int z, int selectedIndex) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<LostTalesQuickLootHudDropItemPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "quick_loot_hud_drop_item_server_packet"));

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
            ServerLevel level = player.serverLevel();
            BlockPos pos = BlockPos.containing(x, y, z);

            if (level.getBlockEntity(pos) instanceof Container container) {
                ItemStack itemStack = container.getItem(selectedIndex);
                dropItem(container, level, itemStack, pos, selectedIndex);
            }
        });
    }

    private void dropItem(Container container, ServerLevel level, ItemStack itemStack, BlockPos pos, int selectedIndex) {
        if (!itemStack.isEmpty()) {
            container.setItem(selectedIndex, ItemStack.EMPTY);
            container.setChanged();

            ItemEntity entity = new ItemEntity(level, x + 0.5, y + 1.0, z + 0.5, itemStack.copy());
            level.addFreshEntity(entity);
            level.playSound(null, pos, SoundEvents.DISPENSER_DISPENSE, SoundSource.BLOCKS, 1.0f, 1.0f);

            LostTales.LOGGER.info("Dropped item: " + itemStack.getDisplayName().getString());
            PacketDistributor.sendToAllPlayers(new LostTalesQuickLootHudDropItemClientPacket(x, y, z, selectedIndex));
        }
    }
}
