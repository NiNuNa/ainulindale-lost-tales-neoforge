package ninuna.losttales.block.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ninuna.losttales.LostTales;
import ninuna.losttales.block.entity.LostTalesBlockEntities;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class LostTalesUrnBlockEntity extends BlockEntity implements Container, GeoBlockEntity {
    protected static final RawAnimation FILL_ANIM = RawAnimation.begin().thenPlay("fill");
    protected static final RawAnimation FULL_ANIM = RawAnimation.begin().thenPlay("full");

    private final NonNullList<ItemStack> inventory = NonNullList.withSize(4, ItemStack.EMPTY);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final String path;
    private float rotation;
    private boolean sealed;

    public LostTalesUrnBlockEntity(BlockPos pos, BlockState blockState) {
        super(LostTalesBlockEntities.URN.get(), pos, blockState);
        this.path = blockState.getBlock().getName().toString().substring(33, blockState.getBlock().getName().toString().length() - 11);
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
        ContainerHelper.loadAllItems(tag, this.inventory, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("sealed", this.sealed);
        tag.putFloat("rotation", this.rotation);
        ContainerHelper.saveAllItems(tag, this.inventory, registries);
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

    @Override
    public int getContainerSize() {
        return this.inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.inventory) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        this.setChanged();
        return this.inventory.get(i);
    }

    @Override
    public ItemStack removeItem(int i, int i1) {
        this.setChanged();
        ItemStack itemStack = inventory.get(i);
        itemStack.shrink(i1);
        return this.inventory.set(i, itemStack);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        this.setChanged();
        return ContainerHelper.takeItem(this.inventory, i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.setChanged();
        int count = this.getItem(i).getCount();
        this.inventory.set(i, itemStack.copy().copyWithCount(count + 1));
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.inventory.clear();
    }

    public boolean isSameItem(ItemStack itemStack1, ItemStack itemStack2) {
        return itemStack1.getItem() == itemStack2.getItem();
    }

    public void playFillAnimation() {
        if (level instanceof ServerLevel) {
            LostTales.LOGGER.info("Fill!");
            this.stopTriggeredAnim("urnAnimationController", "fill");
            this.triggerAnim("urnAnimationController", "fill");
            this.setChanged();
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    public void playFullAnimation() {
        if (level instanceof ServerLevel) {
            LostTales.LOGGER.info("Full!");
            this.stopTriggeredAnim("urnAnimationController", "full");
            this.triggerAnim("urnAnimationController", "full");
            this.setChanged();
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

    public String getPath() {
        return this.path;
    }

    public boolean isSealed() {
        return this.sealed;
    }

    public float getRotation() {
        return this.rotation;
    }
}