package dev.ninuna.losttales.common.data.pack;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.mapmarker.LostTalesMapMarkerData;
import dev.ninuna.losttales.common.mapmarker.LostTalesMapMarkerDataReloadListener;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

@EventBusSubscriber(modid = LostTales.MOD_ID)
public class LostTalesDataPackEvents {

    @SubscribeEvent
    public static void onAddServerReloadListeners(AddServerReloadListenersEvent event) {
        event.addListener(
                ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "map_markers"),
                new LostTalesMapMarkerDataReloadListener(
                        LostTalesMapMarkerData.CODEC,
                        FileToIdConverter.json("map_marker")
                )
        );
    }
}
