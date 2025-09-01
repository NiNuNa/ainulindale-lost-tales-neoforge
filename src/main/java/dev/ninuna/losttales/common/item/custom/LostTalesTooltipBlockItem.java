package dev.ninuna.losttales.common.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import dev.ninuna.losttales.common.item.LostTalesItemHelper;

import java.util.function.Consumer;

public class LostTalesTooltipBlockItem extends BlockItem {

    public LostTalesTooltipBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
        LostTalesItemHelper.addHoverText(stack, tooltipAdder);
        super.appendHoverText(stack, context, tooltipDisplay, tooltipAdder, flag);
    }
}
