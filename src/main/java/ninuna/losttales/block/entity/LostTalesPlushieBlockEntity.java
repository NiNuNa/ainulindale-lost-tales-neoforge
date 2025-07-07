package ninuna.losttales.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ninuna.losttales.LostTales;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LostTalesPlushieBlockEntity extends BlockEntity implements GeoBlockEntity {
    private float rotation = 0.0f;
    private final String path;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected static final RawAnimation SQUEAK_ANIM = RawAnimation.begin().thenPlay("squeak");

    public LostTalesPlushieBlockEntity(BlockPos pos, BlockState blockState) {
        super(LostTalesBlockEntities.PLUSHIE.get(), pos, blockState);
        this.path = blockState.getBlock().getName().toString().substring(33, blockState.getBlock().getName().toString().length() - 11);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("squeakAnimationController", animationTest -> PlayState.STOP).triggerableAnim("squeak", SQUEAK_ANIM));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public void playSqueakAnimation() {
        if (level instanceof ServerLevel) {
            LostTales.LOGGER.info("Squeaked!");
            this.stopTriggeredAnim("squeakAnimationController", "squeak");
            this.triggerAnim("squeakAnimationController", "squeak");
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.rotation = tag.getFloat("rotation").orElse(0.0f);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putFloat("rotation", this.rotation);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag, registries);
        return tag;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        this.setChanged();
    }

    public float getRotation() {
        return this.rotation;
    }

    public String getPath() {
        return path;
    }
}