package ninuna.losttales.client.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import ninuna.losttales.LostTales;
import ninuna.losttales.config.LostTalesConfigs;

@EventBusSubscriber(modid = LostTales.MOD_ID, value = Dist.CLIENT)
public class LostTalesModConfigEvent {

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == LostTalesConfigs.CLIENT_SPEC) {
            syncDependentConfigOptions();
        }
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == LostTalesConfigs.CLIENT_SPEC) {
            syncDependentConfigOptions();
        }
    }

    public static void syncDependentConfigOptions() {
        if (LostTalesConfigs.CLIENT.linkShowQuickLootHud.get()) {
            LostTalesConfigs.CLIENT.showQuickLootHud.set(LostTalesConfigs.CLIENT.showLostTalesHud.get());
        }
    }
}
