package ninuna.losttales.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class LostTalesPlushieBlockEntity extends BlockEntity {
    public float rotationAngle = 0f;

    public LostTalesPlushieBlockEntity(BlockPos pos, BlockState blockState) {
        super(LostTalesBlockEntities.PLUSHIE.get(), pos, blockState);
    }

}