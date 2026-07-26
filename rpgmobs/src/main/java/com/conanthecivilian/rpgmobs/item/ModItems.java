package com.conanthecivilian.rpgmobs.item;

import com.conanthecivilian.rpgmobs.RPGMobs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(RPGMobs.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
