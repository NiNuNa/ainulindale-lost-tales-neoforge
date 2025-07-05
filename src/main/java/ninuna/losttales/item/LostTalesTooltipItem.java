package ninuna.losttales.item;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import ninuna.losttales.LostTales;

import java.util.function.Consumer;

public class LostTalesTooltipItem extends Item {

    public LostTalesTooltipItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        tooltipAdder.accept(Component.empty());

        if (Screen.hasShiftDown()) {
            tooltipAdder.accept(Component.translatable("tooltip." + LostTales.MOD_ID + "." + stack.getItemName().toString().substring(32, stack.getItemName().toString().length() - 11) + ".1").withStyle(ChatFormatting.GRAY));
            tooltipAdder.accept(Component.translatable("tooltip." + LostTales.MOD_ID + "." + stack.getItemName().toString().substring(32, stack.getItemName().toString().length() - 11) + ".2").withStyle(ChatFormatting.GRAY));
        } else {
            tooltipAdder.accept(Component.translatable("tooltip." + LostTales.MOD_ID + ".open"));
        }

        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    }
}