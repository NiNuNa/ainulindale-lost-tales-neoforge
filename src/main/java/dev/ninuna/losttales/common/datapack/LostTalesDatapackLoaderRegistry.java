package dev.ninuna.losttales.common.datapack;

import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.datapack.loader.LostTalesMapMarkerDatapackLoader;
import dev.ninuna.losttales.common.datapack.loader.LostTalesQuestDatapackLoader;
import dev.ninuna.losttales.common.quest.LostTalesQuestServices;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;

@EventBusSubscriber(modid = LostTales.MOD_ID)
public class LostTalesDatapackLoaderRegistry {

    @SubscribeEvent
    public static void addServerReloadListeners(AddServerReloadListenersEvent event) {
        event.addListener(LostTalesDatapackRegistry.MAP_MARKERS_REGISTRY_KEY.location(), new LostTalesMapMarkerDatapackLoader());

        var questLoader = new LostTalesQuestDatapackLoader();
        LostTalesQuestServices.set(questLoader);
        event.addListener(LostTalesDatapackRegistry.QUESTS_REGISTRY_KEY.location(), questLoader);
    }
}
