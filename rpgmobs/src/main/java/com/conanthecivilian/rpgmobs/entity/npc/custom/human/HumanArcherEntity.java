package com.conanthecivilian.rpgmobs.entity.npc.custom.human;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class HumanArcherEntity extends AbstractHumanCombatantEntity<HumanArcherEntity> {
    public HumanArcherEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);

        this.setEquipment(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    }
}
