package ninuna.losttales.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import ninuna.losttales.util.LostTalesItemUtil;

import java.util.function.Consumer;

public class LostTalesTooltipItem extends Item {

    public LostTalesTooltipItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        LostTalesItemUtil.addHoverText(stack, tooltipAdder);
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    }
}
