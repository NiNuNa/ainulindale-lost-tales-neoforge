package ninuna.losttales.block.entity.model;

import net.minecraft.resources.ResourceLocation;
import ninuna.losttales.LostTales;
import ninuna.losttales.block.entity.custom.LostTalesUrnBlockEntity;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class LostTalesUrnBlockEntityModel extends GeoModel<LostTalesUrnBlockEntity> {
    public static final DataTicket<String> PATH = DataTicket.create("path", String.class);

    @Override
    public ResourceLocation getModelResource(GeoRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "block/" + renderState.getGeckolibData(PATH));
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "textures/block/" + renderState.getGeckolibData(PATH) + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(LostTalesUrnBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "block/" + animatable.getPath());
    }

    @Override
    public void addAdditionalStateData(LostTalesUrnBlockEntity animatable, GeoRenderState renderState) {
        super.addAdditionalStateData(animatable, renderState);
        // Replace model, texture and animations once the urn is sealed
        if (animatable.isSealed()) {
            renderState.addGeckolibData(PATH, animatable.getPath() + "_sealed");
        } else {
            renderState.addGeckolibData(PATH, animatable.getPath());
        }
    }
}