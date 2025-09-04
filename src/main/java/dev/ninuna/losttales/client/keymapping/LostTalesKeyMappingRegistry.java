package dev.ninuna.losttales.client.keymapping;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.jarjar.nio.util.Lazy;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import dev.ninuna.losttales.common.LostTales;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = LostTales.MOD_ID, value = Dist.CLIENT)
public class LostTalesKeyMappingRegistry {
    // Translation keys>
    public static final String LOSTTALES_MAPPING_CATEGORY_LANG_KEY = "key.categories." + LostTales.MOD_ID + ".mappings";

    // Key mappings
    public static final Lazy<KeyMapping> CHARACTER_MENU_MAPPING = Lazy.of(new KeyMapping(LostTalesKeyMappingHelper.getKeyMappingLangKey("characterMenu"), InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_CAPS_LOCK, LOSTTALES_MAPPING_CATEGORY_LANG_KEY));
    public static final Lazy<KeyMapping> QUEST_JOURNAL_MAPPING = Lazy.of(new KeyMapping(LostTalesKeyMappingHelper.getKeyMappingLangKey("questJournal"), InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_J, LOSTTALES_MAPPING_CATEGORY_LANG_KEY));
    public static final Lazy<KeyMapping> TOGGLE_HUD_MAPPING = Lazy.of(new KeyMapping(LostTalesKeyMappingHelper.getKeyMappingLangKey("toggleHud"), InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_H, LOSTTALES_MAPPING_CATEGORY_LANG_KEY));
    public static final Lazy<KeyMapping> USE_MAPPING = Lazy.of(new KeyMapping(LostTalesKeyMappingHelper.getKeyMappingLangKey("use"), InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, LOSTTALES_MAPPING_CATEGORY_LANG_KEY));
    public static final Lazy<KeyMapping> MODIFIER_MAPPING = Lazy.of(new KeyMapping(LostTalesKeyMappingHelper.getKeyMappingLangKey("modifier"), InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_ALT, LOSTTALES_MAPPING_CATEGORY_LANG_KEY));

    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        LostTales.LOGGER.info(LostTales.MOD_ID + ": REGISTERING KEYBINDS");
        event.register(CHARACTER_MENU_MAPPING.get());
        event.register(QUEST_JOURNAL_MAPPING.get());
        event.register(TOGGLE_HUD_MAPPING.get());
        event.register(USE_MAPPING.get());
        event.register(MODIFIER_MAPPING.get());
    }
}
