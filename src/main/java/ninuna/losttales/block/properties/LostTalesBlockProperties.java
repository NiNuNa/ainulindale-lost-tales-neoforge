package ninuna.losttales.block.properties;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;

public class LostTalesBlockProperties {

    public static BlockBehaviour.Properties plushieBlockProperties(ResourceLocation registryName) {
        return BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, registryName))
                .sound(SoundType.WOOL)
                .noOcclusion()
                .strength(0.3f, 0.0f)
                .pushReaction(PushReaction.DESTROY);
    }

    public static BlockBehaviour.Properties urnBlockProperties(ResourceLocation registryName) {
        return BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, registryName))
                .sound(SoundType.DECORATED_POT)
                .noOcclusion()
                .strength(0.1f, 0.0f)
                .pushReaction(PushReaction.DESTROY);
    }

    public static BlockBehaviour.Properties cheeseWheelBlockProperties(ResourceLocation registryName) {
        return BlockBehaviour.Properties.of()
                .setId(ResourceKey.create(Registries.BLOCK, registryName))
                .sound(SoundType.WOOL)
                .strength(0.6f, 0.0f)
                .pushReaction(PushReaction.NORMAL);
    }
}