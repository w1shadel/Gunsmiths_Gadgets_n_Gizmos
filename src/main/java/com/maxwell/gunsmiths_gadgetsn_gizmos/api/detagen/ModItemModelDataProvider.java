package com.maxwell.gunsmiths_gadgetsn_gizmos.api.detagen;

import com.google.gson.JsonObject;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModItemModelDataProvider implements DataProvider {
    private final PackOutput output;
    private final String modId;

    public ModItemModelDataProvider(PackOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    public ModItemModelDataProvider(PackOutput output, CompletableFuture<?> lookup, String modId) {
        this(output, modId);
    }

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput output) {
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (Holder<Item> holder : ModItems.ITEMS.getEntries()) {
            Item item = holder.value();
            Identifier itemId = BuiltInRegistries.ITEM.getKey(item);
            if (itemId.getPath().equals("clunker_rifle")) continue;
            if (itemId.getPath().equals("minigun")) continue;
            if (itemId.getPath().equals("gunsmith_bench")) continue;
            if (itemId.getPath().equals("cursed_altar")) continue;
            JsonObject modelJson = new JsonObject();
            modelJson.addProperty("parent", "minecraft:item/generated");
            JsonObject textures = new JsonObject();
            textures.addProperty("layer0", this.modId + ":item/" + itemId.getPath());
            modelJson.add("textures", textures);
            Path modelPath = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                    .resolve(this.modId)
                    .resolve("models")
                    .resolve("item")
                    .resolve(itemId.getPath() + ".json");
            futures.add(DataProvider.saveStable(output, modelJson, modelPath));
            JsonObject itemJson = new JsonObject();
            JsonObject modelObj = new JsonObject();
            modelObj.addProperty("type", "minecraft:model");
            modelObj.addProperty("model", this.modId + ":item/" + itemId.getPath());
            itemJson.add("model", modelObj);
            Path itemDefPath = this.output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                    .resolve(this.modId)
                    .resolve("items")
                    .resolve(itemId.getPath() + ".json");
            futures.add(DataProvider.saveStable(output, itemJson, itemDefPath));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public @NonNull String getName() {
        return "Item Models: " + this.modId;
    }
}