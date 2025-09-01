package dev.ninuna.losttales.client.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.client.gui.hud.LostTalesQuickLootHud;
import dev.ninuna.losttales.common.config.LostTalesConfigs;
import dev.ninuna.losttales.common.config.custom.LostTalesClientConfig;

@EventBusSubscriber(modid = LostTales.MOD_ID, value = Dist.CLIENT)
public class LostTalesModConfigEvent {

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == LostTalesConfigs.CLIENT_SPEC) {
            syncLinkedConfigOptions();
        }
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == LostTalesConfigs.CLIENT_SPEC) {
            syncLinkedConfigOptions();
        }
    }

    public static void syncLinkedConfigOptions() {
        LostTalesClientConfig clientConfig = LostTalesConfigs.CLIENT;
        boolean showLostTalesHud = clientConfig.showLostTalesHud.get();

        if (clientConfig.linkShowQuickLootHud.get()) {
            clientConfig.showQuickLootHud.set(showLostTalesHud);
        }
        if (clientConfig.linkShowCompassHud.get()) {
            clientConfig.showCompassHud.set(showLostTalesHud);
        }

        LostTalesQuickLootHud.resetHud();
    }
}
