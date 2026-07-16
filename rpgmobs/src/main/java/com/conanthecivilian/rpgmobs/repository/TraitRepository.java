package com.conanthecivilian.rpgmobs.repository;

import com.conanthecivilian.rpgmobs.entity.trait.Trait;
import com.conanthecivilian.rpgmobs.manager.TraitManager.TraitType;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TraitRepository {
    public static final String TRAIT_TEMPLATES_LOCATION = "trait";

    public static final HashMap<ResourceLocation, Trait> TRAITS = new HashMap<>();

    // Type -> Category -> ResourceLocation[]
    public static final HashMap<String, HashMap<String, List<ResourceLocation>>> TRAIT_LOOKUP = new HashMap<>();

    public static void setTrait(Trait trait) {
        trait.type().forEach(traitType ->
            TRAIT_LOOKUP
                .computeIfAbsent(traitType, k -> new HashMap<>())
                .computeIfAbsent(trait.category(), k -> new ArrayList<>())
                .add(trait.id())
        );

        TRAITS.put(trait.id(), trait);
    }

    public static @Nullable Trait getTrait(ResourceLocation id) {
        return TRAITS.get(id);
    }

    public static @NotNull List<Trait> getTraits(TraitType type, String category) {
        List<ResourceLocation> traitIds = TRAIT_LOOKUP
            .computeIfAbsent(type.id, k -> new HashMap<>())
            .computeIfAbsent(category, k -> new ArrayList<>());

        return traitIds.stream().map(TraitRepository::getTrait).toList();
    }

    public static @NotNull List<Trait> getTraits(List<ResourceLocation> traitIds) {
        List<Trait> traits = new ArrayList<>();

        traitIds.forEach(traitId -> traits.add(getTrait(traitId)));

        return traits;
    }
}
