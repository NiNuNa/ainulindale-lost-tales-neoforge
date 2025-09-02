package dev.ninuna.losttales.common.network.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.client.gui.hud.LostTalesQuickLootHud;

public record LostTalesQuickLootHudDropItemClientPacket(int x, int y, int z, int selectedIndex) implements CustomPacketPayload {
    public static final StreamCodec<ByteBuf, LostTalesQuickLootHudDropItemClientPacket> STREAM_CODEC;
    public static final Type<LostTalesQuickLootHudDropItemClientPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "quick_loot_hud_client"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Level level = player.level();

            if (level.getBlockEntity(new BlockPos(x, y, z)) instanceof Container container) {
                ItemStack stack = container.getItem(selectedIndex);
                if (!stack.isEmpty()) {
                    container.setItem(selectedIndex, ItemStack.EMPTY);
                    container.setChanged();

                    int scrollIndex = LostTalesQuickLootHud.getIndexScrollOverflow();
                    if (scrollIndex > 0) {
                        LostTalesQuickLootHud.setIndexScrollOverflow(scrollIndex - 1);
                    }

                    LostTales.LOGGER.info("UPDTED");
                }
            }
        });
    }

    static {
        STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, LostTalesQuickLootHudDropItemClientPacket::x,
                ByteBufCodecs.VAR_INT, LostTalesQuickLootHudDropItemClientPacket::y,
                ByteBufCodecs.VAR_INT, LostTalesQuickLootHudDropItemClientPacket::z,
                ByteBufCodecs.VAR_INT, LostTalesQuickLootHudDropItemClientPacket::selectedIndex,
                LostTalesQuickLootHudDropItemClientPacket::new
        );
    }
}
