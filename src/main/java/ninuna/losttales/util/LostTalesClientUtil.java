package ninuna.losttales.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import ninuna.losttales.config.LostTalesConfigs;
import org.joml.Matrix4f;

public class LostTalesClientUtil {
    private static boolean isModifierKeyDown = false;

    public static void setIsModifierKeyDown(boolean isModifierKeyDown) {
        LostTalesClientUtil.isModifierKeyDown = isModifierKeyDown;
    }

    public static boolean isModifierKeyDown() {
        return isModifierKeyDown;
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
