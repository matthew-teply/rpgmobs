package com.conanthecivilian.rpgmobs.item;

import com.conanthecivilian.rpgmobs.RPGMobs;
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
        }
    }
}
