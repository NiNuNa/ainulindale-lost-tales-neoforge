package ninuna.losttales.client.gui.toast;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class LostTalesToast implements Toast {
    private static final ResourceLocation BACKGROUND_SPRITE = ResourceLocation.withDefaultNamespace("toast/system");

    private Toast.Visibility wantedVisibility;
    private Component message;
    private double displayTime;

    public LostTalesToast(Component message, double displayTime) {
        this.wantedVisibility = Visibility.HIDE;
        this.message = message;
        this.displayTime = displayTime;
    }

    @Override
    public Visibility getWantedVisibility() {
        return this.wantedVisibility;
    }

    @Override
    public void update(ToastManager toastManager, long l) {

    }

    @Override
    public void render(GuiGraphics guiGraphics, Font font, long l) {

    }
}
