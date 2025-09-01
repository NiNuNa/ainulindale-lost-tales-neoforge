package dev.ninuna.losttales.common.block.entity.model;

import net.minecraft.resources.ResourceLocation;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.block.entity.custom.LostTalesPlushieBlockEntity;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class LostTalesPlushieBlockEntityModel extends GeoModel<LostTalesPlushieBlockEntity> {
    public static final DataTicket<String> NAME = DataTicket.create("name", String.class);

    @Override
    public ResourceLocation getModelResource(GeoRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "block/" + renderState.getGeckolibData(NAME));
    }

    @Override
    public ResourceLocation getTextureResource(GeoRenderState renderState) {
        return ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "textures/block/" + renderState.getGeckolibData(NAME) + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(LostTalesPlushieBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "block/" + animatable.getBlockState().getBlock().getName().getString().substring(16));
    }

    @Override
    public void addAdditionalStateData(LostTalesPlushieBlockEntity animatable, GeoRenderState renderState) {
        super.addAdditionalStateData(animatable, renderState);
        renderState.addGeckolibData(NAME, animatable.getBlockState().getBlock().getName().getString().substring(16));
    }
}