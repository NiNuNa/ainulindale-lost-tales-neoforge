package ninuna.losttales.client.event;

import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import ninuna.losttales.LostTales;
import ninuna.losttales.block.entity.LostTalesBlockEntities;
import ninuna.losttales.block.entity.renderer.LostTalesPlushieBlockEntityRenderer;
import org.slf4j.Logger;

@EventBusSubscriber(modid = LostTales.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)

public class LostTalesRegisterRenderersEvent {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        LOGGER.info(LostTales.MOD_ID + ": REGISTERING RENDERERS");
        event.registerBlockEntityRenderer(LostTalesBlockEntities.PLUSHIE.get(), context -> new LostTalesPlushieBlockEntityRenderer());
    }
}