package com.conanthecivilian.rpgmobs.manager.NPCFactionManager;

import com.conanthecivilian.rpgmobs.data.ModAttachments;
import com.conanthecivilian.rpgmobs.entity.faction.Faction;
import com.conanthecivilian.rpgmobs.entity.npc.AbstractNPC;
import com.conanthecivilian.rpgmobs.repository.FactionRepository;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Monster;
import org.jetbrains.annotations.Nullable;

public class NPCFactionManager {
    private final AbstractNPC<?> npc;

    public FactionRepository factionRepository;

    public NPCFactionManager(AbstractNPC<?> npc) {
        this.npc = npc;
    }

    @Nullable
    private MinecraftServer getServer() {
        if (this.npc.level() instanceof ServerLevel serverLevel) {
            MinecraftServer server = serverLevel.getServer();

            this.factionRepository = FactionRepository.get(server);
            return server;
        }

        return null;
    }

    public void setNPCFaction() {
        MinecraftServer server = getServer();
        if (server == null) return;

        Faction faction = this.factionRepository.getRandomActiveFactionByRace(
            RandomSource.create(),
            this.npc.getNPCData().getRace()
        );

        if (faction != null) {
            this.npc.setData(
                ModAttachments.NPC_FACTION_ID,
                faction.getUUID()
            );
        }
    }

    public void setNPCFaction(Faction faction) {
        this.npc.setData(
            ModAttachments.NPC_FACTION_ID,
            faction.getUUID()
        );
    }

    public @Nullable Faction getNPCFaction() {
        MinecraftServer server = getServer();
        if (server == null) return null;

        return this.factionRepository.getFaction(
            this.npc.getData(ModAttachments.NPC_FACTION_ID)
        );
    }

    public boolean isEnemyFaction(LivingEntity livingEntity) {
        if (livingEntity instanceof Monster monster) {
            return !(monster instanceof Creeper) && !(monster instanceof EnderMan);
        }

        if (!(livingEntity instanceof AbstractNPC<?> targetNpc)) {
            return false;
        }

        Faction faction = this.getNPCFaction();

        if (faction != null) {
            Faction targetNPCFaction = targetNpc.factionManager.getNPCFaction();

            if (targetNPCFaction != null && !targetNPCFaction.getUUID().equals(faction.getUUID())) {
                return faction.getDiplomacy().enemies().contains(targetNPCFaction.getUUID());
            }
        }

        return false;
    }

    public boolean isAllyFaction(LivingEntity livingEntity) {
        if (livingEntity instanceof IronGolem) {
            return true;
        }

        if (!(livingEntity instanceof AbstractNPC<?> targetNpc)) {
            return false;
        }

        Faction faction = this.getNPCFaction();

        if (faction != null) {
            Faction targetNPCFaction = targetNpc.factionManager.getNPCFaction();

            if (targetNPCFaction != null) {
                if (targetNPCFaction.getUUID().equals(faction.getUUID())) {
                    return true;
                }

                return faction.getDiplomacy().allies().contains(targetNPCFaction.getUUID());
            }
        }

        return false;
    }
}
