package com.conanthecivilian.rpgmobs.entity.npc;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.npc.client.renderer.DwarfRenderer;
import com.conanthecivilian.rpgmobs.entity.npc.client.renderer.HumanlikeRenderer;
import com.conanthecivilian.rpgmobs.entity.npc.dwarf.DwarfArcher;
import com.conanthecivilian.rpgmobs.entity.npc.dwarf.DwarfGuard;
import com.conanthecivilian.rpgmobs.entity.npc.human.HumanArcher;
import com.conanthecivilian.rpgmobs.entity.npc.human.HumanGuard;
import com.conanthecivilian.rpgmobs.entity.npc.orc.OrcArcher;
import com.conanthecivilian.rpgmobs.entity.npc.orc.OrcFighter;
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

    public static final Supplier<EntityType<HumanGuard>> HUMAN_GUARD =
        REGISTRY.register("human_guard", () -> EntityType.Builder.of(HumanGuard::new, MobCategory.MONSTER)
            .sized(HumanlikeRenderer.WIDTH, HumanlikeRenderer.HEIGHT)
            .eyeHeight(HumanlikeRenderer.EYE_HEIGHT)
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("human_guard")
        );

    public static final Supplier<EntityType<HumanArcher>> HUMAN_ARCHER =
        REGISTRY.register("human_archer", () -> EntityType.Builder.of(HumanArcher::new, MobCategory.MONSTER)
            .sized(HumanlikeRenderer.WIDTH, HumanlikeRenderer.HEIGHT)
            .eyeHeight(HumanlikeRenderer.EYE_HEIGHT)
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("human_archer")
        );

    public static final Supplier<EntityType<DwarfGuard>> DWARF_GUARD =
        REGISTRY.register("dwarf_guard", () -> EntityType.Builder.of(DwarfGuard::new, MobCategory.MONSTER)
            .sized(
                HumanlikeRenderer.getWidthWithMultiplier(DwarfRenderer.WIDTH_MULTIPLIER),
                HumanlikeRenderer.getHeightWithMultiplier(DwarfRenderer.HEIGHT_MULTIPLIER)
            )
            .eyeHeight(HumanlikeRenderer.getEyeHeightWithHeightMultiplier(DwarfRenderer.HEIGHT_MULTIPLIER))
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("dwarf_guard")
        );

    public static final Supplier<EntityType<DwarfArcher>> DWARF_ARCHER =
        REGISTRY.register("dwarf_archer", () -> EntityType.Builder.of(DwarfArcher::new, MobCategory.MONSTER)
            .sized(
                HumanlikeRenderer.getWidthWithMultiplier(DwarfRenderer.WIDTH_MULTIPLIER),
                HumanlikeRenderer.getHeightWithMultiplier(DwarfRenderer.HEIGHT_MULTIPLIER)
            )
            .eyeHeight(HumanlikeRenderer.getEyeHeightWithHeightMultiplier(DwarfRenderer.HEIGHT_MULTIPLIER))
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("dwarf_archer")
        );

    public static final Supplier<EntityType<OrcFighter>> ORC_FIGHTER =
        REGISTRY.register("orc_fighter", () -> EntityType.Builder.of(OrcFighter::new, MobCategory.MONSTER)
            .sized(HumanlikeRenderer.WIDTH, HumanlikeRenderer.HEIGHT)
            .eyeHeight(HumanlikeRenderer.EYE_HEIGHT)
            .vehicleAttachment(Player.DEFAULT_VEHICLE_ATTACHMENT)
            .build("orc_fighter")
        );

    public static final Supplier<EntityType<OrcArcher>> ORC_ARCHER =
        REGISTRY.register("orc_archer", () -> EntityType.Builder.of(OrcArcher::new, MobCategory.MONSTER)
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
        event.put(HUMAN_GUARD.get(), HumanGuard.createAttributes().build());
        event.put(HUMAN_ARCHER.get(), HumanArcher.createAttributes().build());
        event.put(DWARF_GUARD.get(), DwarfGuard.createAttributes().build());
        event.put(DWARF_ARCHER.get(), DwarfArcher.createAttributes().build());
        event.put(ORC_FIGHTER.get(), OrcFighter.createAttributes().build());
        event.put(ORC_ARCHER.get(), OrcFighter.createAttributes().build());

        event.put(NPC.get(), com.conanthecivilian.rpgmobs.entity.npc.NPC.createAttributes().build());
    }

    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}
