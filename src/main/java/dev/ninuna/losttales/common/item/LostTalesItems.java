package dev.ninuna.losttales.common.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.block.LostTalesBlocks;
import dev.ninuna.losttales.common.item.custom.instrument.LostTalesHornInstrumentItem;
import dev.ninuna.losttales.common.item.custom.LostTalesPlushieItem;
import dev.ninuna.losttales.common.item.custom.LostTalesTooltipBlockItem;
import dev.ninuna.losttales.common.item.custom.LostTalesTooltipItem;
import dev.ninuna.losttales.common.item.properties.LostTalesConsumables;
import dev.ninuna.losttales.common.item.properties.LostTalesFoods;
import dev.ninuna.losttales.common.item.properties.LostTalesItemProperties;
import dev.ninuna.losttales.common.item.properties.LostTalesToolMaterials;

public class LostTalesItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LostTales.MOD_ID);

    // Lost Tales Creation Tools
    public static final DeferredItem<Item> CREATION_TOOL_LOOT_RESPAWNER = ITEMS.register(
            "creation_tool_loot_respawner",
            registryName -> new LostTalesTooltipItem(LostTalesItemProperties.toolItemProperties(registryName))
    );

    // Lost Tales Instruments
    public static final DeferredItem<Item> HORN_TEST = ITEMS.register(
            "horn_test",
            registryName -> new LostTalesHornInstrumentItem(LostTalesItemProperties.instrumentItemProperties(registryName, Rarity.UNCOMMON))
    );

    // Lost Tales Food & Drinks
    public static final DeferredItem<Item> PLUM = ITEMS.register(
            "plum",
            registryName -> new LostTalesTooltipItem(LostTalesItemProperties.foodItemProperties(registryName, LostTalesFoods.PLUM))
    );

    public static final DeferredItem<Item> PLUM_JUICE = ITEMS.register(
            "plum_juice",
            registryName -> new LostTalesTooltipItem(LostTalesItemProperties.drinkItemProperties(registryName, LostTalesFoods.PEAR_JUICE, LostTalesConsumables.BASE_JUICE))
    );

    public static final DeferredItem<Item> PEAR = ITEMS.register(
            "pear",
            registryName -> new LostTalesTooltipItem(LostTalesItemProperties.foodItemProperties(registryName, LostTalesFoods.PEAR))
    );

    public static final DeferredItem<Item> PEAR_BAKED = ITEMS.register(
            "pear_baked",
            registryName -> new LostTalesTooltipItem(LostTalesItemProperties.foodItemProperties(registryName, LostTalesFoods.PEAR_BAKED))
    );

    public static final DeferredItem<Item> PEAR_JUICE = ITEMS.register(
            "pear_juice",
            registryName -> new LostTalesTooltipItem(LostTalesItemProperties.drinkItemProperties(registryName, LostTalesFoods.PEAR_JUICE, LostTalesConsumables.BASE_JUICE))
    );

    public static final DeferredItem<BlockItem> CHEESE_WHEEL = ITEMS.register(
            "cheese_wheel",
            registryName -> new LostTalesTooltipBlockItem(LostTalesBlocks.CHEESE_WHEEL.get(), LostTalesItemProperties.foodItemProperties(registryName, LostTalesFoods.CHEESE_WHEEL))
    );

    // Lost Tales Plushies
    public static final DeferredItem<BlockItem> PLUSHIE_BEAR = ITEMS.register(
            "plushie_bear",
            registryName -> new LostTalesPlushieItem(LostTalesBlocks.PLUSHIE_BEAR.get(), LostTalesItemProperties.plushieItemProperties(registryName, Rarity.UNCOMMON))
    );

    public static final DeferredItem<BlockItem> PLUSHIE_FOX = ITEMS.register(
            "plushie_fox",
            registryName -> new LostTalesPlushieItem(LostTalesBlocks.PLUSHIE_FOX.get(), LostTalesItemProperties.plushieItemProperties(registryName, Rarity.UNCOMMON))
    );

    public static final DeferredItem<BlockItem> PLUSHIE_GANDALF = ITEMS.register(
            "plushie_gandalf",
            registryName -> new LostTalesPlushieItem(LostTalesBlocks.PLUSHIE_GANDALF.get(), LostTalesItemProperties.plushieItemProperties(registryName, Rarity.RARE))
    );

    // Lost Tales Urns
    public static final DeferredItem<BlockItem> URN_AMPHORA = ITEMS.register(
            "urn_amphora",
            registryName -> new LostTalesTooltipBlockItem(LostTalesBlocks.URN_AMPHORA.get(), LostTalesItemProperties.urnItemProperties(registryName))
    );

    public static final DeferredItem<BlockItem> URN = ITEMS.register(
            "urn",
            registryName -> new LostTalesTooltipBlockItem(LostTalesBlocks.URN.get(), LostTalesItemProperties.urnItemProperties(registryName))
    );

    public static final DeferredItem<BlockItem> URN_LOUTROPHOROS = ITEMS.register(
            "urn_loutrophoros",
            registryName -> new LostTalesTooltipBlockItem(LostTalesBlocks.URN_LOUTROPHOROS.get(), LostTalesItemProperties.urnItemProperties(registryName))
    );

    // Todo: Lost Tales Weapons & Tools
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
            ))
    );

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
            ))
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}