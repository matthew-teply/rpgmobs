package com.conanthecivilian.rpgmobs.item;

import com.conanthecivilian.rpgmobs.RPGMobs;
import com.conanthecivilian.rpgmobs.entity.ModEntities;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RPGMobs.MODID);

    public static final DeferredItem<DeferredSpawnEggItem> HUMAN_ARCHER_SPAWN_EGG = ITEMS.registerItem(
        "human_archer_spawn_egg",
        properties -> new DeferredSpawnEggItem(
            ModEntities.HUMAN_ARCHER,
            0x5C4033,
            0xE2E8F0,
            properties
        )
    );

    public static final DeferredItem<DeferredSpawnEggItem> HUMAN_GUARD_SPAWN_EGG = ITEMS.registerItem(
        "human_guard_spawn_egg",
        properties -> new DeferredSpawnEggItem(
            ModEntities.HUMAN_GUARD,
            0x1E3A8A,
            0x94A3B8,
            properties
        )
    );

    public static final DeferredItem<DeferredSpawnEggItem> DWARF_ARCHER_SPAWN_EGG = ITEMS.registerItem(
        "dwarf_archer_spawn_egg",
        properties -> new DeferredSpawnEggItem(
            ModEntities.DWARF_ARCHER,
            0x5C4033,
            0xE2E8F0,
            properties
        )
    );

    public static final DeferredItem<DeferredSpawnEggItem> DWARF_GUARD_SPAWN_EGG = ITEMS.registerItem(
        "dwarf_guard_spawn_egg",
        properties -> new DeferredSpawnEggItem(
            ModEntities.DWARF_GUARD,
            0x1E3A8A,
            0x94A3B8,
            properties
        )
    );

    public static final DeferredItem<DeferredSpawnEggItem> ORC_ARCHER_SPAWN_EGG = ITEMS.registerItem(
        "orc_archer_spawn_egg",
        properties -> new DeferredSpawnEggItem(
            ModEntities.ORC_ARCHER,
            0x2D3A22,
            0x78350F,
            properties
        )
    );

    public static final DeferredItem<DeferredSpawnEggItem> ORC_FIGHTER_SPAWN_EGG = ITEMS.registerItem(
        "orc_fighter_spawn_egg",
        properties -> new DeferredSpawnEggItem(
            ModEntities.ORC_FIGHTER,
            0x1C2414,
            0x7F1D1D,
            properties
        )
    );

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
