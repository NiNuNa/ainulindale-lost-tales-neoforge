package ninuna.losttales.block.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import ninuna.losttales.LostTales;
import ninuna.losttales.block.LostTalesBlocks;

import java.util.function.Supplier;

public class LostTalesBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, LostTales.MOD_ID);

    public static final Supplier<BlockEntityType<LostTalesPlushieBlockEntity>> PLUSHIE = BLOCK_ENTITY_TYPES.register(
            "plushie", () -> new BlockEntityType<>(
                    LostTalesPlushieBlockEntity::new,
                    false,
                    LostTalesBlocks.PLUSHIE_BEAR.get(), LostTalesBlocks.PLUSHIE_FOX.get()
            )
    );

    public static void register (IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}