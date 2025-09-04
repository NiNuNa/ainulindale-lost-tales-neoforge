package dev.ninuna.losttales.common.data.attachment;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.mapmarker.LostTalesMapMarkerData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.attachment.AttachmentType;

import java.util.function.Supplier;

public final class LostTalesAttachments {
    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, LostTales.MOD_ID);

    public static final Supplier<AttachmentType<LostTalesMapMarkerData>> LEVEL_MARKERS = ATTACHMENT_TYPES.register(
            "level_map_markers", () -> AttachmentType.builder(LostTalesMapMarkerData::new)
                    .serialize(LostTalesMapMarkerData.MAP_CODEC)
                    .build()
    );

    public static final Supplier<AttachmentType<LostTalesMapMarkerData>> PLAYER_MARKERS = ATTACHMENT_TYPES.register(
            "player_map_markers", () -> AttachmentType.builder(LostTalesMapMarkerData::new)
                    .serialize(LostTalesMapMarkerData.MAP_CODEC)
                    .build()
    );

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
