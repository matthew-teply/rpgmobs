package com.conanthecivilian.rpgmobs.entity;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.npc.AbstractNPC;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Enemy;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = RPGMobs.MODID)
public class MobAIPatcher {
    @SubscribeEvent
    public static void onEntityJoinLeveL(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        if (event.getEntity() instanceof Mob mob && mob instanceof Enemy) {
            if (mob instanceof Creeper || mob instanceof EnderMan) {
                return;
            }

            mob.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(
                mob,
                Mob.class,
                10,
                true,
                false,
                entity -> entity instanceof AbstractNPC<?>
            ));
        }
    }
}
