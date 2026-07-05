package com.conanthecivilian.rpgmobs.entity.npc.client.renderer;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.npc.client.model.HumanlikeModel;
import com.conanthecivilian.rpgmobs.entity.npc.custom.AbstractNPCEntity;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class HumanlikeRenderer<T extends AbstractNPCEntity<T>> extends MobRenderer<T, HumanlikeModel<T>> {
    public static final float WIDTH = 0.6F;
    public static final float HEIGHT = 1.8F;

    public static final float EYE_HEIGHT_OFFSET = 0.18F;
    public static final float EYE_HEIGHT = HEIGHT - EYE_HEIGHT_OFFSET;

    public HumanlikeRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanlikeModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5f);

        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    public static float getWidthWithMultiplier(float multiplier) {
        return WIDTH * multiplier;
    }

    public static float getHeightWithMultiplier(float multiplier) {
        return HEIGHT * multiplier;
    }

    public static float getEyeHeightWithHeightMultiplier(float multiplier) {
        return (HEIGHT - EYE_HEIGHT_OFFSET) * multiplier;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T abstractHumanlikeEntity) {
        return ResourceLocation.fromNamespaceAndPath(RPGMobs.MODID, abstractHumanlikeEntity.getTextureLocation().getPath());
    }
}
