package ninuna.losttales.client.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import ninuna.losttales.LostTales;

@EventBusSubscriber(modid = LostTales.MOD_ID, value = Dist.CLIENT)
public class LostTalesRenderLevelStageEvent {

    @SubscribeEvent
    public static void renderQuestMarker(RenderLevelStageEvent event) {

    }
}
