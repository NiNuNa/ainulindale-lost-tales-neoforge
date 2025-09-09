package dev.ninuna.losttales.client.gui.hud.loot;

import net.minecraft.client.Minecraft;
import net.minecraft.world.Container;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import dev.ninuna.losttales.client.keymapping.LostTalesKeyMappingHelper;
import dev.ninuna.losttales.common.LostTales;

@EventBusSubscriber(modid = LostTales.MOD_ID, value = Dist.CLIENT)
public class LostTalesQuickLootHudScrollEvent {
    public static long LAST_SCROLL_TIME = 0;
    public static final long SCROLL_COOLDOWN_MS = 30;

    @SubscribeEvent
    public static void quickLootHudScrollEvent(InputEvent.MouseScrollingEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        long currentTime = System.currentTimeMillis();

        if (LostTalesQuickLootHudRenderer.getContainer(minecraft) != null) {
            Container container = LostTalesQuickLootHudRenderer.getContainer(minecraft);
            if (LostTalesKeyMappingHelper.isModifierKeyDown()) {
                event.setCanceled(true);
                if (currentTime - LAST_SCROLL_TIME < SCROLL_COOLDOWN_MS) return;
                int scrollDelta = event.getScrollDeltaY() > 0 ? -1 : 1;
                LostTalesQuickLootHudRenderer.moveSelectionIndex(container, scrollDelta);
                LAST_SCROLL_TIME = currentTime;
            }
        }
    }
}
