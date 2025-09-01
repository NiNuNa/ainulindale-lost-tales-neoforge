package dev.ninuna.losttales.client.keymapping;

public class LostTalesKeyMappingHelper {
    private static boolean isModifierKeyDown = false;

    public static void setIsModifierKeyDown(boolean isModifierKeyDown) {
        LostTalesKeyMappingHelper.isModifierKeyDown = isModifierKeyDown;
    }

    public static boolean isModifierKeyDown() {
        return isModifierKeyDown;
    }
}
