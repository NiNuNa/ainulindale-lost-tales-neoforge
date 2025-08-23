package ninuna.losttales.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import ninuna.losttales.LostTales;

import java.util.function.Supplier;

public class LostTalesCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LostTales.MOD_ID);

    // Lost Tales Weapons Creative Mode Tab
    public static final Supplier<CreativeModeTab> WEAPONS_TAB = CREATIVE_MODE_TABS.register("weapons", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + LostTales.MOD_ID + ".weapons"))
            .icon(() -> new ItemStack(LostTalesItems.ARNORIAN_SWORD.get()))
            .displayItems((params, output) -> {
                output.accept(LostTalesItems.ARNORIAN_SWORD);
                output.accept(LostTalesItems.ARNORIAN_DAGGER);
            })
            .build()
    );

    // Lost Tales Equipment Creative Mode Tab
    public static final Supplier<CreativeModeTab> EQUIPMENT_TAB = CREATIVE_MODE_TABS.register("equipment", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + LostTales.MOD_ID + ".equipment"))
            .icon(() -> new ItemStack(LostTalesItems.ARNORIAN_SWORD.get()))
            .displayItems((params, output) -> {
                //Todo...
            })
            .build()
    );

    // Lost Tales Tools Creative Mode Tab
    public static final Supplier<CreativeModeTab> TOOLS_TAB = CREATIVE_MODE_TABS.register("tools", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + LostTales.MOD_ID + ".tools"))
            .icon(() -> new ItemStack(LostTalesItems.ARNORIAN_SWORD.get()))
            .displayItems((params, output) -> {
                output.accept(LostTalesItems.HORN_TEST);
            })
            .build()
    );

    // Lost Tales Plushies Creative Mode Tab
    public static final Supplier<CreativeModeTab> PLUSHIES_TAB = CREATIVE_MODE_TABS.register("plushies", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + LostTales.MOD_ID + ".plushies"))
            .icon(() -> new ItemStack(LostTalesItems.PLUSHIE_BEAR.get()))
            .displayItems((params, output) -> {
                output.accept(LostTalesItems.PLUSHIE_BEAR);
                output.accept(LostTalesItems.PLUSHIE_FOX);
                output.accept(LostTalesItems.PLUSHIE_GANDALF);
            })
            .build()
    );

    // Lost Tales Community Creative Mode Tab
    public static final Supplier<CreativeModeTab> COMMUNITY_TAB = CREATIVE_MODE_TABS.register("community", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + LostTales.MOD_ID + ".community"))
            .icon(() -> new ItemStack(LostTalesItems.ARNORIAN_SWORD.get()))
            .displayItems((params, output) -> {
                //Todo...
            })
            .build()
    );

    // Lost Tales Food & Drinks Creative Mode Tab
    public static final Supplier<CreativeModeTab> FOODANDDRINKS_TAB = CREATIVE_MODE_TABS.register("food_and_drinks", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + LostTales.MOD_ID + ".foodAndDrinks"))
            .icon(() -> new ItemStack(LostTalesItems.PEAR.get()))
            .displayItems((params, output) -> {
                output.accept(LostTalesItems.PEAR);
                output.accept(LostTalesItems.PEAR_BAKED);
                output.accept(LostTalesItems.PEAR_JUICE);
                output.accept(LostTalesItems.PLUM);
                output.accept(LostTalesItems.PLUM_JUICE);
                output.accept(LostTalesItems.CHEESE_WHEEL);
            })
            .build()
    );

    // Lost Tales Decorative Blocks Creative Mode Tab
    public static final Supplier<CreativeModeTab> DECORATIVEBLOCKS_TAB = CREATIVE_MODE_TABS.register("decorative_blocks", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + LostTales.MOD_ID + ".decorativeBlocks"))
            .icon(() -> new ItemStack(LostTalesItems.PEAR.get()))
            .displayItems((params, output) -> {
            })
            .build()
    );

    // Lost Tales Decorative Blocks Creative Mode Tab
    public static final Supplier<CreativeModeTab> FUNCTIONALBLOCKS_TAB = CREATIVE_MODE_TABS.register("functional_blocks", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + LostTales.MOD_ID + ".functionalBlocks"))
            .icon(() -> new ItemStack(LostTalesItems.URN_AMPHORA.get()))
            .displayItems((params, output) -> {
                output.accept(LostTalesItems.URN);
                output.accept(LostTalesItems.URN_AMPHORA);
                output.accept(LostTalesItems.URN_LOUTROPHOROS);
            })
            .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
