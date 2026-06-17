package com.conanthecivilian.rpgmobs.entity.custom.monster;

import com.conanthecivilian.rpgmobs.entity.custom.utility.IRangedAttackStrafingMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class OrcArcherEntity extends AbstractOrcCombatantEntity<OrcArcherEntity> implements IRangedAttackStrafingMob {
    public OrcArcherEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);

        this.setEquipment(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
    }
}
