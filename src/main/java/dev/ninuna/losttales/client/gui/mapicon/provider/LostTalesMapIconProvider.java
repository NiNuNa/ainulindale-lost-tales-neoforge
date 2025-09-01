package dev.ninuna.losttales.client.gui.mapicon.provider;

import net.minecraft.client.Minecraft;
import dev.ninuna.losttales.client.gui.mapicon.LostTalesMapIcon;

import java.util.List;

public interface LostTalesMapIconProvider {
    List<LostTalesMapIcon> collect(Minecraft mc);
}
