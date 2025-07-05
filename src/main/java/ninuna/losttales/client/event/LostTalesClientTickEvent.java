package ninuna.losttales.client.event;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import ninuna.losttales.LostTales;
import ninuna.losttales.client.LostTalesKeyMappings;
import ninuna.losttales.client.gui.screen.LostTalesQuestJournalScreen;
import org.slf4j.Logger;

@EventBusSubscriber(modid = LostTales.MOD_ID, value = Dist.CLIENT)
public class LostTalesClientTickEvent {
    private static final Logger LOGGER = LogUtils.getLogger();

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (LostTalesKeyMappings.QUEST_JOURNAL_MAPPING.get().consumeClick()) {
            // Open and display quest journal screen
            LOGGER.info(LostTales.MOD_ID + ": " + Minecraft.getInstance().getUser().getName() + " opened the quest journal");
            Minecraft.getInstance().setScreen(new LostTalesQuestJournalScreen(Minecraft.getInstance().screen));
        }
        else if (LostTalesKeyMappings.TOGGLE_HUD_MAPPING.get().consumeClick()) {
            // Toggle lost tales hud
            LOGGER.info("LOL!");
            //Todo: Pop Up "Hud: Enabled/Disabled"
            //Todo: Toggle Hud
        }
    }
}