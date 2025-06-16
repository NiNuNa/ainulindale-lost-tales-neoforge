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

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}