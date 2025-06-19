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

    // Lost Tales Weapons Creative Mode Tab.
    public static final Supplier<CreativeModeTab> WEAPONS_TAB = CREATIVE_MODE_TABS.register("weapons", () -> CreativeModeTab.builder()
            //Set the title of the tab. Don't forget to add a translation!
            .title(Component.translatable("itemGroup." + LostTales.MOD_ID + ".weapons"))
            //Set the icon of the tab.
            .icon(() -> new ItemStack(LostTalesItems.ARNORIAN_SWORD.get()))
            //Add your items to the tab.
            .displayItems((params, output) -> {
                output.accept(LostTalesItems.ARNORIAN_SWORD);
                output.accept(LostTalesItems.ARNORIAN_DAGGER);
            })
            .build()
    );

    // Lost Tales Equipment Creative Mode Tab.
    public static final Supplier<CreativeModeTab> EQUIPMENT_TAB = CREATIVE_MODE_TABS.register("equipment", () -> CreativeModeTab.builder()
            //Set the title of the tab. Don't forget to add a translation!
            .title(Component.translatable("itemGroup." + LostTales.MOD_ID + ".equipment"))
            //Set the icon of the tab.
            .icon(() -> new ItemStack(LostTalesItems.ARNORIAN_SWORD.get()))
            //Add your items to the tab.
            .displayItems((params, output) -> {
                output.accept(LostTalesItems.ARNORIAN_SWORD);
            })
            .build()
    );

    // Lost Tales Tools Creative Mode Tab.
    public static final Supplier<CreativeModeTab> TOOLS_TAB = CREATIVE_MODE_TABS.register("tools", () -> CreativeModeTab.builder()
            //Set the title of the tab. Don't forget to add a translation!
            .title(Component.translatable("itemGroup." + LostTales.MOD_ID + ".tools"))
            //Set the icon of the tab.
            .icon(() -> new ItemStack(LostTalesItems.ARNORIAN_SWORD.get()))
            //Add your items to the tab.
            .displayItems((params, output) -> {
                output.accept(LostTalesItems.ARNORIAN_SWORD);
            })
            .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}