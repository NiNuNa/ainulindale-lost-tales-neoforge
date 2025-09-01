package dev.ninuna.losttales.common.block.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.block.LostTalesBlocks;
import dev.ninuna.losttales.common.block.entity.custom.LostTalesPlushieBlockEntity;
import dev.ninuna.losttales.common.block.entity.custom.LostTalesUrnBlockEntity;

import java.util.function.Supplier;

public class LostTalesBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, LostTales.MOD_ID);

    public static final Supplier<BlockEntityType<LostTalesPlushieBlockEntity>> PLUSHIE = BLOCK_ENTITY_TYPES.register(
            "plushie", () -> new BlockEntityType<>(LostTalesPlushieBlockEntity::new, false,
                    LostTalesBlocks.PLUSHIE_BEAR.get(), LostTalesBlocks.PLUSHIE_FOX.get(), LostTalesBlocks.PLUSHIE_GANDALF.get()
            )
    );

    public static final Supplier<BlockEntityType<LostTalesUrnBlockEntity>> URN = BLOCK_ENTITY_TYPES.register(
            "urn", () -> new BlockEntityType<>(LostTalesUrnBlockEntity::new, false,
                    LostTalesBlocks.URN_AMPHORA.get(), LostTalesBlocks.URN.get(), LostTalesBlocks.URN_LOUTROPHOROS.get()
            )
    );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITY_TYPES.register(eventBus);
    }
}
