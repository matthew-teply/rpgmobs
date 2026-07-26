package com.conanthecivilian.rpgmobs.repository;

import com.conanthecivilian.rpgmobs.manager.TraitManager.TraitScope;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TraitDatabaseRepository {
    private static final HashMap<ResourceLocation, HashMap<TraitScope, ArrayList<?>>> TRAITS_DATABASE = new HashMap<>();

    public static HashMap<ResourceLocation, HashMap<TraitScope, ArrayList<?>>> getAll() {
        return TRAITS_DATABASE;
    }

    public static <T> @Nullable List<T> get(ResourceLocation trait, TraitScope traitScope) {
        HashMap<TraitScope, ArrayList<?>> innerMap = TRAITS_DATABASE.get(trait);

        return innerMap != null ? (List<T>) innerMap.get(traitScope) : null;
    }

    public static <T> void set(ResourceLocation trait, TraitScope traitScope, T id) {
        HashMap<TraitScope, ArrayList<?>> innerMap = TRAITS_DATABASE.computeIfAbsent(trait, k -> new HashMap<>());
        ArrayList<?> rawList = innerMap.computeIfAbsent(traitScope, k -> new ArrayList<T>());

        ArrayList<T> typedList = (ArrayList<T>) rawList;

        if (!typedList.contains(id)) {
            typedList.add(id);
        }
    }

    public static <T> void setAll(List<ResourceLocation> traits, TraitScope traitScope, T id) {
        for (ResourceLocation trait : traits) {
            set(trait, traitScope, id);
        }
    }
}