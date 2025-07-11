package ninuna.losttales.block.entity.model;

import net.minecraft.resources.ResourceLocation;
import ninuna.losttales.LostTales;
import ninuna.losttales.block.entity.custom.LostTalesPlushieBlockEntity;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class LostTalesPlushieBlockEntityModel extends GeoModel<LostTalesPlushieBlockEntity> {
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
    public ResourceLocation getAnimationResource(LostTalesPlushieBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "block/" + animatable.getPath());
    }

    @Override
    public void addAdditionalStateData(LostTalesPlushieBlockEntity animatable, GeoRenderState renderState) {
        super.addAdditionalStateData(animatable, renderState);
        renderState.addGeckolibData(PATH, animatable.getPath());
    }
}