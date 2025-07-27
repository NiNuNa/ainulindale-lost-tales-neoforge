package ninuna.losttales.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import ninuna.losttales.block.custom.LostTalesUrnDoubleBlock;
import ninuna.losttales.block.entity.LostTalesBlockEntities;
import ninuna.losttales.block.entity.LostTalesRespawnableBlockEntity;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LostTalesUrnBlockEntity extends LostTalesRespawnableBlockEntity implements GeoBlockEntity {
    protected static final RawAnimation FILL_ANIM = RawAnimation.begin().thenPlay("fill");
    protected static final RawAnimation FULL_ANIM = RawAnimation.begin().thenPlay("full");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private float rotation;
    private boolean sealed;

    public LostTalesUrnBlockEntity(BlockPos pos, BlockState blockState) {
        super(LostTalesBlockEntities.URN.get(), pos, blockState);
        // Set the inventory size for tall and small urns
        if (blockState.getBlock() instanceof LostTalesUrnDoubleBlock) {
            this.setInventory(NonNullList.withSize(4, ItemStack.EMPTY));
        } else {
            this.setInventory(NonNullList.withSize(2, ItemStack.EMPTY));
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("urnAnimationController", animationTest -> PlayState.STOP)
                .triggerableAnim("fill", FILL_ANIM)
                .triggerableAnim("full", FULL_ANIM)
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.sealed = tag.getBooleanOr("sealed", false);
        this.rotation = tag.getFloatOr("rotation", 0.0f);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("sealed", this.sealed);
        tag.putFloat("rotation", this.rotation);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.setChanged();
        int count = this.getItem(i).getCount();
        this.getInventory().set(i, itemStack.copy().copyWithCount(count + 1));
    }

    public boolean isSameItem(ItemStack itemStack1, ItemStack itemStack2) {
        return itemStack1.getItem() == itemStack2.getItem();
    }

    public void playFillAnimation() {
        if (level instanceof ServerLevel) {
            this.stopTriggeredAnim("urnAnimationController", "fill");
            this.triggerAnim("urnAnimationController", "fill");
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public void playFullAnimation() {
        if (level instanceof ServerLevel) {
            this.stopTriggeredAnim("urnAnimationController", "full");
            this.triggerAnim("urnAnimationController", "full");
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public void setSealed(boolean sealed) {
        this.sealed = sealed;
        this.setChanged();
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        this.setChanged();
    }

    public boolean isSealed() {
        return this.sealed;
    }

    public float getRotation() {
        return this.rotation;
    }
}
