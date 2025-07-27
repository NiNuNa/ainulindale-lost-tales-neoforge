package ninuna.losttales.util;

import net.minecraft.world.entity.LivingEntity;

public class LostTalesBlockUtil {
    public static int getSnappedRotationIndex(LivingEntity livingEntity, int rotationIndexSteps) {
        float yaw = -livingEntity.getYRot() % 360;
        if (yaw < 0) yaw += 360;
        return Math.round(yaw / ((float) 360 / rotationIndexSteps)) & (rotationIndexSteps - 1);
    }

    public static float getRotationFromSnappedRotationIndex(LivingEntity livingEntity, int rotationIndexSteps) {
        int rotationIndex = getSnappedRotationIndex(livingEntity, rotationIndexSteps);
        return rotationIndex * ((float) 360 / rotationIndexSteps);
    }
}