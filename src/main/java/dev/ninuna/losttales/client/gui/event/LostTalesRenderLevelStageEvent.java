package dev.ninuna.losttales.client.gui.event;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.ninuna.losttales.client.gui.mapmarker.LostTalesMapMarkerRenderManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import dev.ninuna.losttales.common.LostTales;

@EventBusSubscriber(modid = LostTales.MOD_ID, value = Dist.CLIENT)
public class LostTalesRenderLevelStageEvent {

    @SubscribeEvent
    public static void renderLevelMapMarker(RenderLevelStageEvent.AfterBlockEntities event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        ClientLevel level = minecraft.level;
        PoseStack poseStack = event.getPoseStack();

        if (minecraft.isPaused() || player == null || level == null) return;

        LostTalesMapMarkerRenderManager mapMarkerRenderManager = new LostTalesMapMarkerRenderManager();
        mapMarkerRenderManager.renderMapMarkersOnLevel(minecraft, poseStack, level);

    }
}
