package ninuna.losttales.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class LostTalesRespawnableBlockEntity extends BlockEntity implements Container {
    private boolean respawn;
    private NonNullList<ItemStack> inventory;

    public LostTalesRespawnableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.respawn = tag.getBooleanOr("respawn", false);
        ContainerHelper.loadAllItems(tag, this.getInventory(), registries);

    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("respawn", this.respawn);
        ContainerHelper.saveAllItems(tag, this.getInventory(), registries);
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
        return this.getInventory().size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemStack : this.getInventory()) {
            if (!itemStack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        this.setChanged();
        return this.getInventory().get(i);
    }

    @Override
    public ItemStack removeItem(int i, int i1) {
        ItemStack itemStack = getInventory().get(i);
        itemStack.shrink(i1);
        this.setChanged();
        return this.getInventory().set(i, itemStack);
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        this.setChanged();
        return ContainerHelper.takeItem(this.getInventory(), i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        this.getInventory().set(i, itemStack.copy());
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.getInventory().clear();
    }

    public void setRespawn(boolean respawn) {
        this.respawn = respawn;
        this.setChanged();
    }

    public boolean isRespawn() {
        return respawn;
    }

    public void setInventory(NonNullList<ItemStack> inventory) {
        this.inventory = inventory;
        this.setChanged();
    }

    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }
}
