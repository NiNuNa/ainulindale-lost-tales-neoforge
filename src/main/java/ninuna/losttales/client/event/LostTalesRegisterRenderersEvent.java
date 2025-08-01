package ninuna.losttales.client.event;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import ninuna.losttales.LostTales;
import ninuna.losttales.block.entity.LostTalesBlockEntities;
import ninuna.losttales.block.entity.renderer.LostTalesPlushieBlockEntityRenderer;
import ninuna.losttales.block.entity.renderer.LostTalesUrnBlockEntityRenderer;

@EventBusSubscriber(modid = LostTales.MOD_ID, value = Dist.CLIENT)
public class LostTalesRegisterRenderersEvent {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(LostTalesBlockEntities.PLUSHIE.get(), LostTalesPlushieBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(LostTalesBlockEntities.URN.get(), LostTalesUrnBlockEntityRenderer::new);
    }
}