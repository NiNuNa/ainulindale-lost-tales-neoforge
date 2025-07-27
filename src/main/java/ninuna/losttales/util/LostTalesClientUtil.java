package ninuna.losttales.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import ninuna.losttales.config.LostTalesConfigs;

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
}
