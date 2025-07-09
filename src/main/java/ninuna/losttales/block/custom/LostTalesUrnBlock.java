package ninuna.losttales.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import ninuna.losttales.block.entity.LostTalesUrnBlockEntity;
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
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if (level.getBlockEntity(pos) instanceof LostTalesUrnBlockEntity blockEntity && placer != null) {
            //Set block entity rotation
            blockEntity.setRotation(LostTalesBlockUtil.getRotationFromSnappedRotationIndex(placer));
            if (!level.isClientSide()) {
                blockEntity.setChanged();
                level.sendBlockUpdated(pos, state, state, Block.UPDATE_CLIENTS);
            }
        }
    }
}