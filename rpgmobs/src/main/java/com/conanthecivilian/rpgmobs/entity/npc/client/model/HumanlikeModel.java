package com.conanthecivilian.rpgmobs.entity.npc.client.model;

import com.conanthecivilian.rpgmobs.entity.npc.custom.AbstractNPCEntity;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;
import org.jetbrains.annotations.NotNull;

public class HumanlikeModel<T extends AbstractNPCEntity<T>> extends HumanoidModel<T> {
    public HumanlikeModel(ModelPart root) {
        super(root);
    }

    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        this.setupCustomAnim(entity, netHeadYaw, headPitch);
    }

    protected void setupCustomAnim(T entity, float netHeadYaw, float headPitch) {
        this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        this.leftArmPose = HumanoidModel.ArmPose.EMPTY;

        this.head.yRot = netHeadYaw * (float) (Math.PI / 180.0);
        this.head.xRot = headPitch * (float) (Math.PI / 180.0);

        prepareRangedAnimations(entity);
    }

    private void prepareRangedAnimations(T entity) {
        AbstractNPCEntity.HumanlikeArmPose abstractbiped$bipedarmpose = entity.getArmPose();

        if (abstractbiped$bipedarmpose == AbstractNPCEntity.HumanlikeArmPose.BOW_AND_ARROW) {
            if (entity.getMainArm() == HumanoidArm.RIGHT) {
                this.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
            }
        } else if (abstractbiped$bipedarmpose == AbstractNPCEntity.HumanlikeArmPose.CROSSBOW_HOLD) {
            AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
        } else if (abstractbiped$bipedarmpose == AbstractNPCEntity.HumanlikeArmPose.CROSSBOW_CHARGE) {
            AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, entity, true);
        }
    }
}
