package com.conanthecivilian.rpgmobs.entity.custom.monster;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.custom.AbstractCombatantEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.level.Level;

public abstract class AbstractOrcCombatantEntity extends AbstractCombatantEntity implements IMonsterFaction {
    public AbstractOrcCombatantEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);

        this.factionService.addEnemyFactions(this.getEnemyList());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.35F)
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.ATTACK_DAMAGE, 5.0)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    public void registerAdditionalAttackGoals() {
        super.registerAdditionalAttackGoals();
        this.targetSelector
                .addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true, false));
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return ResourceLocation.fromNamespaceAndPath(RPGMobs.MODID, "textures/entity/orc/orc.png");
    }
}
