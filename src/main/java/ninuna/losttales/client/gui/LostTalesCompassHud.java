package ninuna.losttales.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import ninuna.losttales.client.util.LostTalesGuiUtil;

public class LostTalesCompassHud {

    public static void renderHud(Minecraft minecraft, GuiGraphics guiGraphics) {
        guiGraphics.drawString(minecraft.font, "TODO", 15, 25, LostTalesGuiUtil.COLOR_WHITE);
    }
}
