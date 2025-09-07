package dev.ninuna.losttales.common.datapack;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.mapmarker.LostTalesMapMarkerData;
import dev.ninuna.losttales.common.quest.LostTalesQuest;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = LostTales.MOD_ID)
public class LostTalesDatapackRegistry {
    public static final ResourceKey<Registry<LostTalesMapMarkerData.Entry>> MAP_MARKERS_REGISTRY_KEY = ResourceKey.createRegistryKey(LostTales.getResourceLocation("map_markers"));
    public static final ResourceKey<Registry<LostTalesQuest>> QUESTS_REGISTRY_KEY = ResourceKey.createRegistryKey(LostTales.getResourceLocation("quests"));

    @SubscribeEvent
    public static void newDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                MAP_MARKERS_REGISTRY_KEY, LostTalesMapMarkerData.Entry.CODEC, LostTalesMapMarkerData.Entry.CODEC
        );

        event.dataPackRegistry(
                QUESTS_REGISTRY_KEY, LostTalesQuest.CODEC, LostTalesQuest.CODEC
        );
    }
}
