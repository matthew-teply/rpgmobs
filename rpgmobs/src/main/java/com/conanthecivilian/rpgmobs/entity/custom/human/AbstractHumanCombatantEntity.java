package com.conanthecivilian.rpgmobs.entity.custom.human;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.custom.AbstractCombatantEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

public abstract class AbstractHumanCombatantEntity<T extends AbstractHumanCombatantEntity<T>> extends AbstractCombatantEntity<T> implements IHumanFaction {
    public AbstractHumanCombatantEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);

        this.factionService.addEnemyFactions(this.getEnemyList());
        this.factionService.addAllyFaction(IHumanFaction.class);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MOVEMENT_SPEED, 0.35F)
            .add(Attributes.MAX_HEALTH, 24.0)
            .add(Attributes.ATTACK_DAMAGE, 5.0)
            .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    public ResourceLocation getTextureLocation() {
        return ResourceLocation.fromNamespaceAndPath(RPGMobs.MODID, "textures/entity/guard/guard.png");
    }
}
