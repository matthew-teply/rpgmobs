package com.conanthecivilian.rpgmobs.entity.npc.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record NPCRace(
    ResourceLocation id,
    List<ResourceLocation> textures,
    Map<String, NPCNamePool> namePool,
    List<String> genderPool
) {
    public static final Codec<NPCRace> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ResourceLocation.CODEC.fieldOf("id").forGetter(NPCRace::id),
        ResourceLocation.CODEC.listOf().fieldOf("textures").forGetter(NPCRace::textures),
        Codec.unboundedMap(
            Codec.STRING,
            NPCNamePool.CODEC
        ).fieldOf("name_pool").forGetter(NPCRace::namePool),
        Codec.STRING.listOf().fieldOf("gender_pool").forGetter(NPCRace::genderPool)
    ).apply(instance, NPCRace::new));

    public ResourceLocation getRandomTexture() {
        RandomSource random = RandomSource.create();

        return this.textures().get(random.nextInt(this.textures().size()));
    }

    public String getRandomName(Optional<String> gender) {
        RandomSource random = RandomSource.create();

        NPCNamePool emptyNamePool = new NPCNamePool(
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
        );

        NPCNamePool genderedNamePool = gender.isPresent()
            ? this.namePool().get(gender.get())
            : emptyNamePool;

        NPCNamePool commonNamePool = this.namePool().getOrDefault(
            "common",
            emptyNamePool
        );

        // First names
        List<String> firstNamesPool = new ArrayList<>(commonNamePool.firstNames().orElseGet(ArrayList::new));

        if (gender.isPresent()) {
            firstNamesPool.addAll(genderedNamePool.firstNames().orElse(new ArrayList<>()));
        }

        // Middle names
        List<String> middleNamesPool = new ArrayList<>(commonNamePool.middleNames().orElseGet(ArrayList::new));

        if (gender.isPresent()) {
            middleNamesPool.addAll(genderedNamePool.middleNames().orElse(new ArrayList<>()));
        }

        // Last names
        List<String> lastNamesPool = new ArrayList<>(commonNamePool.lastNames().orElseGet(ArrayList::new));

        if (gender.isPresent()) {
            lastNamesPool.addAll(genderedNamePool.lastNames().orElse(new ArrayList<>()));
        }

        MutableComponent name = Component.literal("! Name pool is completely empty !");

        if (!firstNamesPool.isEmpty()) {
            name = Component.literal(firstNamesPool.get(random.nextInt(firstNamesPool.size())));
        }

        if (!middleNamesPool.isEmpty()) {
            name.append(" " + middleNamesPool.get(random.nextInt(middleNamesPool.size())));
        }

        if (!lastNamesPool.isEmpty()) {
            name.append(" " + lastNamesPool.get(random.nextInt(lastNamesPool.size())));
        }

        return name.getString();
    }

    public String getRandomGender() {
        RandomSource random = RandomSource.create();

        return this.genderPool().get(random.nextInt(this.genderPool().size()));
    }
}
