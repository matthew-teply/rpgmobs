package com.conanthecivilian.rpgmobs.entity.npc.human;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.npc.AbstractNPCCombatant;
import com.conanthecivilian.rpgmobs.manager.FactionManager.IDwarfFaction;
import com.conanthecivilian.rpgmobs.manager.FactionManager.IHumanFaction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractHumanCombatant<T extends AbstractHumanCombatant<T>> extends AbstractNPCCombatant<T> implements IHumanFaction {
    public AbstractHumanCombatant(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);

        this.factionManager.addEnemyFactions(this.getEnemyList());

        this.factionManager.addAllyFaction(IHumanFaction.class);
        this.factionManager.addAllyFaction(IDwarfFaction.class);
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
        return ResourceLocation.fromNamespaceAndPath(RPGMobs.MODID, "textures/npc/human/guard.png");
    }

    @Override
    public @NotNull Component getName() {
        return Component.translatable("Human");
    }
}
