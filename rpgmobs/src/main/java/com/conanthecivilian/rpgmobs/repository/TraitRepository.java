package com.conanthecivilian.rpgmobs.repository;

import com.conanthecivilian.rpgmobs.entity.trait.Trait;
import com.conanthecivilian.rpgmobs.manager.TraitManager.TraitScope;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TraitRepository {
    public static final String TRAIT_TEMPLATES_LOCATION = "trait";

    public static final HashMap<ResourceLocation, Trait> TRAITS = new HashMap<>();

    // Scope (NPC) -> Category (Attitude) -> ResourceLocation[]
    public static final HashMap<String, HashMap<String, List<ResourceLocation>>> TRAIT_LOOKUP = new HashMap<>();

    public static void set(Trait trait) {
        trait.scope().forEach(traitScope ->
            TRAIT_LOOKUP
                .computeIfAbsent(traitScope.getSerializedName(), k -> new HashMap<>())
                .computeIfAbsent(trait.category(), k -> new ArrayList<>())
                .add(trait.id())
        );

        TRAITS.put(trait.id(), trait);
    }

    public static @Nullable Trait get(ResourceLocation id) {
        return TRAITS.get(id);
    }

    public static @Nullable List<Trait> get(List<ResourceLocation> traitIds) {
        List<Trait> traits = new ArrayList<>();

        traitIds.forEach(traitId -> traits.add(get(traitId)));

        if (traits.isEmpty()) {
            return null;
        }

        return traits;
    }

    public static @NotNull List<Trait> getByScopeAndCategory(TraitScope type, String category) {
        List<ResourceLocation> traitIds = TRAIT_LOOKUP
            .computeIfAbsent(type.name, k -> new HashMap<>())
            .computeIfAbsent(category, k -> new ArrayList<>());

        return traitIds.stream().map(TraitRepository::get).toList();
    }
}
