package dev.ninuna.losttales.common.network.packet;

import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarker;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.mapmarker.LostTalesMapMarkerData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.Collection;

public record LostTalesSyncMapMarkersPacket(boolean isLevelMapMarker, Collection<LostTalesMapMarkerData.Entry> mapMarkers) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<LostTalesSyncMapMarkersPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "sync_map_markers"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    private static final StreamCodec<ByteBuf, LostTalesMapMarkerData.Entry>
            ENTRY_STREAM_CODEC = ByteBufCodecs.fromCodec(LostTalesMapMarkerData.Entry.CODEC);

    public static final StreamCodec<RegistryFriendlyByteBuf, LostTalesSyncMapMarkersPacket> STREAM_CODEC = StreamCodec.composite(
                    ByteBufCodecs.BOOL, LostTalesSyncMapMarkersPacket::isLevelMapMarker,
                    ByteBufCodecs.collection(ArrayList::new, ENTRY_STREAM_CODEC), LostTalesSyncMapMarkersPacket::mapMarkers,
                    LostTalesSyncMapMarkersPacket::new
    );

    public void handle(IPayloadContext context) {
        context.enqueueWork(() -> {
            if (isLevelMapMarker) {
                LostTalesMapMarker.setShared(mapMarkers);
            } else {
                LostTalesMapMarker.setPersonal(mapMarkers);
            }
            // Debug (client only):
            LostTales.LOGGER.info("[LostTales] received " + mapMarkers.size() + " markers");
        });
    }
}
