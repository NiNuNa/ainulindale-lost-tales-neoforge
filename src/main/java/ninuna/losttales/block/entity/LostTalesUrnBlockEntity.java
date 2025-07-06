package ninuna.losttales.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LostTalesUrnBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final String path;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public LostTalesUrnBlockEntity(BlockPos pos, BlockState blockState) {
        super(LostTalesBlockEntities.URN.get(), pos, blockState);
        this.path = blockState.getBlock().getName().toString().substring(33, blockState.getBlock().getName().toString().length() - 11);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public String getPath() {
        return path;
    }
}
