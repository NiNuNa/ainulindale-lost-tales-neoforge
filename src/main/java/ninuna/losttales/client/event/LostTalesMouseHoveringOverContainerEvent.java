package ninuna.losttales.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import ninuna.losttales.LostTales;
import ninuna.losttales.client.gui.LostTalesQuickLootHud;
import ninuna.losttales.config.LostTalesConfigs;

@EventBusSubscriber(modid = LostTales.MOD_ID, value = Dist.CLIENT)
public class LostTalesMouseHoveringOverContainerEvent {
    private static boolean WAS_LOOKING_AT_CONTAINER = false;

    @SubscribeEvent
    public static void onMouseHoveringOverContainer(RenderGuiLayerEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        boolean isCurrentlyLookingAtContainer = false;

        if (mc.crosshairPickEntity == null && mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) mc.hitResult;
            BlockPos blockPos = blockHit.getBlockPos();
            Level level = mc.level;
            if (level != null && level.getBlockEntity(blockPos) != null && !mc.options.hideGui && LostTalesConfigs.CLIENT.showQuickLootHud.get()) {
                if (level.getBlockEntity(blockPos) instanceof Container container) {
                    isCurrentlyLookingAtContainer = true;
                    LostTalesQuickLootHud.renderHud(mc, event.getGuiGraphics(), container);
                }
            }
        }

        if (!isCurrentlyLookingAtContainer && WAS_LOOKING_AT_CONTAINER) {
            LostTalesQuickLootHud.resetHud();
        }
        WAS_LOOKING_AT_CONTAINER = isCurrentlyLookingAtContainer;
    }
}
