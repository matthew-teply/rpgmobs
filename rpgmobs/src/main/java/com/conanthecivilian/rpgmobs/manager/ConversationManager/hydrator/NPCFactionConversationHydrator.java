package com.conanthecivilian.rpgmobs.manager.ConversationManager.hydrator;

import com.conanthecivilian.rpgmobs.entity.npc.AbstractNPC;
import com.conanthecivilian.rpgmobs.entity.trait.Trait;
import com.conanthecivilian.rpgmobs.repository.TraitRepository;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.atomic.AtomicInteger;

public class NPCFactionConversationHydrator implements IConversationHydrator {
    private static final String HYDRATION_ELEMENT = "{npc_faction_traits}";

    @Override
    public String hydrate(Player player, AbstractNPC<?> npc, String rawText) {
        if (npc.factionManager.getNPCFaction() != null) {
            StringBuilder traitsStringBuilder = new StringBuilder();
            AtomicInteger index = new AtomicInteger(0);

            for (ResourceLocation traitId : npc.factionManager.getNPCFaction().getTraits()) {
                index.getAndIncrement();

                Trait trait = TraitRepository.get(traitId);

                if (trait == null) {
                    continue;
                }

                if (traitsStringBuilder.isEmpty()) {
                    traitsStringBuilder.append(trait.label());
                    continue;
                }

                if (index.get() == npc.factionManager.getNPCFaction().getTraits().size()) {
                    traitsStringBuilder.append(" and ").append(trait.label());
                    continue;
                }

                traitsStringBuilder.append(", ").append(trait.label());
            }

            return rawText.replace(HYDRATION_ELEMENT, traitsStringBuilder.toString());
        }

        return null;
    }

    @Override
    public ResourceLocation getId() {
        return ResourceLocation.parse("rpgmobs:conversation_hydrator_npc_faction");
    }
}
