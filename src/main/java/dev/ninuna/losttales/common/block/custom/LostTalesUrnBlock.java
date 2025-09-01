package dev.ninuna.losttales.common.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.block.LostTalesRespawnableBlock;
import dev.ninuna.losttales.common.block.entity.custom.LostTalesUrnBlockEntity;
import dev.ninuna.losttales.common.item.LostTalesItems;
import dev.ninuna.losttales.common.block.LostTalesBlockHelper;
import org.jetbrains.annotations.Nullable;

public class LostTalesUrnBlock extends LostTalesRespawnableBlock {
    public static final MapCodec<LostTalesUrnBlock> CODEC = simpleCodec(LostTalesUrnBlock::new);
    private static final VoxelShape SHAPE = Block.column(9.0f, 0.0f, 16.0f);

    public final float particleSpawnHeight;

    public LostTalesUrnBlock(Properties properties, float particleSpawnHeight) {
        super(properties);
        this.particleSpawnHeight = particleSpawnHeight;
    }

    public LostTalesUrnBlock(Properties properties) {
        super(properties);
        this.particleSpawnHeight = 1.0f;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new LostTalesUrnBlockEntity(blockPos, blockState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (level.getBlockEntity(pos) instanceof LostTalesUrnBlockEntity urnBlockEntity) {
            Containers.dropContents(level, pos, urnBlockEntity);
            level.updateNeighbourForOutputSignal(pos, this);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (level.getBlockEntity(pos) instanceof LostTalesUrnBlockEntity urnBlockEntity) {
            if (urnBlockEntity.isSealed() && !level.isClientSide && player.preventsBlockDrops() && !urnBlockEntity.isEmpty()) {
                // Todo: Store items like a shulker box
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof LostTalesUrnBlockEntity urnBlockEntity) {
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                if (urnBlockEntity.isSealed() && stack.getItem() != LostTalesItems.CREATION_TOOL_LOOT_RESPAWNER.get()) {
                    return InteractionResult.TRY_WITH_EMPTY_HAND;
                } else if (urnBlockEntity.isSealed() && stack.getItem() == LostTalesItems.CREATION_TOOL_LOOT_RESPAWNER.get()) {
                    return this.setRespawn(level, player, urnBlockEntity, pos);
                } else {
                    if (stack.getItem() == Items.HONEYCOMB) {
                        return this.setSealed(level, urnBlockEntity, stack, player, pos, state);
                    } else {
                        return this.putItems(urnBlockEntity, stack, level, player, pos);
                    }
                }
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof LostTalesUrnBlockEntity urnBlockEntity) {
            urnBlockEntity.playFullAnimation();
            level.playSound(null, pos, SoundEvents.DECORATED_POT_INSERT_FAIL, SoundSource.BLOCKS, 1.0f, 1.0f);
            // Todo: What is this?
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            // Todo: Logger, to be removed
            for (int i = 0; i < urnBlockEntity.getContainerSize(); i++) {
                LostTales.LOGGER.info("Slot " + i + ": " + urnBlockEntity.getItem(i).getHoverName() + " Count: " + urnBlockEntity.getItem(i).getCount());
            }
            LostTales.LOGGER.info("Is sealed: " + urnBlockEntity.isSealed());
            LostTales.LOGGER.info("Does respawn: " + urnBlockEntity.isRespawn());
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof LostTalesUrnBlockEntity urnBlockEntity && placer != null) {
            urnBlockEntity.setRotation(LostTalesBlockHelper.getRotationFromSnappedRotationIndex(placer, 16));
            if (!level.isClientSide()) {
                // Todo: Not sure which one of these is really needed.
                urnBlockEntity.setChanged();
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
            }
        }
    }

    private InteractionResult setSealed(Level level, LostTalesUrnBlockEntity urnBlockEntity, ItemStack itemStack, Player player, BlockPos pos, BlockState blockState) {
        urnBlockEntity.setSealed(true);
        urnBlockEntity.playFillAnimation();
        level.playSound(null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1.0f, 1.0f);
        // Call sendBlockUpdated to ensure that the model changes immediately (Client/Server Syncing)
        level.sendBlockUpdated(pos, blockState, blockState, UPDATE_ALL);
        itemStack.consume(1, player);
        return InteractionResult.SUCCESS;
    }

    private InteractionResult putItems(LostTalesUrnBlockEntity urnBlockEntity, ItemStack itemStack, Level level, Player player, BlockPos pos) {
        for (int i = 0; i < urnBlockEntity.getContainerSize(); i++) {
            if ((urnBlockEntity.getItem(i).isEmpty() || (urnBlockEntity.isSameItem(itemStack, urnBlockEntity.getItem(i)) && urnBlockEntity.getItem(i).getCount() < urnBlockEntity.getItem(i).getMaxStackSize())) && !itemStack.isEmpty()) {
                // The float value changes the pitch of the sound effect that plays when you put an item into the urn
                float f = (float) urnBlockEntity.getItem(i).getCount() / (float) urnBlockEntity.getItem(i).getMaxStackSize();
                urnBlockEntity.playFillAnimation();
                urnBlockEntity.setItem(i, itemStack);
                itemStack.consume(1, player);
                level.playSound(null, pos, SoundEvents.DECORATED_POT_INSERT, SoundSource.BLOCKS, 1.0f, 0.7f + 0.5f * f);
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.DUST_PLUME, (double) pos.getX() + (double) 0.5f, (double) pos.getY() + this.particleSpawnHeight, (double) pos.getZ() + (double) 0.5f, 7, (double) 0.0f, (double) 0.0f, (double) 0.0f, (double) 0.0f);
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.TRY_WITH_EMPTY_HAND;
    }
}
