package com.maxwell.gunsmiths_gadgetsn_gizmos.api.gun;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.Item;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class MuzzleBoneAutoLoader {
    private static final Map<Identifier, MuzzleOffset> AUTO_OFFSETS = new HashMap<>();

    public static MuzzleOffset getOffset(Item gunItem) {
        Identifier itemId = BuiltInRegistries.ITEM.getKey(gunItem);
        return AUTO_OFFSETS.computeIfAbsent(itemId, MuzzleBoneAutoLoader::loadFromModelJson);
    }

    private static MuzzleOffset loadFromModelJson(Identifier itemId) {
        Identifier modelLocation = Identifier.fromNamespaceAndPath(
                itemId.getNamespace(),
                "geckolib/models/item/" + itemId.getPath() + ".geo.json"
        );
        try {
            ResourceManager resourceManager = Minecraft.getInstance().getResourceManager();
            var resourceOpt = resourceManager.getResource(modelLocation);
            if (resourceOpt.isPresent()) {
                try (Reader reader = resourceOpt.get().openAsReader()) {
                    JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();
                    JsonArray geometries = root.getAsJsonArray("minecraft:geometry");
                    if (geometries != null && !geometries.isEmpty()) {
                        JsonArray bones = geometries.get(0).getAsJsonObject().getAsJsonArray("bones");
                        if (bones != null) {
                            for (JsonElement boneEl : bones) {
                                JsonObject boneObj = boneEl.getAsJsonObject();
                                String name = boneObj.get("name").getAsString();
                                if ("muzzle".equalsIgnoreCase(name)
                                        || "muzzle_flash".equalsIgnoreCase(name)
                                        || "attachment_bayonet".equalsIgnoreCase(name)) {
                                    JsonArray pivot = boneObj.getAsJsonArray("pivot");
                                    if (pivot != null && pivot.size() >= 3) {
                                        double px = pivot.get(0).getAsDouble();
                                        double py = pivot.get(1).getAsDouble();
                                        double pz = pivot.get(2).getAsDouble();
                                        return new MuzzleOffset(px, py, pz);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
        return MuzzleOffset.DEFAULT;
    }

    public static void clearCache() {
        AUTO_OFFSETS.clear();
    }
}