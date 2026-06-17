package com.conanthecivilian.rpgmobs.entity.custom;

import com.conanthecivilian.rpgmobs.goal.IntelligentBowAttackGoal;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public abstract class AbstractCombatantEntity extends AbstractHumanlikeEntity implements RangedAttackMob, CrossbowAttackMob {
    private static final EntityDataAccessor<Boolean> IS_CHARGING_CROSSBOW = SynchedEntityData.defineId(
        AbstractCombatantEntity.class,
        EntityDataSerializers.BOOLEAN
    );

    protected IntelligentBowAttackGoal<AbstractCombatantEntity> rangedBowAttackGoal;
    protected RangedCrossbowAttackGoal<AbstractCombatantEntity> rangedCrossbowAttackGoal;
    protected MeleeAttackGoal meleeAttackGoal;

    public AbstractCombatantEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public void registerCombatStyleGoals() {
        if (!this.level().isClientSide()) {
            Item weaponItem = this.getItemBySlot(EquipmentSlot.MAINHAND).getItem();

            if (weaponItem instanceof AirItem) {
                return;
            }

            if (this.rangedBowAttackGoal == null) {
                this.rangedBowAttackGoal = new IntelligentBowAttackGoal<>(this, 1.2, 20, 15.0F);
            }

            if (this.rangedCrossbowAttackGoal == null) {
                this.rangedCrossbowAttackGoal = new RangedCrossbowAttackGoal<>(this, 1.0, 8.0F);
            }

            if (this.meleeAttackGoal == null) {
                this.meleeAttackGoal = new MeleeAttackGoal(this, 1.0, false);
            }

            this.goalSelector.removeGoal(this.meleeAttackGoal);
            this.goalSelector.removeGoal(this.rangedBowAttackGoal);
            this.goalSelector.removeGoal(this.rangedCrossbowAttackGoal);

            if (weaponItem instanceof ProjectileWeaponItem) {
                if (weaponItem instanceof CrossbowItem) {
                    this.goalSelector.addGoal(1, this.rangedCrossbowAttackGoal);
                } else {
                    this.goalSelector.addGoal(1, this.rangedBowAttackGoal);
                }
            } else {
                this.goalSelector.addGoal(1, this.meleeAttackGoal);
            }
        }
    }

    @Override
    protected void registerGoals() {
        this.registerCombatStyleGoals();
        this.registerEnemyFactionAttackGoals();

        super.registerGoals();

        this.registerAdditionalAttackGoals();
    }

    public void registerAdditionalAttackGoals() {}

    public void registerEnemyFactionAttackGoals() {
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(
            this,
            LivingEntity.class,
            10,
            true,
            false,
            entity -> !(entity instanceof Creeper) && this.factionService.isEnemyFaction(entity)
        ));
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
    public void setItemSlot(@NotNull EquipmentSlot slot, @NotNull ItemStack stack) {
        super.setItemSlot(slot, stack);

        if (!this.level().isClientSide) {
            this.registerCombatStyleGoals();
        }
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

        abstractArrow.shoot(d0, d1 + d3 * 0.2F, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));

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
    public BipedArmPose getArmPose() {
        if (this.isFleeing) {
            return BipedArmPose.NEUTRAL;
        } else if (this.isChargingCrossbow()) {
            return BipedArmPose.CROSSBOW_CHARGE;
        } else if (this.isHolding(is -> is.getItem() instanceof CrossbowItem)) {
            return BipedArmPose.CROSSBOW_HOLD;
        } else if (this.isHolding(is -> is.getItem() instanceof BowItem)) {
            return BipedArmPose.BOW_AND_ARROW;
        } else {
            return BipedArmPose.NEUTRAL;
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
