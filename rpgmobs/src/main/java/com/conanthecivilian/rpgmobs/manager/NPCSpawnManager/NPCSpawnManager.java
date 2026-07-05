package com.conanthecivilian.rpgmobs.manager.NPCSpawnManager;

import com.conanthecivilian.rpgmobs.entity.npc.custom.template.NPCTemplate;
import com.conanthecivilian.rpgmobs.entity.npc.custom.template.NPCTemplateSpawnRules;
import com.conanthecivilian.rpgmobs.repository.NPCTemplateRepository;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.WorldGenerationContext;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class NPCSpawnManager {
    public static List<NPCTemplate> getViableTemplates(
        LevelAccessor levelAccessor,
        BlockPos pos
    ) {
        if (!(levelAccessor instanceof ServerLevelAccessor serverLevelAccessor)) {
            return List.of();
        }

        ArrayList<NPCTemplate> viableTemplates = new ArrayList<>();
        ServerLevel level = serverLevelAccessor.getLevel();

        ChunkGenerator generator = level.getChunkSource().getGenerator();
        WorldGenerationContext context = new WorldGenerationContext(generator, level);

        for (NPCTemplate npcTemplate : NPCTemplateRepository.getAll().values()) {
            if (areConditionsValid(context, pos, npcTemplate.spawnRules())) {
                viableTemplates.add(npcTemplate);
            }
        }

        return viableTemplates;
    }

    public static float getHighestSpawnChance(List<NPCTemplate> npcTemplates) {
        return npcTemplates.stream()
            .map(template -> template.spawnRules().baseChance())
            .max(Float::compare)
            .orElse(0.0f);
    }

    public static @Nullable NPCTemplate determineTemplateByWeight(
        RandomSource random,
        List<NPCTemplate> npcTemplates
    ) {
        if (npcTemplates.isEmpty()) {
            return null;
        }

        int spawnPool = npcTemplates.stream()
            .map(template -> template.spawnRules().weight())
            .reduce(0, Integer::sum);

        if (spawnPool <= 0) {
            return null;
        }

        int randomRoll = random.nextInt(spawnPool);
        int spawnPoolSubtotal = 0;

        for (NPCTemplate npcTemplate : npcTemplates) {
            spawnPoolSubtotal += npcTemplate.spawnRules().weight();

            if (spawnPoolSubtotal > randomRoll) {
                return npcTemplate;
            }
        }

        return null;
    }

    private static boolean areConditionsValid(
        WorldGenerationContext context,
        BlockPos pos,
        NPCTemplateSpawnRules spawnRules
    ) {
        return isYValid(context, pos, spawnRules);
    }

    private static boolean isYValid(
        WorldGenerationContext context,
        BlockPos pos,
        NPCTemplateSpawnRules spawnRules
    ) {
        if (spawnRules.conditions().isPresent()) {
            if (spawnRules.conditions().get().y().isPresent()) {
                NPCTemplateSpawnRules.Conditions.YRange yRange = spawnRules.conditions().get().y().get();

                int minY = yRange.min().resolveY(context);
                int maxY = yRange.max().resolveY(context);

                int currentY = pos.getY();

                return currentY >= minY && currentY <= maxY;
            }
        }

        return true;
    }
}
