package com.conanthecivilian.rpgmobs.goal;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.custom.AbstractHumanlikeEntity;
import com.conanthecivilian.rpgmobs.entity.custom.utility.IRangedAttackStrafingMob;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;

import java.util.EnumSet;

public class IntelligentBowAttackGoal<T extends AbstractHumanlikeEntity & RangedAttackMob> extends Goal {
    private final T shooter;
    private final double speedModifier;
    private int attackIntervalMin;
    private final float attackRadiusSqr;
    private int attackTime;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime;

    private boolean isFleeing = false;
    private int fleeingTime;
    private boolean isOnFleeingCooldown;

    private boolean isTargetRunningTowardsMe;
    private double previousTargetDistance;

    private static final int MAX_FLEEING_TIME_IN_SECONDS = 4;

    private String stage = "";

    public IntelligentBowAttackGoal(
        T shooter,
        double speedModifier,
        int attackIntervalMin,
        float attackRadiusSqr
    ) {
        this.attackTime = -1;
        this.strafingTime = -1;
        this.fleeingTime = -1;
        this.previousTargetDistance = -1;
        this.isTargetRunningTowardsMe = false;
        this.isOnFleeingCooldown = false;
        this.shooter = shooter;
        this.speedModifier = speedModifier;
        this.attackIntervalMin = attackIntervalMin;
        this.attackRadiusSqr = attackRadiusSqr * attackRadiusSqr;

        this.shooter.setCustomNameVisible(true);

        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    public void setMinAttackInterval(int attackCooldown) {
        this.attackIntervalMin = attackCooldown;
    }

    public boolean canUse() {
        return this.shooter.getTarget() != null && this.isHoldingBow();
    }

    protected boolean isHoldingBow() {
        return this.shooter.isHolding((is) -> is.getItem() instanceof BowItem);
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.shooter.getNavigation().isDone()) && this.isHoldingBow() && !this.isFleeing;
    }

    public void start() {
        super.start();
        this.shooter.setAggressive(true);
    }

    public void stop() {
        super.stop();
        this.shooter.setAggressive(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.shooter.stopUsingItem();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public boolean canTargetReachMe(LivingEntity target) {
        if (target instanceof Mob targetMob) {
            net.minecraft.world.level.pathfinder.Path path = targetMob.getNavigation().createPath(this.shooter, 0);
            return path != null && path.canReach();
        }

        return this.shooter.getSensing().hasLineOfSight(target);
    }

    private double getXEscapeDir(LivingEntity targetEntity) {
        return this.shooter.getX() - targetEntity.getX();
    }

    private double getZEscapeDir(LivingEntity targetEntity) {
        return this.shooter.getZ() - targetEntity.getZ();
    }

    private double getXRetreatTarget(double x, double vectorLength) {
        return this.shooter.getX() + (x / vectorLength) * 8.0;
    }

    private double getZRetreatTarget(double z, double vectorLength) {
        return this.shooter.getZ() + (z / vectorLength) * 8.0;
    }

    private double getVectorLength(double x, double z) {
        return Math.sqrt(x * x + z * z);
    }

    public void tick() {
        this.shooter.setCustomName(Component.literal(this.stage + " (isFleeing: " + this.isFleeing + ")"));

        LivingEntity targetEntity = this.shooter.getTarget();

        if (targetEntity != null) {
            double distanceToTargetEntity = this.shooter.distanceToSqr(targetEntity.getX(), targetEntity.getY(), targetEntity.getZ());

            if (targetEntity instanceof Mob targetMob) {
                this.isTargetRunningTowardsMe = targetMob.getTarget() == this.shooter && distanceToTargetEntity < this.previousTargetDistance;
            } else {
                this.isTargetRunningTowardsMe = distanceToTargetEntity < this.previousTargetDistance;
            }

            this.previousTargetDistance = distanceToTargetEntity;

            boolean hasLineOfSight = this.shooter.getSensing().hasLineOfSight(targetEntity);
            boolean isTargetEntitySeen = this.seeTime > 0;

            if (hasLineOfSight != isTargetEntitySeen) {
                this.seeTime = 0;
            }

            if (hasLineOfSight) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if ((this.isOnFleeingCooldown || !this.isFleeing) && this.fleeingTime > -1) {
                --this.fleeingTime;
            }

            if (this.isOnFleeingCooldown && this.fleeingTime <= -1) {
                this.isOnFleeingCooldown = false;
            }

            if (this.isFleeing) {
                if (this.fleeingTime < MAX_FLEEING_TIME_IN_SECONDS * 20) {
                    ++this.fleeingTime;
                } else {
                    this.isOnFleeingCooldown = true;
                    this.isFleeing = false;
                }
            }

            this.shooter.getLookControl().setLookAt(targetEntity, 30.0F, 30.0F);

            RPGMobs.LOGGER.debug("{} < (double){}: {}", distanceToTargetEntity, (double) this.attackRadiusSqr, distanceToTargetEntity < (double) this.attackRadiusSqr);

            boolean canBeReached = this.canTargetReachMe(targetEntity);

            // State 1: ACTIVE FLEEING (Maintain flee state until timer runs out or cooldown hits)
            if (this.isFleeing && !this.isOnFleeingCooldown && this.isTargetRunningTowardsMe) {
                this.stage = "Fleeing (Maintaining)";

                // Check if we have calculated a retreat point. If navigation is idle, refresh it.
                if (this.shooter.getNavigation().isDone()) {
                    double xEscapeDir = this.getXEscapeDir(targetEntity);
                    double zEscapeDir = this.getZEscapeDir(targetEntity);

                    double vectorLength = this.getVectorLength(xEscapeDir, zEscapeDir);

                    if (vectorLength > 0) {
                        // Project precisely 8 blocks away from the enemy (No radius squaring addition!)
                        double xRetreatTarget = this.getXRetreatTarget(xEscapeDir, vectorLength);
                        double zRetreatTarget = this.getZRetreatTarget(zEscapeDir, vectorLength);

                        this.shooter.getNavigation().moveTo(xRetreatTarget, this.shooter.getY(), zRetreatTarget, this.speedModifier);
                    }
                }

                // State 2: TRIGGER NEW FLEE (Target entered the danger zone and can reach us)
            } else if (!this.isOnFleeingCooldown && canBeReached && distanceToTargetEntity < (double) (this.attackRadiusSqr / 4)) {
                this.stage = "Fleeing (Triggered)";
                this.isFleeing = true;
                this.strafingTime = -1; // Terminate strafing states

                double xEscapeDir = this.getXEscapeDir(targetEntity);
                double zEscapeDir = this.getZEscapeDir(targetEntity);

                double vectorLength = this.getVectorLength(xEscapeDir, zEscapeDir);

                if (vectorLength > 0) {
                    // Fixed math projection
                    double xRetreatTarget = this.getXRetreatTarget(xEscapeDir, vectorLength);
                    double zRetreatTarget = this.getZRetreatTarget(zEscapeDir, vectorLength);

                    this.shooter.getNavigation().moveTo(xRetreatTarget, this.shooter.getY(), zRetreatTarget, this.speedModifier);
                }

                // State 3: IDEAL RANGE - HOLD POSITION & STRAFE (Target is outside danger zone, but within firing range)
            } else if (distanceToTargetEntity <= (double) this.attackRadiusSqr && this.seeTime >= 20) {
                this.stage = "Holding position";
                this.isFleeing = false;

                // Stop standard pathfinding navigation completely to lock coordinates
                this.shooter.getNavigation().stop();
                ++this.strafingTime; // Increment time to allow side-to-side strafing calculations

                // State 4: TARGET IS TOO FAR AWAY (Close the gap)
            } else {
                this.stage = "Closing Gap";
                this.isFleeing = false;
                this.strafingTime = -1;

                double xDiff = targetEntity.getX() - this.shooter.getX();
                double zDiff = targetEntity.getZ() - this.shooter.getZ();
                double currentDistance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);

                if (currentDistance > 0) {
                    // Calculate your actual linear attack radius (square root of attackRadiusSqr)
                    // Subtract a 2-block buffer so they don't stop exactly on the razor's edge of their range
                    double maxLinearRange = Math.sqrt(this.attackRadiusSqr) - 2.0D;

                    // How far the shooter actually needs to advance to hit that outer ring
                    double advanceDistance = currentDistance - maxLinearRange;

                    if (advanceDistance > 0) {
                        // Normalize the direction vector and scale it by the advance distance
                        double xTargetPos = this.shooter.getX() + (xDiff / currentDistance) * advanceDistance;
                        double zTargetPos = this.shooter.getZ() + (zDiff / currentDistance) * advanceDistance;

                        // Pathfind to the precise edge of the firing envelope
                        this.shooter.getNavigation().moveTo(xTargetPos, this.shooter.getY(), zTargetPos, this.speedModifier);
                    } else {
                        // Safety fallback: if they are somehow already within range, just stand still
                        this.shooter.getNavigation().stop();
                    }
                }
            }

            if (this.shooter instanceof IRangedAttackStrafingMob) {
                if (this.strafingTime >= 20) {
                    if ((double) this.shooter.getRandom().nextFloat() < 0.3) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double) this.shooter.getRandom().nextFloat() < 0.3) {
                        this.strafingBackwards = !this.strafingBackwards;
                    }

                    this.strafingTime = 0;
                }
            }

            if (this.strafingTime > -1 && this.shooter instanceof IRangedAttackStrafingMob && !this.isFleeing) {
                if (distanceToTargetEntity > (double) (this.attackRadiusSqr * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (distanceToTargetEntity < (double) (this.attackRadiusSqr * 0.25F)) {
                    this.strafingBackwards = true;
                }

                this.shooter.getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);

                Entity controlledVehicle = this.shooter.getControlledVehicle();

                if (controlledVehicle instanceof Mob controlledVehicleMob) {
                    controlledVehicleMob.lookAt(targetEntity, 30.0F, 30.0F);
                }

                this.shooter.lookAt(targetEntity, 30.0F, 30.0F);
            }

            if (this.isFleeing) {
                this.shooter.stopUsingItem();
            } else if (this.shooter.isUsingItem()) {
                if (!hasLineOfSight && this.seeTime < -60) {
                    this.shooter.stopUsingItem();
                } else if (hasLineOfSight) {
                    int itemTick = this.shooter.getTicksUsingItem();

                    if (itemTick >= 20) {
                        this.shooter.stopUsingItem();
                        this.shooter.performRangedAttack(targetEntity, BowItem.getPowerForTime(itemTick));
                        this.attackTime = this.attackIntervalMin;
                    }
                }
            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                this.shooter.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.shooter, (item) -> item instanceof BowItem));
            }
        }

    }
}
