package dev.ninuna.losttales.common.network.packet;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.client.cache.LostTalesClientMobAggroCache;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;

public record LostTalesSyncMobAggroPacket(List<Integer> ids) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<LostTalesSyncMobAggroPacket> TYPE = new CustomPacketPayload.Type<>(LostTales.getResourceLocation("aggroed_mobs"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, LostTalesSyncMobAggroPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(ArrayList::new, ByteBufCodecs.VAR_INT),
            LostTalesSyncMobAggroPacket::ids,
            LostTalesSyncMobAggroPacket::new
    );

    public static void handle(LostTalesSyncMobAggroPacket lostTalesLockedOnTargetPacket, IPayloadContext context) {
        context.enqueueWork(() -> {
            LostTalesClientMobAggroCache.accept(lostTalesLockedOnTargetPacket.ids());
        });
    }
}
