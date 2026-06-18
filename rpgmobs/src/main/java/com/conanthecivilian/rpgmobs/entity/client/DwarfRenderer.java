package com.conanthecivilian.rpgmobs.entity.client;

import com.conanthecivilian.rpgmobs.entity.custom.AbstractHumanlikeEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

public class DwarfRenderer<T extends AbstractHumanlikeEntity<T>> extends HumanlikeRenderer<T> {
    public static final float WIDTH_MULTIPLIER = 1.05F;
    public static final float HEIGHT_MULTIPLIER = 0.8F;

    public DwarfRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void scale(@NotNull T livingEntity, PoseStack poseStack, float partialTickTime) {
        poseStack.scale(WIDTH_MULTIPLIER, HEIGHT_MULTIPLIER, WIDTH_MULTIPLIER);

        super.scale(livingEntity, poseStack, partialTickTime);
    }
}
