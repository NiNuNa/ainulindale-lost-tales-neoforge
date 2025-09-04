package dev.ninuna.losttales.common.data.pack;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.mapmarker.LostTalesMapMarkerData;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = LostTales.MOD_ID)
public class LostTalesDatapackRegistry {
    public static final ResourceKey<Registry<LostTalesMapMarkerData.Entry>> MAP_MARKER_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "map_markers"));

    @SubscribeEvent
    public static void newDatapackRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                MAP_MARKER_REGISTRY_KEY,
                LostTalesMapMarkerData.Entry.CODEC,
                LostTalesMapMarkerData.Entry.CODEC
        );
    }
}
