package com.conanthecivilian.rpgmobs.entity.npc.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.CrossbowAttack;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.BowAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRetaliateTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;

public abstract class AbstractNPCCombatantEntity<T extends AbstractNPCCombatantEntity<T>> extends AbstractNPCEntity<T> implements
    SmartBrainOwner<T>,
    RangedAttackMob,
    CrossbowAttackMob {

    public AbstractNPCCombatantEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public BrainActivityGroup<T> getIdleTasks() {
        BrainActivityGroup<T> idleTasks = super.getIdleTasks();

        idleTasks.getBehaviours().addFirst(
            new TargetOrRetaliate<>()
                .attackablePredicate(entity -> this.factionManager.isEnemyFaction(entity) || entity.getLastHurtMob() == this)
                .isAllyIf((mob, entity) -> this.factionManager.isAllyFaction(entity))
                .alertAlliesWhen((mob, entity) ->
                    entity instanceof LivingEntity livingTarget && (
                        this.factionManager.isEnemyFaction(livingTarget) || livingTarget.getLastHurtMob() == this
                    )
                )
        );
        idleTasks.getBehaviours().addFirst(new SetRetaliateTarget<>());

        return idleTasks;
    }

    @Override
    public BrainActivityGroup<T> getFightTasks() {
        return BrainActivityGroup.fightTasks(
            new InvalidateAttackTarget<>(),
            new SetWalkTargetToAttackTarget<>(),
            new BowAttack<>(20).startCondition(
                entity -> entity.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof BowItem
            ),
            new CrossbowAttack<>(),
            new AnimatableMeleeAttack<>(0).startCondition(
                entity -> !(entity.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ProjectileWeaponItem)
            )
        );
    }
}
