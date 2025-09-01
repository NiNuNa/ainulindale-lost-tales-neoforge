package dev.ninuna.losttales.common.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import dev.ninuna.losttales.common.block.entity.custom.LostTalesPlushieBlockEntity;
import dev.ninuna.losttales.common.sound.LostTalesSoundEvents;
import dev.ninuna.losttales.common.block.LostTalesBlockHelper;
import org.jetbrains.annotations.Nullable;

public class LostTalesPlushieBlock extends BaseEntityBlock {
    private static final VoxelShape SHAPE = Block.column(8.5, 0.0F, 12.0F);

    public static final MapCodec<LostTalesPlushieBlock> CODEC = simpleCodec(LostTalesPlushieBlock::new);

    public LostTalesPlushieBlock(Properties properties) {
        super(properties);
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
        return new LostTalesPlushieBlockEntity(blockPos, blockState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof LostTalesPlushieBlockEntity blockEntity && placer != null) {
            //Set block entity rotation
            blockEntity.setRotation(LostTalesBlockHelper.getRotationFromSnappedRotationIndex(placer, 16));
            if (!level.isClientSide()) {
                blockEntity.setChanged();
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
                // Play squeak animation
                blockEntity.playSqueakAnimation();
            }
        }
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, double fallDistance) {
        if (!level.isClientSide() && fallDistance > 0.4 && !entity.isSteppingCarefully() && level.getBlockEntity(pos) instanceof LostTalesPlushieBlockEntity blockEntity) {
            this.squeak(blockEntity, level, pos);
            super.fallOn(level, state, pos, entity, fallDistance * (double) 0.8F);
        } else {
            super.fallOn(level, state, pos, entity, fallDistance);
        }
    }

    @Override
    public void updateEntityMovementAfterFallOn(BlockGetter level, Entity entity) {
        if (entity.isSuppressingBounce()) {
            super.updateEntityMovementAfterFallOn(level, entity);
        } else {
            this.bounceUp(entity);
        }
    }

    private void bounceUp(Entity entity) {
        Vec3 vec3 = entity.getDeltaMovement();
        if (vec3.y <(double) 0.0F) {
            double d = entity instanceof LivingEntity ? (double) 1.0F : 0.8F;
            entity.setDeltaMovement(vec3.x, -vec3.y * (double) 0.6F * d, vec3.z);
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide() && level.getBlockEntity(pos) instanceof LostTalesPlushieBlockEntity blockEntity) {
            this.squeak(blockEntity, level, pos);
        }
        return InteractionResult.SUCCESS;
    }

    private void squeak(LostTalesPlushieBlockEntity blockEntity, Level level, BlockPos pos) {
        if (!level.isClientSide()) {
            // Play squeak animation
            blockEntity.playSqueakAnimation();
            // Play squeak sound effect
            level.playSound(null, pos, LostTalesSoundEvents.PLUSHIE_SQUEAK.value(), SoundSource.BLOCKS, 0.2f, 1.0f);
        }
    }
}