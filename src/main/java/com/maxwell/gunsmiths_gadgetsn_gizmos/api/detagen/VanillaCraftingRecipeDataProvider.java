package com.maxwell.gunsmiths_gadgetsn_gizmos.api.detagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.maxwell.gunsmiths_gadgetsn_gizmos.registry.ModBlocks;
import com.maxwell.gunsmiths_gadgetsn_gizmos.registry.ModItems;
import io.redspace.irons_artifice.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class VanillaCraftingRecipeDataProvider implements DataProvider {
    private final PackOutput output;
    private final String modId;
    private final Map<String, JsonObject> recipes = new HashMap<>();

    public VanillaCraftingRecipeDataProvider(PackOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    public VanillaCraftingRecipeDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup, String modId) {
        this(output, modId);
    }

    protected void buildRecipes() {
        // 1. ガンスミス作業台: 鉄ブロック + 木材 + 簡易機械部品
        addShapedRecipe("craft_gunsmith_bench",
                List.of(" I ", "MMM", "W W"),
                Map.of(
                        'I', "minecraft:iron_block",
                        'M', "irons_artifice:simple_mechanical_components",
                        'W', "minecraft:oak_planks"
                ),
                ModBlocks.GUNSMITH_BENCH.get(), 1
        );

        // 2. 銃工のシャーシ拡張フレーム: 機械部品 x4 + 鉄ブロック x2 + 銅インゴット x3
        addShapedRecipe("craft_gunsmith_chassis_kit",
                List.of("IMI", "CCC", "IMI"),
                Map.of(
                        'I', "minecraft:iron_block",
                        'M', "irons_artifice:mechanical_components",
                        'C', "minecraft:copper_ingot"
                ),
                ModItems.GUNSMITH_CHASSIS_KIT.get(), 1
        );

        // 3. 虚無の空薬莢: 泣く黒曜石 + 鉄インゴット + 黒火薬 (4個作成)
        addShapedRecipe("craft_void_casing",
                List.of(" I ", "IBI", " O "),
                Map.of(
                        'I', "minecraft:iron_ingot",
                        'B', "irons_artifice:blackpowder",
                        'O', "minecraft:crying_obsidian"
                ),
                ModItems.VOID_CASING.get(), 4
        );

        // 4. 弾薬ポーチ: 革 x5 + 鉄インゴット + 簡易機械部品
        addShapedRecipe("craft_ammo_pouch",
                List.of("LLL", "LML", " I "),
                Map.of(
                        'L', "minecraft:leather",
                        'M', "irons_artifice:simple_mechanical_components",
                        'I', "minecraft:iron_ingot"
                ),
                ModItems.AMMO_POUCH.get(), 1
        );

        // 5. 銀の弾丸: 鉄 + 金 + 黒火薬 (16発作成)
        addShapedRecipe("craft_silver_bullet",
                List.of(" G ", " I ", " B "),
                Map.of(
                        'G', "minecraft:gold_ingot",
                        'I', "minecraft:iron_ingot",
                        'B', "irons_artifice:blackpowder"
                ),
                ModItems.SILVER_BULLET.get(), 16
        );

        // 6. 徹甲弾 (AP弾): 銅 + 鉄 + 呪印の真鍮 (16発作成)
        addShapedRecipe("craft_ap_bullet",
                List.of(" C ", " I ", " B "),
                Map.of(
                        'C', this.modId + ":cursed_brass_ingot",
                        'I', "minecraft:iron_ingot",
                        'B', "irons_artifice:blackpowder"
                ),
                ModItems.AP_BULLET.get(), 16
        );

        // 7. 魂の燃え殻 (かまど製錬): ソウルサンド/ソウルソイルを焼く -> 魂の燃え殻
        addSmeltingRecipe("smelt_soul_sand_to_cinder", "minecraft:soul_sand", ModItems.SOUL_CINDER.get(), 0.2F, 200);
        addSmeltingRecipe("smelt_soul_soil_to_cinder", "minecraft:soul_soil", ModItems.SOUL_CINDER.get(), 0.2F, 200);
    }

    // --- レシピ構築ヘルパー ---
    public void addShapedRecipe(String name, List<String> pattern, Map<Character, String> key, ItemLike result, int count) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:crafting_shaped");
        json.addProperty("category", "misc");

        JsonArray patternArr = new JsonArray();
        pattern.forEach(patternArr::add);
        json.add("pattern", patternArr);

        JsonObject keyObj = new JsonObject();
        key.forEach((k, v) -> {
            JsonObject ing = new JsonObject();
            ing.addProperty("item", v);
            keyObj.add(String.valueOf(k), ing);
        });
        json.add("key", keyObj);

        JsonObject resObj = new JsonObject();
        resObj.addProperty("id", BuiltInRegistries.ITEM.getKey(result.asItem()).toString());
        resObj.addProperty("count", count);
        json.add("result", resObj);

        this.recipes.put(name, json);
    }

    public void addSmeltingRecipe(String name, String ingredientItem, ItemLike result, float xp, int cookingTime) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:smelting");
        json.addProperty("category", "misc");

        JsonObject ing = new JsonObject();
        ing.addProperty("item", ingredientItem);
        json.add("ingredient", ing);

        json.addProperty("result", BuiltInRegistries.ITEM.getKey(result.asItem()).toString());
        json.addProperty("experience", xp);
        json.addProperty("cookingtime", cookingTime);

        this.recipes.put(name, json);
    }

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput output) {
        this.recipes.clear();
        buildRecipes();

        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (Map.Entry<String, JsonObject> entry : recipes.entrySet()) {
            Path path = this.output.getOutputFolder(PackOutput.Target.DATA_PACK)
                    .resolve(this.modId)
                    .resolve("recipe")
                    .resolve(entry.getKey() + ".json");
            futures.add(DataProvider.saveStable(output, entry.getValue(), path));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public @NonNull String getName() {
        return "Vanilla Crafting Recipes: " + this.modId;
    }
}