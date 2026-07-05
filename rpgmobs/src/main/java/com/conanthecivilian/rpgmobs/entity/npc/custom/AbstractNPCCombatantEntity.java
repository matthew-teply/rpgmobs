package com.conanthecivilian.rpgmobs.entity.npc.custom;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.behavior.CrossbowAttack;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.BowAttack;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRetaliateTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public abstract class AbstractNPCCombatantEntity<T extends AbstractNPCCombatantEntity<T>> extends AbstractNPCEntity<T> implements
    SmartBrainOwner<T>,
    RangedAttackMob,
    CrossbowAttackMob {
    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(
        AbstractNPCCombatantEntity.class,
        EntityDataSerializers.BOOLEAN
    );

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

    @Override
    public boolean canFireProjectileWeapon(@NotNull ProjectileWeaponItem projectileWeapon) {
        return true;
    }

    @Override
    public boolean isHolding(Predicate<ItemStack> predicate) {
        return predicate.test(this.getItemBySlot(EquipmentSlot.MAINHAND)) || super.isHolding(predicate);
    }

    protected AbstractArrow getArrow(ItemStack arrow, float velocity, @Nullable ItemStack weapon) {
        return ProjectileUtil.getMobArrow(this, arrow, velocity, weapon);
    }

    @Override
    public @NotNull ItemStack getProjectile(@NotNull ItemStack weapon) {
        if (weapon.getItem() instanceof ProjectileWeaponItem) {
            return new ItemStack(Items.ARROW);
        }

        return super.getProjectile(weapon);
    }

    // Stolen from AbstractSkeleton
    @Override
    public void performRangedAttack(@NotNull LivingEntity target, float distanceFactor) {
        ItemStack weapon = this.getItemBySlot(EquipmentSlot.MAINHAND);

        ItemStack projectileStack = this.getProjectile(weapon);
        AbstractArrow abstractArrow = this.getArrow(projectileStack, distanceFactor, weapon);

        if (weapon.getItem() instanceof ProjectileWeaponItem weaponItem) {
            abstractArrow = weaponItem.customArrow(abstractArrow, projectileStack, weapon);
        }

        double spawnX = this.getX() + this.getLookAngle().x * 0.5D;
        double spawnY = this.getEyeY() - 0.1D;
        double spawnZ = this.getZ() + this.getLookAngle().z * 0.5D;

        abstractArrow.moveTo(spawnX, spawnY, spawnZ, abstractArrow.getYRot(), abstractArrow.getXRot());

        double d0 = target.getX() - abstractArrow.getX();
        double d1 = target.getY(0.3333333333333333) - abstractArrow.getY();
        double d2 = target.getZ() - abstractArrow.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);

        abstractArrow.shoot(d0, d1 + d3 * 0.2F, d2, 1.6F, (float) (14 - this.level().getDifficulty().getId() * 4));

        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(abstractArrow);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_CHARGING_CROSSBOW, false);
    }

    public boolean isChargingCrossbow() {
        return this.entityData.get(IS_CHARGING_CROSSBOW);
    }

    @Override
    public void setChargingCrossbow(boolean isCharging) {
        this.entityData.set(IS_CHARGING_CROSSBOW, isCharging);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        this.noActionTime = 0;
    }

    @Override
    public HumanlikeArmPose getArmPose() {
        if (this.isChargingCrossbow()) {
            return HumanlikeArmPose.CROSSBOW_CHARGE;
        } else if (this.isHolding(is -> is.getItem() instanceof CrossbowItem)) {
            return HumanlikeArmPose.CROSSBOW_HOLD;
        } else if (this.isHolding(is -> is.getItem() instanceof BowItem)) {
            return HumanlikeArmPose.BOW_AND_ARROW;
        } else {
            return HumanlikeArmPose.NEUTRAL;
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
        @NotNull ServerLevelAccessor levelAccessor,
        @NotNull DifficultyInstance difficulty,
        @NotNull MobSpawnType spawnType,
        @Nullable SpawnGroupData spawnGroupData
    ) {
        spawnGroupData = super.finalizeSpawn(levelAccessor, difficulty, spawnType, spawnGroupData);

        RandomSource random = levelAccessor.getRandom();
        this.populateDefaultEquipmentSlots(random, difficulty);

        return spawnGroupData;
    }
}
