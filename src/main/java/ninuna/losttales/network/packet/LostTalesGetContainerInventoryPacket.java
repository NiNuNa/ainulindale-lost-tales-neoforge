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
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import ninuna.losttales.LostTales;

public record LostTalesGetContainerInventoryPacket(int x, int y, int z) implements CustomPacketPayload {
    public static final Type<LostTalesGetContainerInventoryPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "quick_loot_hud_server"));
    public static final StreamCodec<ByteBuf, LostTalesGetContainerInventoryPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, LostTalesGetContainerInventoryPacket::x,
            ByteBufCodecs.VAR_INT, LostTalesGetContainerInventoryPacket::y,
            ByteBufCodecs.VAR_INT, LostTalesGetContainerInventoryPacket::z,
            LostTalesGetContainerInventoryPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {

        });
    }
}
