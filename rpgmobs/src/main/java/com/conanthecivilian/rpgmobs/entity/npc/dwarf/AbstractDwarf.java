package com.conanthecivilian.rpgmobs.entity.npc.dwarf;

import com.conanthecivilian.rpgmobs.entity.npc.AbstractNPCCombatant;
import com.conanthecivilian.rpgmobs.entity.npc.data.NPCData;
import com.conanthecivilian.rpgmobs.entity.npc.data.NPCRace;
import com.conanthecivilian.rpgmobs.manager.FactionManager.IDwarfFaction;
import com.conanthecivilian.rpgmobs.manager.FactionManager.IHumanFaction;
import com.conanthecivilian.rpgmobs.repository.RaceRepository;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class AbstractDwarf<T extends AbstractDwarf<T>> extends AbstractNPCCombatant<T> implements IDwarfFaction {
    public AbstractDwarf(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);

        this.factionManager.addEnemyFactions(this.getEnemyList());

        this.factionManager.addAllyFaction(IDwarfFaction.class);
        this.factionManager.addAllyFaction(IHumanFaction.class);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
            .add(Attributes.MOVEMENT_SPEED, 0.35F)
            .add(Attributes.MAX_HEALTH, 24.0)
            .add(Attributes.ATTACK_DAMAGE, 5.0)
            .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
        @NotNull ServerLevelAccessor levelAccessor,
        @NotNull DifficultyInstance difficulty,
        @NotNull MobSpawnType spawnType,
        @Nullable SpawnGroupData spawnGroupData
    ) {
        SpawnGroupData superSpawnGroupData = super.finalizeSpawn(levelAccessor, difficulty, spawnType, spawnGroupData);

        NPCRace race = RaceRepository.get(ResourceLocation.parse("rpgmobs:race_dwarf"));

        NPCData npcData = this.getNPCData();

        npcData.setRace(race.id());
        npcData.setTexture(Optional.of(race.getRandomTexture()));

        this.setNPCData(npcData);

        return superSpawnGroupData;
    }
}
