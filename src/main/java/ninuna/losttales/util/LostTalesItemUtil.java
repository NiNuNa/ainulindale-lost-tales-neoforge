package ninuna.losttales.util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import ninuna.losttales.LostTales;

import java.util.function.Consumer;

public class LostTalesItemUtil {

    public static void addHoverText(ItemStack stack, Consumer<Component> tooltipAdder) {
        if (Screen.hasShiftDown()) {
            tooltipAdder.accept(Component.translatable("tooltip." + LostTales.MOD_ID + "." + stack.getItemName().toString().substring(32, stack.getItemName().toString().length() - 11)).withStyle(ChatFormatting.GRAY));
        } else {
            tooltipAdder.accept(Component.translatable("tooltip." + LostTales.MOD_ID + ".open"));
        }
    }
}