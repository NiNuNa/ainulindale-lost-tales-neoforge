package dev.ninuna.losttales.common.network.packet;

import dev.ninuna.losttales.client.cache.LostTalesClientQuestCache;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.quest.LostTalesQuestPlayerData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.List;

public record LostTalesSyncQuestsPacket(List<LostTalesQuestPlayerData.QuestProgress> questProgresses) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<LostTalesSyncQuestsPacket> TYPE = new CustomPacketPayload.Type<>(LostTales.getResourceLocation("quest_progress"));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static final StreamCodec<RegistryFriendlyByteBuf, LostTalesSyncQuestsPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(LostTalesQuestPlayerData.QuestProgress.CODEC.listOf()),
            LostTalesSyncQuestsPacket::questProgresses,
            LostTalesSyncQuestsPacket::new
    );

    public static void handle(LostTalesSyncQuestsPacket lostTalesSyncQuestsPacket, IPayloadContext context) {
        context.enqueueWork(() -> {
            var map = new HashMap<ResourceLocation, LostTalesQuestPlayerData.QuestProgress>();
            for (var questProgress : lostTalesSyncQuestsPacket.questProgresses()) {
                map.put(questProgress.questId, questProgress);
            }
            LostTalesClientQuestCache.get().replaceAll(map);
        });
    }
}
