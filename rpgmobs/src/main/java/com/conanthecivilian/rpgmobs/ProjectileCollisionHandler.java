package com.conanthecivilian.rpgmobs;

import com.conanthecivilian.rpgmobs.entity.custom.AbstractCombatantEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.ProjectileImpactEvent;

@EventBusSubscriber(modid = RPGMobs.MODID)
public class ProjectileCollisionHandler {
    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (!(event.getProjectile() instanceof AbstractArrow arrow)) {
            return;
        }

        if (event.getRayTraceResult().getType() == HitResult.Type.ENTITY) {
            EntityHitResult entityHitResult = (EntityHitResult) event.getRayTraceResult();

            Entity victim = entityHitResult.getEntity();
            Entity shooter = arrow.getOwner();

            if (!(shooter instanceof AbstractCombatantEntity combatantEntity)) {
                return;
            }

            if (!(victim instanceof LivingEntity victimLivingEntity)) {
                return;
            }

            if (
                combatantEntity.getTarget() != victimLivingEntity &&
                    !combatantEntity.factionManager.isEnemyFaction(victimLivingEntity)
            ) {
                event.setCanceled(true);
            }
        }
    }
}
