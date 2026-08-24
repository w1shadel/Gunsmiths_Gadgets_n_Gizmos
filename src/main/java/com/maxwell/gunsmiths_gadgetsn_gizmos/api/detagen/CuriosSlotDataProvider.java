package com.maxwell.gunsmiths_gadgetsn_gizmos.api.detagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CuriosSlotDataProvider implements DataProvider {
    private static final List<String> SLOTS_TO_ENABLE = List.of(
            "head",
            "body",
            "back",
            "belt",
            "hands",
            "ring",
            "feet",
            "charm",
            "curio"
    );
    private final PackOutput output;

    public CuriosSlotDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        this.output = output;
    }

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput output) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        JsonObject entityJson = new JsonObject();
        JsonArray entitiesArray = new JsonArray();
        entitiesArray.add("minecraft:player");
        entityJson.add("entities", entitiesArray);
        JsonArray slotsArray = new JsonArray();
        for (String slot : SLOTS_TO_ENABLE) {
            slotsArray.add(slot);
        }
        entityJson.add("slots", slotsArray);
        Path entityPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK)
                .resolve("curios")
                .resolve("curios")
                .resolve("entities")
                .resolve("default.json");
        futures.add(DataProvider.saveStable(output, entityJson, entityPath));
        int order = 100;
        for (String slotName : SLOTS_TO_ENABLE) {
            JsonObject slotJson = new JsonObject();
            slotJson.addProperty("order", order += 10);
            slotJson.addProperty("size", 1);
            Path slotPath = this.output.getOutputFolder(PackOutput.Target.DATA_PACK)
                    .resolve("curios")
                    .resolve("curios")
                    .resolve("slots")
                    .resolve(slotName + ".json");
            futures.add(DataProvider.saveStable(output, slotJson, slotPath));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public @NonNull String getName() {
        return "Curios Slots & Entity Settings: gunsmiths_gadgetsn_gizmos";
    }
}