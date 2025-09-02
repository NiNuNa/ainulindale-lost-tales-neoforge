package dev.ninuna.losttales.client.keymapping;

import dev.ninuna.losttales.common.LostTales;

public class LostTalesKeyMappingHelper {
    private static boolean isModifierKeyDown = false;

    public static void setIsModifierKeyDown(boolean isModifierKeyDown) {
        LostTalesKeyMappingHelper.isModifierKeyDown = isModifierKeyDown;
    }

    public static boolean isModifierKeyDown() {
        return isModifierKeyDown;
    }

    public static String getKeyMappingLangKey(String name) {
        return "key." + LostTales.MOD_ID + "." + name;
    }
}
