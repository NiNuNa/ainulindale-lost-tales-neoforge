package ninuna.losttales.item.properties;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.Consumable;

public class LostTalesItemProperties {

    public static Item.Properties urnItemProperties(ResourceLocation registryName) {
        return baseItemProperties(registryName)
                .stacksTo(16);
    }

    public static Item.Properties plushieItemProperties(ResourceLocation registryName, Rarity rarity) {
        return baseItemProperties(registryName)
                .rarity(rarity)
                .stacksTo(1);
    }

    public static Item.Properties foodItemProperties(ResourceLocation registryName, FoodProperties food) {
        return baseItemProperties(registryName)
                .food(food);
    }

    public static Item.Properties cheeseWheelItemProperties(ResourceLocation registryName, FoodProperties food) {
        return foodItemProperties(registryName, food)
                .stacksTo(1);
    }

    public static Item.Properties drinkItemProperties(ResourceLocation registryName, FoodProperties food, Consumable consumable) {
        return baseItemProperties(registryName)
                .food(food, consumable)
                .craftRemainder(Items.GLASS_BOTTLE)
                .usingConvertsTo(Items.GLASS_BOTTLE)
                .stacksTo(16);
    }

    public static Item.Properties toolItemProperties(ResourceLocation registryName) {
        return baseItemProperties(registryName)
                .stacksTo(1);
    }

    public static Item.Properties instrumentItemProperties(ResourceLocation registryName, Rarity rarity) {
        return baseItemProperties(registryName)
                .stacksTo(1)
                .rarity(rarity);
    }

    public static Item.Properties baseItemProperties(ResourceLocation registryName) {
        return new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, registryName));
    }
}