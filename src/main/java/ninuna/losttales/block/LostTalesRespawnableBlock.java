package ninuna.losttales.block;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import ninuna.losttales.block.entity.LostTalesRespawnableBlockEntity;

public abstract class LostTalesRespawnableBlock extends BaseEntityBlock {
    public LostTalesRespawnableBlock(Properties properties) {
        super(properties);
    }

    protected InteractionResult setRespawn(Level level, Player player, LostTalesRespawnableBlockEntity respawnableBlockEntity, BlockPos pos) {
        if (player.isCreative()) {
            if (respawnableBlockEntity.isRespawn()) {
                respawnableBlockEntity.setRespawn(false);
                level.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, 1.0f, 1.0f);
            } else {
                respawnableBlockEntity.setRespawn(true);
                level.playSound(null, pos, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }
    }
}
