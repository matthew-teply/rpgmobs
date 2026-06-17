package com.conanthecivilian.rpgmobs.goal;

import com.conanthecivilian.rpgmobs.entity.custom.AbstractHumanlikeEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.UseItemGoal;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

class EquipOffhandGoal extends UseItemGoal<AbstractHumanlikeEntity> {
    public final Mob mob;
    public final ItemStack item;
    public final SoundEvent finishUsingSound;

    public EquipOffhandGoal(AbstractHumanlikeEntity mob, ItemStack item, @Nullable SoundEvent finishUsingSound, Predicate<? super AbstractHumanlikeEntity> canUseSelector) {
        super(mob, item, finishUsingSound, canUseSelector);

        this.mob = mob;
        this.item = item;
        this.finishUsingSound = finishUsingSound;
    }

    @Override
    public void start() {
        this.mob.setItemSlot(EquipmentSlot.OFFHAND, this.item.copy());
        this.mob.startUsingItem(InteractionHand.OFF_HAND);
    }

    @Override
    public void stop() {
        this.mob.setItemSlot(EquipmentSlot.OFFHAND, ItemStack.EMPTY);
        if (this.finishUsingSound != null) {
            this.mob.playSound(this.finishUsingSound, 1.0F, this.mob.getRandom().nextFloat() * 0.2F + 0.9F);
        }

    }
}
