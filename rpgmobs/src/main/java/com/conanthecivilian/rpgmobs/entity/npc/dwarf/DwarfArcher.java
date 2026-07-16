package com.conanthecivilian.rpgmobs.entity.npc.dwarf;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public class DwarfArcher extends AbstractDwarf<DwarfArcher> {
    public DwarfArcher(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);

        this.setEquipment(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
    }
}
