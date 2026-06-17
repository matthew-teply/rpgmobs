package com.conanthecivilian.rpgmobs.entity.client;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.custom.AbstractHumanlikeEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class HumanlikeRenderer extends MobRenderer<AbstractHumanlikeEntity, HumanlikeModel> {
    public HumanlikeRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanlikeModel(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);

        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull AbstractHumanlikeEntity abstractHumanlikeEntity) {
        return ResourceLocation.fromNamespaceAndPath(RPGMobs.MODID, abstractHumanlikeEntity.getTextureLocation().getPath());
    }
}
