package ninuna.losttales.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
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
import ninuna.losttales.LostTales;
import ninuna.losttales.block.entity.custom.LostTalesUrnBlockEntity;
import ninuna.losttales.util.LostTalesBlockUtil;
import org.jetbrains.annotations.Nullable;

public class LostTalesUrnBlock extends BaseEntityBlock {
    public static final MapCodec<LostTalesUrnBlock> CODEC = simpleCodec(LostTalesUrnBlock::new);
    private static final VoxelShape SHAPE = Block.column(9.0f, 0.0f, 16.0f);

    public LostTalesUrnBlock(Properties properties) {
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
        return new LostTalesUrnBlockEntity(blockPos, blockState);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (level.getBlockEntity(pos) instanceof LostTalesUrnBlockEntity blockEntity) {
            Containers.dropContents(level, pos, blockEntity);
            level.updateNeighbourForOutputSignal(pos, this);
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof LostTalesUrnBlockEntity blockEntity) {
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                if (blockEntity.isSealed()) {
                    return InteractionResult.TRY_WITH_EMPTY_HAND;
                } else {
                    // Sealing the urn code
                    if (stack.getItem() == Items.OMINOUS_TRIAL_KEY) {
                        blockEntity.setSealed(true);
                        level.playSound(null, pos, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1.0f, 1.0f);
                        level.sendBlockUpdated(pos, state, state, UPDATE_ALL);
                        return InteractionResult.SUCCESS;
                    } else {
                        // Run this for each inventory slot of the block entity
                        for (int i = 0; i < blockEntity.getContainerSize(); i++) {
                            if ((blockEntity.getItem(i).isEmpty() || (blockEntity.isSameItem(stack, blockEntity.getItem(i)) && blockEntity.getItem(i).getCount() < blockEntity.getItem(i).getMaxStackSize())) && !stack.isEmpty()) {
                                float f = (float) blockEntity.getItem(i).getCount() / (float) blockEntity.getItem(i).getMaxStackSize();
                                // Play the fill/success animation
                                blockEntity.playFillAnimation();
                                // Add the item from the player's hand to the block entity's inventory, then remove one of the same item from the player's inventory
                                blockEntity.setItem(i, stack);
                                stack.shrink(1);
                                // Play the success sound effect
                                level.playSound(null, pos, SoundEvents.DECORATED_POT_INSERT, SoundSource.BLOCKS, 1.0f, 0.7f + 0.5f * f);
                                return InteractionResult.SUCCESS;
                            }
                        }
                        return InteractionResult.TRY_WITH_EMPTY_HAND;
                    }
                }
            }
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof LostTalesUrnBlockEntity blockEntity) {
            // Play the full/failure animation
            blockEntity.playFullAnimation();
            // Play the failure sound effect
            level.playSound(null, pos, SoundEvents.DECORATED_POT_INSERT_FAIL, SoundSource.BLOCKS, 1.0f, 1.0f);
            // Todo: What is this?
            level.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
            // Todo: Logger, to be removed
            for (int i = 0; i < blockEntity.getContainerSize(); i++) {
                LostTales.LOGGER.info("Slot " + i + ": " + blockEntity.getItem(i).getHoverName() + " Count: " + blockEntity.getItem(i).getCount());
            }
            LostTales.LOGGER.info("Is sealed: " + blockEntity.isSealed());
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof LostTalesUrnBlockEntity blockEntity && placer != null) {
            // Set block entity rotation
            blockEntity.setRotation(LostTalesBlockUtil.getRotationFromSnappedRotationIndex(placer));
            if (!level.isClientSide()) {
                // Todo: Not sure which one of these is really needed.
                blockEntity.setChanged();
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
            }
        }
    }
}