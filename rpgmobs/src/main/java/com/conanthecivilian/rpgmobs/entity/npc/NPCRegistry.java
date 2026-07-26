package com.conanthecivilian.rpgmobs.entity.npc;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.npc.client.renderer.HumanlikeRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NPCRegistry {
    public static final DeferredRegister<EntityType<?>> REGISTRY =
        DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, RPGMobs.MODID);

    public static final Supplier<EntityType<NPC>> NPC =
        REGISTRY.register("npc", () -> EntityType.Builder.of(com.conanthecivilian.rpgmobs.entity.npc.NPC::new, MobCategory.MONSTER)
            .sized(HumanlikeRenderer.WIDTH, HumanlikeRenderer.HEIGHT)
            .eyeHeight(HumanlikeRenderer.EYE_HEIGHT)
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("npc")
        );

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(NPC.get(), HumanlikeRenderer::new);
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(NPC.get(), com.conanthecivilian.rpgmobs.entity.npc.NPC.createAttributes().build());
    }

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
