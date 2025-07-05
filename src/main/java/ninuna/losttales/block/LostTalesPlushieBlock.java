package ninuna.losttales.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import ninuna.losttales.LostTales;
import ninuna.losttales.block.entity.LostTalesPlushieBlockEntity;
import ninuna.losttales.sound.LostTalesSoundEvents;
import org.jetbrains.annotations.Nullable;

public class LostTalesPlushieBlock extends BaseEntityBlock {
    public static final MapCodec<LostTalesPlushieBlock> CODEC = simpleCodec(LostTalesPlushieBlock::new);

    public LostTalesPlushieBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
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
            int rotationIndex = this.getSnappedRotationIndex(placer);
            blockEntity.setRotation(rotationIndex * 22.5f);
        }
    }

    // Todo: Make plushies a little bit bouncy

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof LostTalesPlushieBlockEntity blockEntity) {
            //Todo: Remove this logger.
            LostTales.LOGGER.info("This: " + blockEntity.getRotation());
            // Play squeak sound effect
            level.playLocalSound(player, LostTalesSoundEvents.PLUSHIE_SQUEAK.value(), SoundSource.BLOCKS, 0.5f, 1.0f);
            // Play squeak animation
            blockEntity.setSqueaked(true);
        }
        return InteractionResult.PASS;
    }

    private int getSnappedRotationIndex(LivingEntity placer) {
        float yaw = -placer.getYRot() % 360;
        if (yaw < 0) yaw += 360;
        return Math.round(yaw / 22.5f) & 15;
    }
}