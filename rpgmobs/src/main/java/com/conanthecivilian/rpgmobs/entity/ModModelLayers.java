package com.conanthecivilian.rpgmobs.entity;

import com.conanthecivilian.rpgmobs.RPGMobs;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation DWARF = new ModelLayerLocation(
        ResourceLocation.fromNamespaceAndPath(RPGMobs.MODID, "dwarf"),
        "main"
    );
}
