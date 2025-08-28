package ninuna.losttales.client.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import ninuna.losttales.client.event.LostTalesModConfigEvent;
import ninuna.losttales.config.LostTalesConfigs;
import ninuna.losttales.config.custom.LostTalesClientConfig;
import org.joml.Matrix4f;

public class LostTalesGuiUtil {
    public static final int COLOR_WHITE = 0xFFFFFBDE;
    public static final int COLOR_WHITE_FADE = 0x00FFFBDE;

    public static void toggleLostTalesHud() {
        LostTalesClientConfig clientConfig = LostTalesConfigs.CLIENT;

        clientConfig.showLostTalesHud.set(!clientConfig.showLostTalesHud.get());
        LostTalesModConfigEvent.syncLinkedConfigOptions();
        LostTalesConfigs.CLIENT_SPEC.save();

        renderToast();
    }

    public static void renderToast() {
        Component message;
        if (LostTalesConfigs.CLIENT.showLostTalesHud.get()) {
            message = Component.literal("Lost Tales HUD: ON");
        } else {
            message = Component.literal("Lost Tales HUD: OFF");
        }
        Minecraft.getInstance().getToastManager().addToast(new SystemToast(SystemToast.SystemToastId.NARRATOR_TOGGLE, message, null));
    }

    public static void renderHorizontalFade(GuiGraphics g, int x1, int y1, int x2, int y2, int leftColor, int rightColor) {
        renderHorizontalFade(g, x1, y1, x2, y2, 0, leftColor, rightColor);
    }

    public static void renderHorizontalFade(GuiGraphics guiGraphics, int x1, int y1, int x2, int y2, int z, int leftColor, int rightColor) {
        VertexConsumer vertexConsumer = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.gui());
        Matrix4f matrix4f = guiGraphics.pose().last().pose();

        vertexConsumer.addVertex(matrix4f, (float)x1, (float)y1, (float)z).setColor(leftColor);
        vertexConsumer.addVertex(matrix4f, (float)x1, (float)y2, (float)z).setColor(leftColor);
        vertexConsumer.addVertex(matrix4f, (float)x2, (float)y2, (float)z).setColor(rightColor);
        vertexConsumer.addVertex(matrix4f, (float)x2, (float)y1, (float)z).setColor(rightColor);
    }
}
