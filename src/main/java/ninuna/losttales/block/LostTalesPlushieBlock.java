package ninuna.losttales.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import ninuna.losttales.block.entity.LostTalesPlushieBlockEntity;
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
        return RenderShape.MODEL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        level.playLocalSound(player, SoundEvents.ITEM_BREAK.value(), SoundSource.BLOCKS, 1f, 2f);
        return InteractionResult.PASS;
    }
}