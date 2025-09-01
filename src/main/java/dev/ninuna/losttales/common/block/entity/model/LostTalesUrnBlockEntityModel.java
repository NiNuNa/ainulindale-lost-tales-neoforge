package dev.ninuna.losttales.common.block.entity.model;

import net.minecraft.resources.ResourceLocation;
import dev.ninuna.losttales.common.LostTales;
import dev.ninuna.losttales.common.block.custom.LostTalesUrnDoubleBlock;
import dev.ninuna.losttales.common.block.entity.custom.LostTalesUrnBlockEntity;
import software.bernie.geckolib.constant.dataticket.DataTicket;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class LostTalesUrnBlockEntityModel extends GeoModel<LostTalesUrnBlockEntity> {
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
    public ResourceLocation getAnimationResource(LostTalesUrnBlockEntity animatable) {
        if (animatable.getBlockState().getBlock() instanceof LostTalesUrnDoubleBlock) {
            return ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "block/urn_tall");
        } else {
            return ResourceLocation.fromNamespaceAndPath(LostTales.MOD_ID, "block/urn");
        }
    }

    @Override
    public void addAdditionalStateData(LostTalesUrnBlockEntity animatable, GeoRenderState renderState) {
        super.addAdditionalStateData(animatable, renderState);
        // Replace model, texture and animations once the urn is sealed
        if (animatable.isSealed()) {
            renderState.addGeckolibData(NAME, animatable.getBlockState().getBlock().getName().getString().substring(16) + "_sealed");
        } else {
            renderState.addGeckolibData(NAME, animatable.getBlockState().getBlock().getName().getString().substring(16));
        }
    }
}