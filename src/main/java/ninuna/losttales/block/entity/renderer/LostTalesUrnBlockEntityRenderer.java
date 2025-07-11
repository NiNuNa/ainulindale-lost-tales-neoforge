package ninuna.losttales.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;
import ninuna.losttales.block.entity.custom.LostTalesUrnBlockEntity;
import ninuna.losttales.block.entity.model.LostTalesUrnBlockEntityModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class LostTalesUrnBlockEntityRenderer extends GeoBlockRenderer<LostTalesUrnBlockEntity> {

    public LostTalesUrnBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new LostTalesUrnBlockEntityModel());
    }

    @Override
    public void render(LostTalesUrnBlockEntity animatable, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, Vec3 cameraPosition) {
        float rotation = animatable.getRotation();

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.translate(-0.5, 0.0, -0.5);

        super.render(animatable, partialTick, poseStack, bufferSource, packedLight, packedOverlay, cameraPosition);
        poseStack.popPose();
    }
}