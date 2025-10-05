package dev.ninuna.losttales.client;

import dev.ninuna.losttales.common.block.entity.LostTalesBlockEntities;
import dev.ninuna.losttales.common.block.entity.renderer.LostTalesPlushieBlockEntityRenderer;
import dev.ninuna.losttales.common.block.entity.renderer.LostTalesUrnBlockEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import dev.ninuna.losttales.common.LostTales;

@Mod(value = LostTales.MOD_ID, dist = Dist.CLIENT)
public class LostTalesClient {
    public LostTalesClient(IEventBus modEventBus, ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        modEventBus.addListener(this::registerEntityRenderers);
    }

    private void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        LostTales.LOGGER.info("[{}] Registering entity renderers", LostTales.MOD_ID);

        event.registerBlockEntityRenderer(LostTalesBlockEntities.PLUSHIE.get(), LostTalesPlushieBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(LostTalesBlockEntities.URN.get(), LostTalesUrnBlockEntityRenderer::new);
    }
}
