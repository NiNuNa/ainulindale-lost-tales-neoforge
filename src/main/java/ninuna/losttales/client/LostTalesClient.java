package ninuna.losttales.client;

import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import ninuna.losttales.LostTales;

@Mod(value = LostTales.MOD_ID, dist = Dist.CLIENT)
public class LostTalesClient {
    public LostTalesClient(IEventBus modEventBus, ModContainer container) {
        modEventBus.addListener(this::onClientSetup);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    private void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        LostTales.LOGGER.info("HELLO FROM CLIENT SETUP");
        LostTales.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
    }
}