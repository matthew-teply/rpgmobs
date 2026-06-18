package com.conanthecivilian.rpgmobs.entity;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.client.DwarfRenderer;
import com.conanthecivilian.rpgmobs.entity.client.HumanlikeRenderer;
import com.conanthecivilian.rpgmobs.entity.custom.dwarf.DwarfArcherEntity;
import com.conanthecivilian.rpgmobs.entity.custom.dwarf.DwarfGuardEntity;
import com.conanthecivilian.rpgmobs.entity.custom.human.HumanArcherEntity;
import com.conanthecivilian.rpgmobs.entity.custom.human.HumanGuardEntity;
import com.conanthecivilian.rpgmobs.entity.custom.monster.OrcArcherEntity;
import com.conanthecivilian.rpgmobs.entity.custom.monster.OrcFighterEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
        DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, RPGMobs.MODID);

    public static final Supplier<EntityType<HumanGuardEntity>> HUMAN_GUARD =
        ENTITY_TYPES.register("human_guard", () -> EntityType.Builder.of(HumanGuardEntity::new, MobCategory.MONSTER)
            .sized(HumanlikeRenderer.WIDTH, HumanlikeRenderer.HEIGHT)
            .eyeHeight(HumanlikeRenderer.EYE_HEIGHT)
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("human_guard")
        );

    public static final Supplier<EntityType<HumanArcherEntity>> HUMAN_ARCHER =
        ENTITY_TYPES.register("human_archer", () -> EntityType.Builder.of(HumanArcherEntity::new, MobCategory.MONSTER)
            .sized(HumanlikeRenderer.WIDTH, HumanlikeRenderer.HEIGHT)
            .eyeHeight(HumanlikeRenderer.EYE_HEIGHT)
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("human_archer")
        );

    public static final Supplier<EntityType<DwarfGuardEntity>> DWARF_GUARD =
        ENTITY_TYPES.register("dwarf_guard", () -> EntityType.Builder.of(DwarfGuardEntity::new, MobCategory.MONSTER)
            .sized(
                HumanlikeRenderer.getWidthWithMultiplier(DwarfRenderer.WIDTH_MULTIPLIER),
                HumanlikeRenderer.getHeightWithMultiplier(DwarfRenderer.HEIGHT_MULTIPLIER)
            )
            .eyeHeight(HumanlikeRenderer.getEyeHeightWithHeightMultiplier(DwarfRenderer.HEIGHT_MULTIPLIER))
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("dwarf_guard")
        );

    public static final Supplier<EntityType<DwarfArcherEntity>> DWARF_ARCHER =
        ENTITY_TYPES.register("dwarf_archer", () -> EntityType.Builder.of(DwarfArcherEntity::new, MobCategory.MONSTER)
            .sized(
                HumanlikeRenderer.getWidthWithMultiplier(DwarfRenderer.WIDTH_MULTIPLIER),
                HumanlikeRenderer.getHeightWithMultiplier(DwarfRenderer.HEIGHT_MULTIPLIER)
            )
            .eyeHeight(HumanlikeRenderer.getEyeHeightWithHeightMultiplier(DwarfRenderer.HEIGHT_MULTIPLIER))
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("dwarf_archer")
        );

    public static final Supplier<EntityType<OrcFighterEntity>> ORC_FIGHTER =
        ENTITY_TYPES.register("orc_fighter", () -> EntityType.Builder.of(OrcFighterEntity::new, MobCategory.MONSTER)
            .sized(HumanlikeRenderer.WIDTH, HumanlikeRenderer.HEIGHT)
            .eyeHeight(HumanlikeRenderer.EYE_HEIGHT)
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("orc_fighter")
        );

    public static final Supplier<EntityType<OrcArcherEntity>> ORC_ARCHER =
        ENTITY_TYPES.register("orc_archer", () -> EntityType.Builder.of(OrcArcherEntity::new, MobCategory.MONSTER)
            .sized(HumanlikeRenderer.WIDTH, HumanlikeRenderer.HEIGHT)
            .eyeHeight(HumanlikeRenderer.EYE_HEIGHT)
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("orc_archer")
        );

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.HUMAN_GUARD.get(), HumanlikeRenderer::new);
        event.registerEntityRenderer(ModEntities.HUMAN_ARCHER.get(), HumanlikeRenderer::new);
        event.registerEntityRenderer(ModEntities.DWARF_GUARD.get(), DwarfRenderer::new);
        event.registerEntityRenderer(ModEntities.DWARF_ARCHER.get(), DwarfRenderer::new);
        event.registerEntityRenderer(ModEntities.ORC_FIGHTER.get(), HumanlikeRenderer::new);
        event.registerEntityRenderer(ModEntities.ORC_ARCHER.get(), HumanlikeRenderer::new);
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.HUMAN_GUARD.get(), HumanGuardEntity.createAttributes().build());
        event.put(ModEntities.HUMAN_ARCHER.get(), HumanArcherEntity.createAttributes().build());
        event.put(ModEntities.DWARF_GUARD.get(), DwarfGuardEntity.createAttributes().build());
        event.put(ModEntities.DWARF_ARCHER.get(), DwarfArcherEntity.createAttributes().build());
        event.put(ModEntities.ORC_FIGHTER.get(), OrcFighterEntity.createAttributes().build());
        event.put(ModEntities.ORC_ARCHER.get(), OrcFighterEntity.createAttributes().build());
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
