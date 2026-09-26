package com.maxwell.gunsmiths_gadgetsn_gizmos.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class CursedAltarRecipeManager extends SimplePreparableReloadListener<Map<ResourceLocation, CursedAltarRecipe>> {
    private static final List<FileToIdConverter> CONVERTERS = List.of(
            FileToIdConverter.json("recipe"),
            FileToIdConverter.json("recipes"),
            FileToIdConverter.json("cursed_altar"),
            FileToIdConverter.json("cursed_altar_recipes")
    );
    private static final Map<ResourceLocation, CursedAltarRecipe> RECIPES = new LinkedHashMap<>();

    public static Optional<CursedAltarRecipe> findMatchingRecipe(ItemStack base, ItemStack material, ItemStack catalyst) {
        for (CursedAltarRecipe recipe : RECIPES.values()) {
            if (recipe.matches(base, material, catalyst)) {
                return Optional.of(recipe);
            }
        }
        return Optional.empty();
    }

    public static Collection<CursedAltarRecipe> getAllRecipes() {
        return RECIPES.values();
    }

    @Override
    protected @NonNull Map<ResourceLocation, CursedAltarRecipe> prepare(@NonNull ResourceManager resourceManager, @NonNull ProfilerFiller profiler) {
        Map<ResourceLocation, CursedAltarRecipe> map = new LinkedHashMap<>();
        for (FileToIdConverter converter : CONVERTERS) {
            scanResources(resourceManager, converter, map);
        }
        scanGeneratedFolder(map);
        return map;
    }

    private void scanResources(ResourceManager resourceManager, FileToIdConverter converter, Map<ResourceLocation, CursedAltarRecipe> map) {
        for (var entry : converter.listMatchingResources(resourceManager).entrySet()) {
            ResourceLocation fullPath = entry.getKey();
            ResourceLocation recipeId = converter.fileToId(fullPath);
            try (Reader reader = entry.getValue().openAsReader()) {
                parseAndAdd(recipeId, reader, map);
            } catch (Exception e) {
                GunsmithsGadgetsnGizmos.LOGGER.error("[CursedAltar] Error parsing recipe from {}: {}", fullPath, e.getMessage());
            }
        }
    }

    private void scanGeneratedFolder(Map<ResourceLocation, CursedAltarRecipe> map) {
        List<Path> potentialRoots = List.of(
                Path.of("src/generated/resources/data"),
                Path.of("../src/generated/resources/data"),
                Path.of("../../src/generated/resources/data")
        );
        for (Path root : potentialRoots) {
            if (Files.exists(root) && Files.isDirectory(root)) {
                try (Stream<Path> stream = Files.walk(root)) {
                    stream.filter(Files::isRegularFile)
                            .filter(p -> p.toString().endsWith(".json"))
                            .forEach(path -> {
                                try (Reader reader = Files.newBufferedReader(path)) {
                                    Path rel = root.relativize(path);
                                    String namespace = rel.getName(0).toString();
                                    String fileName = path.getFileName().toString().replace(".json", "");
                                    ResourceLocation recipeId = ResourceLocation.fromNamespaceAndPath(namespace, fileName);
                                    parseAndAdd(recipeId, reader, map);
                                } catch (Exception e) {
                                    GunsmithsGadgetsnGizmos.LOGGER.error("[CursedAltar] Error reading generated JSON at {}: {}", path, e.getMessage());
                                }
                            });
                } catch (Exception ignored) {
                }
            }
        }
    }

    private void parseAndAdd(ResourceLocation recipeId, Reader reader, Map<ResourceLocation, CursedAltarRecipe> map) {
        JsonElement element = JsonParser.parseReader(reader);
        if (element.isJsonObject()) {
            JsonObject json = element.getAsJsonObject();
            if (json.has("type") && isCursedAltarType(json.get("type").getAsString())) {
                CursedAltarRecipe recipe = CursedAltarRecipe.fromJson(recipeId, json);
                map.put(recipeId, recipe);
            }
        }
    }

    private boolean isCursedAltarType(String typeStr) {
        return typeStr.equals(GunsmithsGadgetsnGizmos.MODID + ":cursed_altar")
                || typeStr.equals(GunsmithsGadgetsnGizmos.MODID + ":cursed_altar_recipe")
                || typeStr.endsWith(":cursed_altar")
                || typeStr.endsWith(":cursed_altar_recipe");
    }

    @Override
    protected void apply(@NonNull Map<ResourceLocation, CursedAltarRecipe> map, @NonNull ResourceManager resourceManager, @NonNull ProfilerFiller profiler) {
        RECIPES.clear();
        RECIPES.putAll(map);
        GunsmithsGadgetsnGizmos.LOGGER.info("[CursedAltar] Successfully loaded {} Altar Recipes!", RECIPES.size());
    }
}