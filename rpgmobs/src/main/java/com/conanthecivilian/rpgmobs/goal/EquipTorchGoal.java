package com.conanthecivilian.rpgmobs.goal;

import com.conanthecivilian.rpgmobs.entity.custom.AbstractHumanlikeEntity;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class EquipTorchGoal extends EquipLightSourceGoal {
    public EquipTorchGoal(AbstractHumanlikeEntity mob, @Nullable SoundEvent finishUsingSound) {
        super(mob, new ItemStack(Items.TORCH), finishUsingSound);
    }
}
