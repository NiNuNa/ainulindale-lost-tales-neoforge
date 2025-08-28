package ninuna.losttales.client.util;

public class LostTalesKeyMappingUtil {
    private static boolean isModifierKeyDown = false;

    public static void setIsModifierKeyDown(boolean isModifierKeyDown) {
        LostTalesKeyMappingUtil.isModifierKeyDown = isModifierKeyDown;
    }

    public static boolean isModifierKeyDown() {
        return isModifierKeyDown;
    }
}
