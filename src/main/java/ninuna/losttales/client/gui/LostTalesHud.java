package ninuna.losttales.client.gui;

import ninuna.losttales.client.event.LostTalesModConfigEvent;
import ninuna.losttales.config.LostTalesConfigs;
import ninuna.losttales.util.LostTalesClientUtil;

public class LostTalesHud {

    public static void toggleLostTalesHud() {
        LostTalesConfigs.CLIENT.showLostTalesHud.set(!LostTalesConfigs.CLIENT.showLostTalesHud.get());
        LostTalesModConfigEvent.syncDependentConfigOptions();
        LostTalesConfigs.CLIENT_SPEC.save();

        LostTalesClientUtil.renderToast();
    }
}
