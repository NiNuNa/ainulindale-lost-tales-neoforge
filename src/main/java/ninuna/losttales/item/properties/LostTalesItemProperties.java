package ninuna.losttales.item.properties;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class LostTalesItemProperties {

    public static Item.Properties urnItemProperties(ResourceLocation registryName) {
        return new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, registryName))
                .stacksTo(16);
    }

    public static Item.Properties plushieItemProperties(ResourceLocation registryName, Rarity rarity) {
        return new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, registryName))
                .rarity(rarity)
                .stacksTo(1);
    }
}