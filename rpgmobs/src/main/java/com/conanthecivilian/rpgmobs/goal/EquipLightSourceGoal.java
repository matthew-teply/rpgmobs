package com.conanthecivilian.rpgmobs.goal;

import com.conanthecivilian.rpgmobs.entity.custom.AbstractHumanlikeEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class EquipLightSourceGoal extends EquipOffhandGoal {
    public EquipLightSourceGoal(AbstractHumanlikeEntity mob, ItemStack item, @Nullable SoundEvent finishUsingSound) {
        super(mob, item, finishUsingSound, entity -> true);
    }

    @Override
    public boolean canUse() {
        if (this.mob.level().isDay() || this.mob.isAggressive()) {
            return false;
        }

        return super.canUse();
    }
}
