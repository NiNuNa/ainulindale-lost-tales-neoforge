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

public class LostTalesBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(LostTales.MOD_ID);

    public static final DeferredBlock<Block> TEST_BLOCK = BLOCKS.register("test_block",
            registryName -> new Block(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registryName))
                    .destroyTime(2.0f)
                    .explosionResistance(10.0f)
                    .sound(SoundType.GRAVEL)
                    .lightLevel(state -> 7)
            ));

    public static final DeferredBlock<Block> CHEESE_WHEEL = BLOCKS.register("cheese_wheel",
            registryName -> new LostTalesCheeseWheelBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registryName))
                    .destroyTime(2.0f)
                    .explosionResistance(10.0f)
                    .sound(SoundType.WOOL)
            ));

    // Lost Tales Plushies.
    public static final DeferredBlock<LostTalesPlushieBlock> PLUSHIE_BEAR = BLOCKS.register("plushie_bear",
            registryName -> new LostTalesPlushieBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registryName))
                    .sound(SoundType.WOOL)
                    .noOcclusion()
            ));

    public static final DeferredBlock<LostTalesPlushieBlock> PLUSHIE_FOX = BLOCKS.register("plushie_fox",
            registryName -> new LostTalesPlushieBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registryName))
                    .sound(SoundType.WOOL)
                    .noOcclusion()
            ));

    public static void register (IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}