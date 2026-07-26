package com.conanthecivilian.rpgmobs.repository;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.manager.LoreManager.LoreEvent;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LoreRepository extends SavedData {
    private static final String DATA_KEY = "rpgmobs_lore";

    private final Map<UUID, LoreEvent> history = new HashMap<>();
    private final Map<Integer, List<UUID>> historyLookup = new LinkedHashMap<>();

    public LoreRepository() {
    }

    public Map<UUID, LoreEvent> getAll() {
        return Collections.unmodifiableMap(this.history);
    }

    public Map<Integer, List<LoreEvent>> getAllChronologically() {
        Map<Integer, List<LoreEvent>> chronologicalMap = new LinkedHashMap<>();

        this.historyLookup.forEach((year, uuids) ->
            chronologicalMap.put(year, getEventsByUuids(uuids))
        );

        return chronologicalMap;
    }

    public Optional<LoreEvent> getEvent(UUID uuid) {
        return Optional.ofNullable(this.history.get(uuid));
    }

    public List<LoreEvent> getEventsByYear(int year) {
        List<UUID> uuids = this.historyLookup.getOrDefault(year, List.of());
        return getEventsByUuids(uuids);
    }

    private List<LoreEvent> getEventsByUuids(List<UUID> uuids) {
        List<LoreEvent> events = new ArrayList<>();
        for (UUID id : uuids) {
            LoreEvent event = this.history.get(id);
            if (event != null) {
                events.add(event);
            }
        }
        return events;
    }

    public void addEvent(LoreEvent loreEvent) {
        this.history.put(loreEvent.uuid(), loreEvent);

        this.historyLookup
            .computeIfAbsent(loreEvent.year(), k -> new ArrayList<>())
            .add(loreEvent.uuid());

        this.setDirty();
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
        ListTag loreEventTagsList = new ListTag();

        this.history.forEach((uuid, loreEvent) -> {
            CompoundTag loreEventTag = new CompoundTag();
            loreEventTag.putUUID("UUID", uuid);

            LoreEvent.CODEC.encodeStart(NbtOps.INSTANCE, loreEvent)
                .resultOrPartial(err -> RPGMobs.LOGGER.error("Failed to save lore event {}: {}", uuid, err))
                .ifPresent(encodedLoreEvent -> loreEventTag.put("Data", encodedLoreEvent));

            loreEventTagsList.add(loreEventTag);
        });

        compoundTag.put(DATA_KEY, loreEventTagsList);
        return compoundTag;
    }

    public static LoreRepository load(CompoundTag compoundTag, HolderLookup.Provider provider) {
        LoreRepository loreRepository = new LoreRepository();

        if (compoundTag.contains(DATA_KEY, Tag.TAG_LIST)) {
            ListTag loreEventTagsList = compoundTag.getList(DATA_KEY, Tag.TAG_COMPOUND);

            for (int i = 0; i < loreEventTagsList.size(); i++) {
                CompoundTag loreEventTag = loreEventTagsList.getCompound(i);
                UUID uuid = loreEventTag.getUUID("UUID");

                LoreEvent.CODEC.parse(NbtOps.INSTANCE, loreEventTag.getCompound("Data"))
                    .resultOrPartial(err -> RPGMobs.LOGGER.error("Failed to load lore event {}: {}", uuid, err))
                    .ifPresent(loreRepository::addEvent);
            }
        }

        return loreRepository;
    }

    public static LoreRepository get(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(
            new SavedData.Factory<>(
                LoreRepository::new,
                LoreRepository::load,
                DataFixTypes.LEVEL
            ),
            DATA_KEY
        );
    }
}