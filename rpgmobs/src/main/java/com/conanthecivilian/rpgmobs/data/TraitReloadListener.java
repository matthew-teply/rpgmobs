package com.conanthecivilian.rpgmobs.data;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.trait.Trait;
import com.conanthecivilian.rpgmobs.repository.TraitRepository;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class TraitReloadListener extends SimpleJsonResourceReloadListener {
    protected final Codec<Trait> codec;
    protected final String templateLocation;

    public TraitReloadListener(
        Codec<Trait> codec,
        String templateLocation
    ) {
        super(new Gson(), templateLocation);

        this.codec = codec;
        this.templateLocation = templateLocation;
    }

    @Override
    protected void apply(
        @NotNull Map<ResourceLocation, JsonElement> parsed,
        @NotNull ResourceManager resourceManager,
        @NotNull ProfilerFiller profilerFiller
    ) {
        TraitRepository.TRAITS.clear();
        TraitRepository.TRAIT_LOOKUP.clear();

        // Map containing the raw JSON elements discovered by Minecraft
        parsed.forEach((location, jsonElement) -> {
            // Convert raw JSON elements into your clean Java Record via Codec
            this.codec.parse(JsonOps.INSTANCE, jsonElement)
                .resultOrPartial(error -> RPGMobs.LOGGER.error("Failed to parse template {}: {}", location, error))
                .ifPresent(id -> {
                    TraitRepository.setTrait(id);
                });
        });

        RPGMobs.LOGGER.info("Loaded {} event templates successfully from {}.", TraitRepository.TRAITS.size(), this.templateLocation);
    }
}
