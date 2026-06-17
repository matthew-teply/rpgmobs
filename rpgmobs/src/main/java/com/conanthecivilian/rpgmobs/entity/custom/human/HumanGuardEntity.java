package com.conanthecivilian.rpgmobs.entity.custom.human;

import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

public class HumanGuardEntity extends AbstractHumanCombatantEntity {
    public HumanGuardEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);

        this.setEquipment(EquipmentSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
    }
}
