package com.conanthecivilian.rpgmobs.entity.client;

import com.conanthecivilian.rpgmobs.entity.custom.AbstractHumanlikeEntity;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class HumanlikeModel extends HumanoidModel<AbstractHumanlikeEntity> {
    public HumanlikeModel(ModelPart root) {
        super(root);
    }

    public void setupAnim(AbstractHumanlikeEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
        this.leftArmPose = HumanoidModel.ArmPose.EMPTY;

        this.head.yRot = netHeadYaw * (float) (Math.PI / 180.0);
        this.head.xRot = headPitch * (float) (Math.PI / 180.0);

        AbstractHumanlikeEntity.BipedArmPose abstractbiped$bipedarmpose = entity.getArmPose();

        if (abstractbiped$bipedarmpose == AbstractHumanlikeEntity.BipedArmPose.BOW_AND_ARROW) {
            prepareBowAnimation(entity);
        } else if (abstractbiped$bipedarmpose == AbstractHumanlikeEntity.BipedArmPose.CROSSBOW_HOLD) {
            AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, true);
        } else if (abstractbiped$bipedarmpose == AbstractHumanlikeEntity.BipedArmPose.CROSSBOW_CHARGE) {
            AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, entity, true);
        }
    }

    private void prepareBowAnimation(AbstractHumanlikeEntity entity) {
        ItemStack itemStack = entity.getItemInHand(InteractionHand.MAIN_HAND);

        if (itemStack.is(Items.BOW) && entity.isAggressive()) {
            if (entity.getMainArm() == HumanoidArm.RIGHT) {
                this.rightArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
            } else {
                this.leftArmPose = HumanoidModel.ArmPose.BOW_AND_ARROW;
            }
        }
    }
}
