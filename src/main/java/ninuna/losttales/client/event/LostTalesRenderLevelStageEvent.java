package ninuna.losttales.client.event;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import ninuna.losttales.LostTales;
import org.joml.Matrix4f;

@EventBusSubscriber(modid = LostTales.MOD_ID, value = Dist.CLIENT)
public class LostTalesRenderLevelStageEvent {
    private static final ResourceLocation CONTAINER_HUD_TEXTURE = ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "textures/item/pear.png");

    @SubscribeEvent
    public static void onHoveringOverUrn(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_SKY) return;

        PoseStack poseStack = event.getPoseStack();
        Level level = event.getLevel();
        Minecraft mc = Minecraft.getInstance();

        if (mc.player == null) return;

        MultiBufferSource bufferSource = mc.renderBuffers().bufferSource();
        BlockPos pos = new BlockPos(109, 64, 109);

        // Todo: Remove Debug stuff.
        level.addParticle(ParticleTypes.DUST_PLUME, (double) pos.getX() + (double) 0.5f, (double) pos.getY() + 1.0f, (double) pos.getZ() + (double) 0.5f, 0.0f, 0.0f, 0.0f);
        //LostTales.LOGGER.info("Render Tick! Surely it will render!");

        // Push pose stack
        poseStack.pushPose();

        // Translate to world position
        poseStack.translate(pos.getX(), pos.getY(), pos.getZ());

        // Scale
        poseStack.scale(1.0f, 1.0f, 1.0f);

        // Matrix
        Matrix4f matrix4f = poseStack.last().pose();
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        // Draw Texture

        // Pop pose stack
        poseStack.popPose();
    }
}