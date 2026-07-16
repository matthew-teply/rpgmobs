package com.conanthecivilian.rpgmobs.entity.npc.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.Map;

public record NPCRace(
    ResourceLocation id,
    List<ResourceLocation> textures,
    Map<String, Map<String, List<String>>> names
) {
    public static final Codec<NPCRace> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("id").forGetter(NPCRace::id),
        ResourceLocation.CODEC.listOf().fieldOf("textures").forGetter(NPCRace::textures),
        Codec.unboundedMap(
            Codec.STRING,
            Codec.unboundedMap(
                Codec.STRING,
                Codec.STRING.listOf()
            )
        ).fieldOf("names").forGetter(NPCRace::names)
    ).apply(instance, NPCRace::new));

    public ResourceLocation getRandomTexture() {
        RandomSource random = RandomSource.create();

        return this.textures().get(random.nextInt(this.textures().size()));
    }

    public String getRandomName() {
        RandomSource random = RandomSource.create();

        List<String> firstNames = this.names()
            .get("first")
            .get("male");

        List<String> lastNames = this.names()
            .getOrDefault("last", Map.of())
            .get("common");

        if (lastNames == null) {
            return firstNames.get(random.nextInt(firstNames.size()));
        }

        return
            firstNames.get(random.nextInt(firstNames.size())) + " " + lastNames.get(random.nextInt(lastNames.size()));
    }
}
