package ninuna.losttales.util;

import net.minecraft.world.entity.LivingEntity;

public class LostTalesBlockUtil {

    public static int getSnappedRotationIndex(LivingEntity livingEntity) {
        float yaw = -livingEntity.getYRot() % 360;
        if (yaw < 0) yaw += 360;
        return Math.round(yaw / 22.5f) & 15;
    }

    public static float getRotationFromSnappedRotationIndex(LivingEntity livingEntity) {
        int rotationIndex = getSnappedRotationIndex(livingEntity);
        return rotationIndex * 22.5f;
    }
}