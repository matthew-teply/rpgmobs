package com.conanthecivilian.rpgmobs.data;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

// Reload listeners because it reloads stuff on /reload command
public class GenericReloadListener<T> extends SimpleJsonResourceReloadListener {
    private final Codec<T> codec;
    private final HashMap<ResourceLocation, T> registry;
    private final String templateLocation;

    public GenericReloadListener(
        Codec<T> codec,
        HashMap<ResourceLocation, T> registry,
        String templateLocation
    ) {
        super(new Gson(), templateLocation);

        this.codec = codec;
        this.registry = registry;
        this.templateLocation = templateLocation;
    }

    @Override
    protected void apply(
        @NotNull Map<ResourceLocation, JsonElement> parsed,
        @NotNull ResourceManager resourceManager,
        @NotNull ProfilerFiller profilerFiller
    ) {
        this.registry.clear();

        // Map containing the raw JSON elements discovered by Minecraft
        parsed.forEach((location, jsonElement) -> {
            // Convert raw JSON elements into your clean Java Record via Codec
            this.codec.parse(JsonOps.INSTANCE, jsonElement)
                .resultOrPartial(error -> RPGMobs.LOGGER.error("Failed to parse template {}: {}", location, error))
                .ifPresent(template -> {
                    String fullPath = location.getPath();
                    String filenameOnly = fullPath.substring(fullPath.lastIndexOf('/') + 1);

                    ResourceLocation cleanKey = ResourceLocation.fromNamespaceAndPath("rpgmobs", filenameOnly);

                    this.registry.put(cleanKey, template);
                });
        });

        RPGMobs.LOGGER.info("Loaded {} event templates successfully from {}.", this.registry.size(), this.templateLocation);
    }
}
