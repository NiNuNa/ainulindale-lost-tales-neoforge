package dev.ninuna.losttales.common.block;

import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.block.custom.*;
import dev.ninuna.losttales.common.block.properties.LostTalesBlockProperties;

public class LostTalesBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(LostTales.MOD_ID);

    public static final DeferredBlock<Block> CHEESE_WHEEL = BLOCKS.register(
            "cheese_wheel",
            registryName -> new LostTalesCheeseWheelBlock(LostTalesBlockProperties.cheeseWheelBlockProperties(registryName))
    );

    // Lost Tales Plushies
    public static final DeferredBlock<LostTalesPlushieBlock> PLUSHIE_BEAR = BLOCKS.register(
            "plushie_bear",
            registryName -> new LostTalesPlushieBlock(LostTalesBlockProperties.plushieBlockProperties(registryName))
    );

    public static final DeferredBlock<LostTalesPlushieBlock> PLUSHIE_FOX = BLOCKS.register(
            "plushie_fox",
            registryName -> new LostTalesPlushieBlock(LostTalesBlockProperties.plushieBlockProperties(registryName))
    );

    public static final DeferredBlock<LostTalesPlushieBlock> PLUSHIE_GANDALF = BLOCKS.register(
            "plushie_gandalf",
            registryName -> new LostTalesPlushieBlock(LostTalesBlockProperties.plushieBlockProperties(registryName))
    );

    // Lost Tales Urns
    public static final DeferredBlock<LostTalesUrnBlock> URN_AMPHORA = BLOCKS.register(
            "urn_amphora",
            registryName -> new LostTalesUrnBlock(LostTalesBlockProperties.urnBlockProperties(registryName))
    );

    public static final DeferredBlock<LostTalesUrnBlock> URN = BLOCKS.register(
            "urn",
            registryName -> new LostTalesUrnBlock(LostTalesBlockProperties.urnBlockProperties(registryName), 0.8f)
    );

    public static final DeferredBlock<LostTalesUrnDoubleBlock> URN_LOUTROPHOROS = BLOCKS.register(
            "urn_loutrophoros",
            registryName -> new LostTalesUrnDoubleBlock(LostTalesBlockProperties.urnBlockProperties(registryName), 1.6f)
    );

    public static void register (IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}