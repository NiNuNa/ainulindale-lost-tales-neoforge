package ninuna.losttales.block;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import ninuna.losttales.LostTales;
import ninuna.losttales.block.custom.LostTalesCheeseWheelBlock;
import ninuna.losttales.block.custom.LostTalesPlushieBlock;
import ninuna.losttales.block.custom.LostTalesUrnBlock;
import ninuna.losttales.block.properties.LostTalesBlockProperties;

public class LostTalesBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(LostTales.MOD_ID);

    public static final DeferredBlock<Block> CHEESE_WHEEL = BLOCKS.register(
            "cheese_wheel",
            registryName ->
                    new LostTalesCheeseWheelBlock(BlockBehaviour.Properties.of()
                            .setId(ResourceKey.create(Registries.BLOCK, registryName))
                            .destroyTime(2.0f)
                            .sound(SoundType.WOOL)
            ));

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

    public static void register (IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}