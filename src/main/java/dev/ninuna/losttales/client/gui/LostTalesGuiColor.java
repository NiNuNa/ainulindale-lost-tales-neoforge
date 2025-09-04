package dev.ninuna.losttales.client.gui;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;

public enum LostTalesGuiColor implements StringRepresentable {
    WHITE   (0xFFFBDE),
    RED     (0xFF0000),
    BLUE    (0x0000FF);

    private static final int ALPHA_SHIFT = 24;
    public static final Codec<LostTalesGuiColor> CODEC = StringRepresentable.fromEnum(LostTalesGuiColor::values);

    private final int colorRgb;

    LostTalesGuiColor(int colorRgb) {
        this.colorRgb = colorRgb;
    }

    public int getColorRgb() {
        return this.colorRgb;
    }

    public int getColorArgb(float alpha) {
        int alphaClamp = Math.min(255, Math.max(0, Math.round(alpha * 255)));
        return (alphaClamp << ALPHA_SHIFT) | this.colorRgb;
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
