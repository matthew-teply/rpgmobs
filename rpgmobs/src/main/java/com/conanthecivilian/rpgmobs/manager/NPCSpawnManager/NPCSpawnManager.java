package com.conanthecivilian.rpgmobs.manager.NPCSpawnManager;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.npc.custom.template.NPCTemplateEntity;
import com.conanthecivilian.rpgmobs.entity.npc.custom.template.NPCTemplateSpawnRulesEntity;
import com.conanthecivilian.rpgmobs.repository.NPCTemplateRepository;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.structure.Structure;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NPCSpawnManager {
    public static List<NPCTemplateEntity> getViableTemplates(
        LevelAccessor levelAccessor,
        BlockPos pos
    ) {
        if (!(levelAccessor instanceof ServerLevelAccessor serverLevelAccessor)) {
            return List.of();
        }

        ArrayList<NPCTemplateEntity> viableTemplates = new ArrayList<>();
        ServerLevel level = serverLevelAccessor.getLevel();

        ChunkGenerator generator = level.getChunkSource().getGenerator();
        WorldGenerationContext context = new WorldGenerationContext(generator, level);

        for (NPCTemplateEntity npcTemplate : NPCTemplateRepository.getAll().values()) {
            if (areConditionsValid(context, pos, npcTemplate.spawnRules())) {
                viableTemplates.add(npcTemplate);
            }
        }

        return viableTemplates;
    }

    public static float getHighestSpawnChance(List<NPCTemplateEntity> npcTemplates) {
        return npcTemplates.stream()
            .map(template -> template.spawnRules().baseChance())
            .max(Float::compare)
            .orElse(0.0f);
    }

    public static @Nullable NPCTemplateEntity determineTemplateByWeight(
        ServerLevelAccessor levelAccessor,
        BlockPos pos,
        RandomSource random,
        List<NPCTemplateEntity> npcTemplates
    ) {
        if (npcTemplates.isEmpty()) {
            return null;
        }

        HashMap<ResourceLocation, Integer> spawnWeights = new HashMap<>();

        // Apply multipliers
        npcTemplates.forEach(npcTemplate -> {
            spawnWeights.put(npcTemplate.id(), applyWeightMultipliers(
                npcTemplate.spawnRules(),
                levelAccessor.getLevel(),
                pos
            ));
        });

        int spawnPool = spawnWeights
            .values()
            .stream()
            .reduce(0, Integer::sum);

        if (spawnPool <= 0) {
            return null;
        }

        int randomRoll = random.nextInt(spawnPool);
        int spawnPoolSubtotal = 0;

        for (NPCTemplateEntity npcTemplate : npcTemplates) {
            spawnPoolSubtotal += npcTemplate.spawnRules().weight();

            if (spawnPoolSubtotal > randomRoll) {
                return npcTemplate;
            }
        }

        return null;
    }

    private static int applyWeightMultipliers(
        NPCTemplateSpawnRulesEntity spawnRules,
        ServerLevel level,
        BlockPos pos
    ) {
        int weight = spawnRules.weight();

        if (spawnRules.multipliers().isPresent()) {
            applyStructureWeightMultipliers(weight, spawnRules.multipliers().get(), level, pos);
        }

        return weight;
    }

    private static void applyStructureWeightMultipliers(
        int weight,
        NPCTemplateSpawnRulesEntity.Multipliers multipliers,
        ServerLevel level,
        BlockPos pos
    ) {
        if (multipliers.structures().isEmpty()) {
            return;
        }

        for (ResourceLocation structureId : multipliers.structures().keySet()) {
            if (isInStructure(level, pos, structureId)) {
                // RPGMobs.LOGGER.info("Structure ({}) spawn weight multiplier applied", structureId);
                weight *= multipliers.structures().get(structureId);
            }
        }
    }

    private static boolean isInStructure(ServerLevel level, BlockPos pos, ResourceLocation structureId) {
        Registry<Structure> structureRegistry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);

        Structure structure = structureRegistry.get(structureId);

        if (structure == null) {
            RPGMobs.LOGGER.error("Structure \"{}\" not found", structureId);
            return false;
        }

        return level.structureManager().getStructureAt(pos, structure).isValid();
    }

    private static boolean areConditionsValid(
        WorldGenerationContext context,
        BlockPos pos,
        NPCTemplateSpawnRulesEntity spawnRules
    ) {
        return isYValid(context, pos, spawnRules);
    }

    private static boolean isYValid(
        WorldGenerationContext context,
        BlockPos pos,
        NPCTemplateSpawnRulesEntity spawnRules
    ) {
        if (spawnRules.conditions().isPresent()) {
            if (spawnRules.conditions().get().y().isPresent()) {
                NPCTemplateSpawnRulesEntity.Conditions.YRange yRange = spawnRules.conditions().get().y().get();

                int minY = yRange.min().resolveY(context);
                int maxY = yRange.max().resolveY(context);

                int currentY = pos.getY();

                return currentY >= minY && currentY <= maxY;
            }
        }

        return true;
    }
}
