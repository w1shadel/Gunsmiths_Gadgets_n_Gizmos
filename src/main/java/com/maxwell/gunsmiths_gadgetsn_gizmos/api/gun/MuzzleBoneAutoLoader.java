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




    public static double BASE_HAND_RIGHT = 0.38;

    public static double BASE_HAND_UP = -0.38;

    public static double BASE_HAND_FORWARD = 0.10;

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

                                if ("muzzle".equalsIgnoreCase(name) || "muzzle_flash".equalsIgnoreCase(name)) {
                                    JsonArray pivot = boneObj.getAsJsonArray("pivot");
                                    if (pivot != null && pivot.size() >= 3) {
                                        double px = pivot.get(0).getAsDouble();
                                        double py = pivot.get(1).getAsDouble();
                                        double pz = pivot.get(2).getAsDouble();

                                        double forward = (-pz / 16.0) + BASE_HAND_FORWARD;
                                        double right = BASE_HAND_RIGHT + (px / 16.0);
                                        double up = BASE_HAND_UP + (py / 16.0);
                                        return new MuzzleOffset(forward, right, up);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }

        return MuzzleOffset.DEFAULT;
    }

    public static void clearCache() {
        AUTO_OFFSETS.clear();
    }
}