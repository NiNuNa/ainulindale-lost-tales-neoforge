package dev.ninuna.losttales.client.gui.hud;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import dev.ninuna.losttales.common.config.LostTalesModConfigEvent;
import dev.ninuna.losttales.common.config.LostTalesConfigs;
import dev.ninuna.losttales.common.config.custom.LostTalesClientConfig;

public class LostTalesHudHelper {
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
}
