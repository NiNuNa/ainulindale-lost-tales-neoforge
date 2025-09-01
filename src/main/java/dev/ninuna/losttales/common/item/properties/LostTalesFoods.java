package dev.ninuna.losttales.common.item.properties;

import net.minecraft.world.food.FoodProperties;

public class LostTalesFoods {
    public static final FoodProperties PLUM = (new FoodProperties.Builder()).nutrition(1).saturationModifier(0.1f).alwaysEdible().build();
    public static final FoodProperties PEAR = (new FoodProperties.Builder()).nutrition(3).saturationModifier(0.3f).alwaysEdible().build();
    public static final FoodProperties PEAR_BAKED = (new FoodProperties.Builder()).nutrition(6).saturationModifier(0.6F).alwaysEdible().build();
    public static final FoodProperties PEAR_JUICE = (new FoodProperties.Builder()).nutrition(2).saturationModifier(0.1F).alwaysEdible().build();
    public static final FoodProperties CHEESE_WHEEL = (new FoodProperties.Builder()).nutrition(8).saturationModifier(0.6f).alwaysEdible().build();
}