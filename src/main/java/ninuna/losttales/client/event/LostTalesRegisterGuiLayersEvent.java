package ninuna.losttales.client.event;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import ninuna.losttales.LostTales;
import ninuna.losttales.client.gui.LostTalesCompassHud;
import ninuna.losttales.client.gui.LostTalesQuickLootHud;
import ninuna.losttales.config.LostTalesConfigs;

@EventBusSubscriber(modid = LostTales.MOD_ID, value = Dist.CLIENT)
public class LostTalesRegisterGuiLayersEvent {
    public static final ResourceLocation COMPASS_HUD = ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "compass_hud");
    public static final ResourceLocation QUICK_LOOT_HUD = ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "quick_loot_hud");

    private static boolean WAS_LOOKING_AT_CONTAINER = false;

    @SubscribeEvent
    public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerBelow(VanillaGuiLayers.HOTBAR, QUICK_LOOT_HUD, LostTalesRegisterGuiLayersEvent::renderQuickLootHud);
        event.registerAbove(QUICK_LOOT_HUD, COMPASS_HUD, LostTalesRegisterGuiLayersEvent::renderCompassHud);
    }

    private static void renderQuickLootHud(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        boolean isCurrentlyLookingAtContainer = false;

        if (mc.crosshairPickEntity == null && mc.hitResult != null && mc.hitResult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockHit = (BlockHitResult) mc.hitResult;
            BlockPos blockPos = blockHit.getBlockPos();
            Level level = mc.level;
            if (level != null && level.getBlockEntity(blockPos) != null && !mc.options.hideGui && LostTalesConfigs.CLIENT.showQuickLootHud.get()) {
                if (level.getBlockEntity(blockPos) instanceof Container container) {
                    isCurrentlyLookingAtContainer = true;
                    LostTalesQuickLootHud.renderHud(mc, guiGraphics, container);
                }
            }
        }

        if (!isCurrentlyLookingAtContainer && WAS_LOOKING_AT_CONTAINER) {
            LostTalesQuickLootHud.resetHud();
        }
        WAS_LOOKING_AT_CONTAINER = isCurrentlyLookingAtContainer;
    }

    private static void renderCompassHud(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        if (!mc.options.hideGui && LostTalesConfigs.CLIENT.showCompassHud.get()) {
            LostTalesCompassHud.renderHud(mc, guiGraphics);
        }
    }
}
