package ninuna.losttales.client.gui.mapiconprovider;

import net.minecraft.client.Minecraft;
import ninuna.losttales.client.gui.LostTalesCompassHud;

import java.util.List;

public class LostTalesQuestMapIconProvider implements LostTalesCompassHud.IconProvider {
    @Override
    public List<LostTalesCompassHud.MapIcon> collect(Minecraft mc) {
        return List.of();
    }
}
