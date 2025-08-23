package ninuna.losttales.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.world.Container;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import ninuna.losttales.LostTales;
import ninuna.losttales.client.gui.LostTalesQuickLootHud;
import ninuna.losttales.util.LostTalesClientUtil;

@EventBusSubscriber(modid = LostTales.MOD_ID, value = Dist.CLIENT)
public class LostTalesQuickLootHudScrollEvent {
    public static long LAST_SCROLL_TIME = 0;
    public static final long SCROLL_COOLDOWN_MS = 40;

    @SubscribeEvent
    public static void quickLootHudScrollEvent(InputEvent.MouseScrollingEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        long currentTime = System.currentTimeMillis();

        if (LostTalesQuickLootHud.getContainer(minecraft) != null) {
            Container container = LostTalesQuickLootHud.getContainer(minecraft);
            if (LostTalesClientUtil.isModifierKeyDown()) {
                event.setCanceled(true);
                if (currentTime - LAST_SCROLL_TIME < SCROLL_COOLDOWN_MS) return;
                int scrollDelta = event.getScrollDeltaY() > 0 ? -1 : 1;
                LostTalesQuickLootHud.moveSelectionIndex(container, scrollDelta);
                LAST_SCROLL_TIME = currentTime;
            }
        }
    }
}
