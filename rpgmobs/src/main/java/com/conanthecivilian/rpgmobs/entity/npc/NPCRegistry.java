package com.conanthecivilian.rpgmobs.entity.npc;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.npc.client.renderer.DwarfRenderer;
import com.conanthecivilian.rpgmobs.entity.npc.client.renderer.HumanlikeRenderer;
import com.conanthecivilian.rpgmobs.entity.npc.custom.NPCEntity;
import com.conanthecivilian.rpgmobs.entity.npc.custom.dwarf.DwarfArcherEntity;
import com.conanthecivilian.rpgmobs.entity.npc.custom.dwarf.DwarfGuardEntity;
import com.conanthecivilian.rpgmobs.entity.npc.custom.human.HumanArcherEntity;
import com.conanthecivilian.rpgmobs.entity.npc.custom.human.HumanGuardEntity;
import com.conanthecivilian.rpgmobs.entity.npc.custom.orc.OrcArcherEntity;
import com.conanthecivilian.rpgmobs.entity.npc.custom.orc.OrcFighterEntity;
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

    public static final Supplier<EntityType<NPCEntity>> NPC =
        REGISTRY.register("npc", () -> EntityType.Builder.of(NPCEntity::new, MobCategory.MONSTER)
            .sized(HumanlikeRenderer.WIDTH, HumanlikeRenderer.HEIGHT)
            .eyeHeight(HumanlikeRenderer.EYE_HEIGHT)
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("npc")
        );

    public static final Supplier<EntityType<HumanGuardEntity>> HUMAN_GUARD =
        REGISTRY.register("human_guard", () -> EntityType.Builder.of(HumanGuardEntity::new, MobCategory.MONSTER)
            .sized(HumanlikeRenderer.WIDTH, HumanlikeRenderer.HEIGHT)
            .eyeHeight(HumanlikeRenderer.EYE_HEIGHT)
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("human_guard")
        );

    public static final Supplier<EntityType<HumanArcherEntity>> HUMAN_ARCHER =
        REGISTRY.register("human_archer", () -> EntityType.Builder.of(HumanArcherEntity::new, MobCategory.MONSTER)
            .sized(HumanlikeRenderer.WIDTH, HumanlikeRenderer.HEIGHT)
            .eyeHeight(HumanlikeRenderer.EYE_HEIGHT)
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("human_archer")
        );

    public static final Supplier<EntityType<DwarfGuardEntity>> DWARF_GUARD =
        REGISTRY.register("dwarf_guard", () -> EntityType.Builder.of(DwarfGuardEntity::new, MobCategory.MONSTER)
            .sized(
                HumanlikeRenderer.getWidthWithMultiplier(DwarfRenderer.WIDTH_MULTIPLIER),
                HumanlikeRenderer.getHeightWithMultiplier(DwarfRenderer.HEIGHT_MULTIPLIER)
            )
            .eyeHeight(HumanlikeRenderer.getEyeHeightWithHeightMultiplier(DwarfRenderer.HEIGHT_MULTIPLIER))
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("dwarf_guard")
        );

    public static final Supplier<EntityType<DwarfArcherEntity>> DWARF_ARCHER =
        REGISTRY.register("dwarf_archer", () -> EntityType.Builder.of(DwarfArcherEntity::new, MobCategory.MONSTER)
            .sized(
                HumanlikeRenderer.getWidthWithMultiplier(DwarfRenderer.WIDTH_MULTIPLIER),
                HumanlikeRenderer.getHeightWithMultiplier(DwarfRenderer.HEIGHT_MULTIPLIER)
            )
            .eyeHeight(HumanlikeRenderer.getEyeHeightWithHeightMultiplier(DwarfRenderer.HEIGHT_MULTIPLIER))
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("dwarf_archer")
        );

    public static final Supplier<EntityType<OrcFighterEntity>> ORC_FIGHTER =
        REGISTRY.register("orc_fighter", () -> EntityType.Builder.of(OrcFighterEntity::new, MobCategory.MONSTER)
            .sized(HumanlikeRenderer.WIDTH, HumanlikeRenderer.HEIGHT)
            .eyeHeight(HumanlikeRenderer.EYE_HEIGHT)
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("orc_fighter")
        );

    public static final Supplier<EntityType<OrcArcherEntity>> ORC_ARCHER =
        REGISTRY.register("orc_archer", () -> EntityType.Builder.of(OrcArcherEntity::new, MobCategory.MONSTER)
            .sized(HumanlikeRenderer.WIDTH, HumanlikeRenderer.HEIGHT)
            .eyeHeight(HumanlikeRenderer.EYE_HEIGHT)
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("orc_archer")
        );

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(HUMAN_GUARD.get(), HumanlikeRenderer::new);
        event.registerEntityRenderer(HUMAN_ARCHER.get(), HumanlikeRenderer::new);
        event.registerEntityRenderer(DWARF_GUARD.get(), DwarfRenderer::new);
        event.registerEntityRenderer(DWARF_ARCHER.get(), DwarfRenderer::new);
        event.registerEntityRenderer(ORC_FIGHTER.get(), HumanlikeRenderer::new);
        event.registerEntityRenderer(ORC_ARCHER.get(), HumanlikeRenderer::new);

        event.registerEntityRenderer(NPC.get(), HumanlikeRenderer::new);
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(HUMAN_GUARD.get(), HumanGuardEntity.createAttributes().build());
        event.put(HUMAN_ARCHER.get(), HumanArcherEntity.createAttributes().build());
        event.put(DWARF_GUARD.get(), DwarfGuardEntity.createAttributes().build());
        event.put(DWARF_ARCHER.get(), DwarfArcherEntity.createAttributes().build());
        event.put(ORC_FIGHTER.get(), OrcFighterEntity.createAttributes().build());
        event.put(ORC_ARCHER.get(), OrcFighterEntity.createAttributes().build());

        event.put(NPC.get(), NPCEntity.createAttributes().build());
    }

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
