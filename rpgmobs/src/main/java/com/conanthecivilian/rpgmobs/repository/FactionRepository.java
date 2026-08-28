package com.conanthecivilian.rpgmobs.repository;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.faction.Faction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class FactionRepository extends SavedData {
    private static final String DATA_KEY = "rpgmobs_factions";

    public HashMap<UUID, Faction> FACTIONS = new HashMap<>();

    private final List<UUID> LOOKUP_ACTIVE_FACTIONS = new ArrayList<>();
    private final List<UUID> LOOKUP_INACTIVE_FACTIONS = new ArrayList<>();

    public void set(Faction faction) {
        this.FACTIONS.put(faction.getUUID(), faction);

        this.setLookupTables(faction);

        this.setDirty();
    }

    public void setActive(Faction faction) {
        this.LOOKUP_INACTIVE_FACTIONS.remove(faction.getUUID());
        this.LOOKUP_ACTIVE_FACTIONS.add(faction.getUUID());

        this.setDirty();
    }

    public void setInactive(Faction faction) {
        this.LOOKUP_ACTIVE_FACTIONS.remove(faction.getUUID());
        this.LOOKUP_INACTIVE_FACTIONS.add(faction.getUUID());

        this.setDirty();
    }

    public Faction getFaction(UUID id) {
        return this.FACTIONS.get(id);
    }

    public HashMap<UUID, Faction> getAllFactions() {
        return this.FACTIONS;
    }

    public List<UUID> getActiveFactions() {
        return this.LOOKUP_ACTIVE_FACTIONS;
    }

    public List<UUID> getInactiveFactions() {
        return this.LOOKUP_INACTIVE_FACTIONS;
    }

    public void remove(UUID id) {
        this.FACTIONS.remove(id);

        this.LOOKUP_ACTIVE_FACTIONS.remove(id);
        this.LOOKUP_INACTIVE_FACTIONS.remove(id);

        this.setDirty();
    }

    public void clear() {
        this.FACTIONS.clear();
        this.LOOKUP_ACTIVE_FACTIONS.clear();
        this.LOOKUP_INACTIVE_FACTIONS.clear();
    }

    public void setLookupTables(Faction faction) {
        if (faction.isActive()) {
            this.LOOKUP_ACTIVE_FACTIONS.add(faction.getUUID());
        } else {
            this.LOOKUP_INACTIVE_FACTIONS.add(faction.getUUID());
        }
    }

    public Faction getRandomFaction(RandomSource random) {
        return this.FACTIONS
            .values()
            .stream()
            .toList()
            .get(random.nextInt(this.FACTIONS.size()));
    }

    public Faction getRandomActiveFaction(RandomSource random) {
        UUID factionId = this.LOOKUP_ACTIVE_FACTIONS
            .stream()
            .toList()
            .get(random.nextInt(this.LOOKUP_ACTIVE_FACTIONS.size()));

        return this.getFaction(factionId);
    }

    public @Nullable Faction getRandomActiveFactionByRace(RandomSource random, ResourceLocation raceId) {
        List<UUID> raceFactions = new ArrayList<>();

        for (UUID factionID : this.LOOKUP_ACTIVE_FACTIONS) {
            Faction faction = this.getFaction(factionID);

            if (faction.getRaces().contains(raceId)) {
                raceFactions.add(faction.getUUID());
            }
        }

        if (!raceFactions.isEmpty()) {
            UUID factionId = raceFactions
                .stream()
                .toList()
                .get(random.nextInt(raceFactions.size()));

            return this.getFaction(factionId);
        }

        return null;
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        ListTag factionTagsList = new ListTag();

        this.getAllFactions().forEach((factionUUID, faction) -> {
            CompoundTag factionTag = new CompoundTag();

            factionTag.putUUID("UUID", factionUUID);

            Faction.CODEC.encodeStart(NbtOps.INSTANCE, faction)
                .resultOrPartial(err -> RPGMobs.LOGGER.error("Failed to save faction {}: {}", faction.getName(), err))
                .ifPresent(encodedFaction -> factionTag.put("Data", encodedFaction));

            factionTagsList.add(factionTag);
        });

        compoundTag.put(DATA_KEY, factionTagsList);

        return compoundTag;
    }

    public static FactionRepository load(CompoundTag compoundTag, HolderLookup.Provider provider) {
        FactionRepository factionRepository = new FactionRepository();

        factionRepository.clear();

        AtomicInteger factionsLoaded = new AtomicInteger();

        if (compoundTag.contains(DATA_KEY, Tag.TAG_LIST)) {
            ListTag factionTagsList = compoundTag.getList(DATA_KEY, Tag.TAG_COMPOUND);

            for (int i = 0; i < factionTagsList.size(); i++) {
                CompoundTag factionTag = factionTagsList.getCompound(i);
                UUID uuid = factionTag.getUUID("UUID");

                Faction.CODEC.parse(NbtOps.INSTANCE, factionTag.getCompound("Data"))
                    .resultOrPartial(err -> RPGMobs.LOGGER.error("Failed to load faction {}: {}", uuid, err))
                    .ifPresent(faction -> {
                        factionRepository.set(faction);

                        factionsLoaded.getAndIncrement();
                        RPGMobs.LOGGER.info("[FactionSavedData] Successfully loaded Faction {}", uuid);
                    });
            }
        }

        RPGMobs.LOGGER.info("[FactionSavedData] Loaded {} factions", factionsLoaded.get());

        return factionRepository;
    }

    public static FactionRepository get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(
            new SavedData.Factory<>(
                FactionRepository::new,
                FactionRepository::load,
                DataFixTypes.LEVEL
            ),
            DATA_KEY
        );
    }
}
