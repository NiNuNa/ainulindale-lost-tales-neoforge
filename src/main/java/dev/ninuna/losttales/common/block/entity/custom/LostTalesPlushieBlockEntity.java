package dev.ninuna.losttales.common.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.block.entity.LostTalesBlockEntities;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LostTalesPlushieBlockEntity extends BlockEntity implements GeoBlockEntity {
    protected static final RawAnimation SQUEAK_ANIM = RawAnimation.begin().thenPlay("squeak");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private float rotation;

    public LostTalesPlushieBlockEntity(BlockPos pos, BlockState blockState) {
        super(LostTalesBlockEntities.PLUSHIE.get(), pos, blockState);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("plushieAnimationController", animationTest -> PlayState.STOP).triggerableAnim("squeak", SQUEAK_ANIM));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.rotation = tag.getFloatOr("rotation", 0.0f);
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

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public void playSqueakAnimation() {
        if (level instanceof ServerLevel) {
            LostTales.LOGGER.info("Squeaked!");
            this.stopTriggeredAnim("plushieAnimationController", "squeak");
            this.triggerAnim("plushieAnimationController", "squeak");
            this.setChanged();
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
        this.setChanged();
    }

    public float getRotation() {
        return this.rotation;
    }
}