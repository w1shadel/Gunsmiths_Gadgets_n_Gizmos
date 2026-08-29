package com.maxwell.gunsmiths_gadgetsn_gizmos.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class CursedAltarRecipeManager extends SimplePreparableReloadListener<Map<Identifier, CursedAltarRecipe>> {
    private static final FileToIdConverter RECIPE_CONVERTER = FileToIdConverter.json("recipe");
    private static final FileToIdConverter RECIPES_CONVERTER = FileToIdConverter.json("recipes");
    private static final Map<Identifier, CursedAltarRecipe> RECIPES = new LinkedHashMap<>();

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
    protected @NonNull Map<Identifier, CursedAltarRecipe> prepare(@NonNull ResourceManager resourceManager, @NonNull ProfilerFiller profiler) {
        Map<Identifier, CursedAltarRecipe> map = new LinkedHashMap<>();
        scanResources(resourceManager, RECIPE_CONVERTER, map);
        scanResources(resourceManager, RECIPES_CONVERTER, map);
        scanGeneratedFolder(map);
        return map;
    }

    private void scanResources(ResourceManager resourceManager, FileToIdConverter converter, Map<Identifier, CursedAltarRecipe> map) {
        for (var entry : converter.listMatchingResources(resourceManager).entrySet()) {
            Identifier fullPath = entry.getKey();
            Identifier recipeId = converter.fileToId(fullPath);
            try (Reader reader = entry.getValue().openAsReader()) {
                parseAndAdd(recipeId, reader, map);
            } catch (Exception e) {
                GunsmithsGadgetsnGizmos.LOGGER.error("[CursedAltar] Error parsing recipe from {}: {}", fullPath, e.getMessage());
            }
        }
    }

    private void scanGeneratedFolder(Map<Identifier, CursedAltarRecipe> map) {
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
                            .filter(p -> {
                                String normalized = p.toString().replace('\\', '/');
                                return normalized.contains("/recipe/") || normalized.contains("/recipes/");
                            })
                            .forEach(path -> {
                                try (Reader reader = Files.newBufferedReader(path)) {
                                    Path rel = root.relativize(path);
                                    String namespace = rel.getName(0).toString();
                                    String fileName = path.getFileName().toString().replace(".json", "");
                                    Identifier recipeId = Identifier.fromNamespaceAndPath(namespace, fileName);
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

    private void parseAndAdd(Identifier recipeId, Reader reader, Map<Identifier, CursedAltarRecipe> map) {
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
        return typeStr.equals(GunsmithsGadgetsnGizmos.MODID + ":cursed_altar") || typeStr.endsWith(":cursed_altar");
    }

    @Override
    protected void apply(@NonNull Map<Identifier, CursedAltarRecipe> map, @NonNull ResourceManager resourceManager, @NonNull ProfilerFiller profiler) {
        RECIPES.clear();
        RECIPES.putAll(map);
        GunsmithsGadgetsnGizmos.LOGGER.info("[CursedAltar] Successfully loaded {} Altar Recipes (from datapacks, other mods & generated resources)!", RECIPES.size());
    }
}