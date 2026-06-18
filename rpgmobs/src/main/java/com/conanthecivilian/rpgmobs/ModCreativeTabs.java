package com.conanthecivilian.rpgmobs;

import com.conanthecivilian.rpgmobs.item.ModItems;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@EventBusSubscriber(modid = RPGMobs.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModCreativeTabs {
    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        // Add your spawn eggs alongside vanilla ones
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.HUMAN_ARCHER_SPAWN_EGG.get());
            event.accept(ModItems.HUMAN_GUARD_SPAWN_EGG.get());
            event.accept(ModItems.DWARF_ARCHER_SPAWN_EGG.get());
            event.accept(ModItems.DWARF_GUARD_SPAWN_EGG.get());
            event.accept(ModItems.ORC_ARCHER_SPAWN_EGG.get());
            event.accept(ModItems.ORC_FIGHTER_SPAWN_EGG.get());
        }
    }
}
