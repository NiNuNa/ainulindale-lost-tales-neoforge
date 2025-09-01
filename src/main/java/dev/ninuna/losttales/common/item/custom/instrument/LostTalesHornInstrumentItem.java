package dev.ninuna.losttales.common.item.custom.instrument;

import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import dev.ninuna.losttales.common.item.custom.LostTalesTooltipItem;

public class LostTalesHornInstrumentItem extends LostTalesTooltipItem {
    private final int customCooldownDuration;

    public LostTalesHornInstrumentItem(Properties properties) {
        super(properties);
        this.customCooldownDuration = 0;
    }

    public LostTalesHornInstrumentItem(Properties properties, int customCooldownDuration) {
        super(properties);
        this.customCooldownDuration = customCooldownDuration;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        //playInstrument(p_220123_, p_220124_, instrument);
        if (this.customCooldownDuration > 0) {
            player.getCooldowns().addCooldown(itemStack, Mth.floor(this.customCooldownDuration * 20.0f));
        } else {
            int defaultCooldownDuration = 4;
            player.getCooldowns().addCooldown(itemStack, Mth.floor(defaultCooldownDuration * 20.0f));
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        return InteractionResult.CONSUME;
    }

    @Override
    public ItemUseAnimation getUseAnimation(ItemStack stack) {
        return ItemUseAnimation.TOOT_HORN;
    }

    private void playInstrument() {
        // Todo
    }
}