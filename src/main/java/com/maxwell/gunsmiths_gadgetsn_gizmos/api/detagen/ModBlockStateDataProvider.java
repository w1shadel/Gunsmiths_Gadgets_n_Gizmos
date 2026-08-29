package com.maxwell.gunsmiths_gadgetsn_gizmos.api.detagen;

import com.google.gson.JsonObject;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModBlockStateDataProvider implements DataProvider {
    private final PackOutput output;
    private final String modId;

    public ModBlockStateDataProvider(PackOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    public ModBlockStateDataProvider(PackOutput output, CompletableFuture<?> lookup, String modId) {
        this(output, modId);
    }

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput output) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        addOrientableBlock("gunsmith_bench", output, futures);
        addOrientableBlock("cursed_altar", output, futures);
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    private void addOrientableBlock(String name, CachedOutput output, List<CompletableFuture<?>> futures) {
        JsonObject modelJson = new JsonObject();
        modelJson.addProperty("parent", "minecraft:block/orientable_with_bottom");
        JsonObject textures = new JsonObject();
        textures.addProperty("particle", this.modId + ":block/" + name + "_front");
        textures.addProperty("bottom", this.modId + ":block/" + name + "_bottom");
        textures.addProperty("top", this.modId + ":block/" + name + "_top");
        textures.addProperty("side", this.modId + ":block/" + name + "_side");
        textures.addProperty("front", this.modId + ":block/" + name + "_front");
        modelJson.add("textures", textures);
        Path modelPath = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(this.modId).resolve("models").resolve("block").resolve(name + ".json");
        futures.add(DataProvider.saveStable(output, modelJson, modelPath));
        JsonObject stateJson = new JsonObject();
        JsonObject variants = new JsonObject();
        JsonObject north = new JsonObject();
        north.addProperty("model", this.modId + ":block/" + name);
        variants.add("facing=north", north);
        JsonObject east = new JsonObject();
        east.addProperty("model", this.modId + ":block/" + name);
        east.addProperty("y", 90);
        variants.add("facing=east", east);
        JsonObject south = new JsonObject();
        south.addProperty("model", this.modId + ":block/" + name);
        south.addProperty("y", 180);
        variants.add("facing=south", south);
        JsonObject west = new JsonObject();
        west.addProperty("model", this.modId + ":block/" + name);
        west.addProperty("y", 270);
        variants.add("facing=west", west);
        stateJson.add("variants", variants);
        Path statePath = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(this.modId).resolve("blockstates").resolve(name + ".json");
        futures.add(DataProvider.saveStable(output, stateJson, statePath));
        JsonObject itemModelJson = new JsonObject();
        itemModelJson.addProperty("parent", this.modId + ":block/" + name);
        Path itemModelPath = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(this.modId).resolve("models").resolve("item").resolve(name + ".json");
        futures.add(DataProvider.saveStable(output, itemModelJson, itemModelPath));
        JsonObject itemDefJson = new JsonObject();
        JsonObject modelObj = new JsonObject();
        modelObj.addProperty("type", "minecraft:model");
        modelObj.addProperty("model", this.modId + ":item/" + name);
        itemDefJson.add("model", modelObj);
        Path itemDefPath = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve(this.modId).resolve("items").resolve(name + ".json");
        futures.add(DataProvider.saveStable(output, itemDefJson, itemDefPath));
    }

    @Override
    public @NonNull String getName() {
        return "BlockStates & Block Models: " + this.modId;
    }
}