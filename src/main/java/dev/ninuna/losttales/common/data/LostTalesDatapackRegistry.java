package dev.ninuna.losttales.common.data;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.mapmarker.LostTalesMapMarkerData;
import dev.ninuna.losttales.common.mapmarker.LostTalesMapMarkerDataReloadListener;
import net.minecraft.core.Registry;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = LostTales.MOD_ID)
public class LostTalesDatapackRegistry {
    public static final ResourceKey<Registry<LostTalesMapMarkerData.Entry>> MAP_MARKERS_REGISTRY_KEY = ResourceKey.createRegistryKey(LostTales.getResourceLocation("map_markers"));

    @SubscribeEvent
    public static void newDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                MAP_MARKERS_REGISTRY_KEY,
                LostTalesMapMarkerData.Entry.CODEC,
                LostTalesMapMarkerData.Entry.CODEC
        );
    }

    @SubscribeEvent
    public static void addServerReloadListeners(AddServerReloadListenersEvent event) {
        event.addListener(
                LostTales.getResourceLocation("map_markers"), new LostTalesMapMarkerDataReloadListener(
                        LostTalesMapMarkerData.MAP_CODEC.codec(),
                        FileToIdConverter.json("map_marker")
                )
        );
    }
}
