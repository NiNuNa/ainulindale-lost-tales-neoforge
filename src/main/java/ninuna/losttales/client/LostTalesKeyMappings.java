package ninuna.losttales.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.logging.LogUtils;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.jarjar.nio.util.Lazy;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import ninuna.losttales.LostTales;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;

@EventBusSubscriber(modid = LostTales.MOD_ID, value = Dist.CLIENT)
public class LostTalesKeyMappings {
    // Key mapping translation keys
    public static final String LOSTTALES_MAPPING_CATEGORY_LANG_KEY = "key.categories.losttales.mappings";
    public static final String QUEST_JOURNAL_MAPPING_LANG_KEY = "key.losttales.questJournal";
    public static final String TOGGLE_HUD_MAPPING_LANG_KEY = "key.losttales.toggleHud";

    // Key mappings
    public static final Lazy<KeyMapping> QUEST_JOURNAL_MAPPING = Lazy.of(new KeyMapping(QUEST_JOURNAL_MAPPING_LANG_KEY, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_J, LOSTTALES_MAPPING_CATEGORY_LANG_KEY));
    public static final Lazy<KeyMapping> TOGGLE_HUD_MAPPING = Lazy.of(new KeyMapping(TOGGLE_HUD_MAPPING_LANG_KEY, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, LOSTTALES_MAPPING_CATEGORY_LANG_KEY));

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        LostTales.LOGGER.info(LostTales.MOD_ID + ": REGISTERING KEYBINDS");
        event.register(QUEST_JOURNAL_MAPPING.get());
        event.register(TOGGLE_HUD_MAPPING.get());
    }
}