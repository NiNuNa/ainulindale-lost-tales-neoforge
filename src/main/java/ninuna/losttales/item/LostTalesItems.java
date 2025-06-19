package ninuna.losttales.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import ninuna.losttales.LostTales;
import ninuna.losttales.block.LostTalesBlocks;

public class LostTalesItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(LostTales.MOD_ID);

    // Lost Tales Items.
    public static final DeferredItem<Item> TEST_ITEM = ITEMS.registerItem(
            "example_item",
            Item::new,
            new Item.Properties()
    );

    // Lost Tales BlockItems.
    public static final DeferredItem<BlockItem> EXAMPLE_BLOCK_ITEM = ITEMS.registerSimpleBlockItem(
            "example_block",
            LostTalesBlocks.TEST_BLOCK,
            new Item.Properties()
    );

    // Lost Tales Weapons and Tools.
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