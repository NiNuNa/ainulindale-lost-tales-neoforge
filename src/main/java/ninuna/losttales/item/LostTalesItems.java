package ninuna.losttales.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import ninuna.losttales.LostTales;
import ninuna.losttales.block.LostTalesBlocks;
import ninuna.losttales.item.properties.LostTalesConsumables;
import ninuna.losttales.item.properties.LostTalesFoods;
import ninuna.losttales.item.properties.LostTalesToolMaterials;

public class LostTalesItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LostTales.MOD_ID);

    // Lost Tales Urns & Pots


    // Lost Tales Food & Drinks
    public static final DeferredItem<Item> PLUM = ITEMS.registerItem(
            "plum",
            LostTalesTooltipItem::new,
            new Item.Properties()
                    .food(LostTalesFoods.PLUM)
    );

    public static final DeferredItem<Item> PLUM_JUICE = ITEMS.registerItem(
            "plum_juice",
            LostTalesTooltipItem::new,
            new Item.Properties()
                    .craftRemainder(Items.GLASS_BOTTLE)
                    .food(LostTalesFoods.PEAR_JUICE, LostTalesConsumables.PEAR_JUICE)
                    .usingConvertsTo(Items.GLASS_BOTTLE)
                    .stacksTo(16)
    );

    public static final DeferredItem<Item> PEAR = ITEMS.registerItem(
            "pear",
            LostTalesTooltipItem::new,
            new Item.Properties()
                    .food(LostTalesFoods.PEAR)
    );

    public static final DeferredItem<Item> PEAR_BAKED = ITEMS.registerItem(
            "pear_baked",
            LostTalesTooltipItem::new,
            new Item.Properties()
                    .food(LostTalesFoods.PEAR_BAKED)
    );

    public static final DeferredItem<Item> PEAR_JUICE = ITEMS.registerItem(
            "pear_juice",
            LostTalesTooltipItem::new,
            new Item.Properties()
                    .craftRemainder(Items.GLASS_BOTTLE)
                    .food(LostTalesFoods.PEAR_JUICE, LostTalesConsumables.PEAR_JUICE)
                    .usingConvertsTo(Items.GLASS_BOTTLE)
                    .stacksTo(16)
    );

    // Lost Tales Food & Drinks BlockItems
    public static final DeferredItem<BlockItem> CHEESE_WHEEL = ITEMS.registerSimpleBlockItem(
            "cheese_wheel",
            LostTalesBlocks.CHEESE_WHEEL,
            new Item.Properties()
                    .food(LostTalesFoods.CHEESE_WHEEL)
                    .stacksTo(1)
    );

    // Lost Tales Plushie BlockItems
    public static final DeferredItem<BlockItem> PLUSHIE_BEAR = ITEMS.registerSimpleBlockItem(
            "plushie_bear",
            LostTalesBlocks.PLUSHIE_BEAR,
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
    );

    public static final DeferredItem<BlockItem> PLUSHIE_FOX = ITEMS.registerSimpleBlockItem(
            "plushie_fox",
            LostTalesBlocks.PLUSHIE_FOX,
            new Item.Properties()
                    .stacksTo(1)
                    .rarity(Rarity.UNCOMMON)
    );

    // Lost Tales Weapons & Tools
    public static final DeferredItem<Item> ARNORIAN_SWORD = ITEMS.registerItem(
            "arnorian_sword",
            props -> new Item(props.sword(
                    // The material to use.
                    LostTalesToolMaterials.ARNORIAN_MATERIAL,
                    // The type-specific attack damage bonus. 3 for swords, 1.5 for shovels, 1 for pickaxes, varying for axes and hoes.
                    3,
                    // The type-specific attack speed modifier. The player has a default attack speed of 4, so to get to the desired
                    // value of 1.6f, we use -2.4f. -2.4f for swords, -3f for shovels, -2.8f for pickaxes, varying for axes and hoes.
                    -2.4f
            )));

    public static final DeferredItem<Item> ARNORIAN_DAGGER = ITEMS.registerItem(
            "arnorian_dagger",
            props -> new Item(props.sword(
                    // The material to use.
                    LostTalesToolMaterials.ARNORIAN_MATERIAL,
                    // The type-specific attack damage bonus. 3 for swords, 1.5 for shovels, 1 for pickaxes, varying for axes and hoes.
                    3,
                    // The type-specific attack speed modifier. The player has a default attack speed of 4, so to get to the desired
                    // value of 1.6f, we use -2.4f. -2.4f for swords, -3f for shovels, -2.8f for pickaxes, varying for axes and hoes.
                    -2.4f
            )));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}