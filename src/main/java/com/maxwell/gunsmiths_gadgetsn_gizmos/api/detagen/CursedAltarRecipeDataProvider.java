package com.maxwell.gunsmiths_gadgetsn_gizmos.api.detagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModItems;
import io.redspace.irons_artifice.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CursedAltarRecipeDataProvider implements DataProvider {
    private final PackOutput output;
    private final String modId;
    private final Map<String, JsonObject> recipes = new HashMap<>();

    public CursedAltarRecipeDataProvider(PackOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    public CursedAltarRecipeDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        this(output, modId);
    }

    protected void buildRecipes() {
        addRecipe("altar_crimson_singularity",
                ModItems.HEAVY_CORE_IMPACT_MODIFIER.get(),
                ModItems.BLOODBOUND_CALAMITY_MODIFIER.get(), 1,
                ModItems.ABYSSAL_SINGULARITY_CORE.get(), 1,
                ModItems.CRIMSON_SINGULARITY_MODIFIER.get(), 1
        );
        addRecipe("altar_ominous_chamber",
                ItemRegistry.OVERCHARGED_POWDER.get(),
                ModItems.COAGULATED_OMEN_BLOOD.get(), 2,
                ModItems.OMINOUS_CLOCKWORK_CORE.get(), 1,
                ModItems.OMINOUS_CHAMBER_MODIFIER.get(), 1
        );
        addRecipe("altar_reapers_gambit",
                ModItems.VOID_CASING.get(),
                Items.WITHER_SKELETON_SKULL, 1,
                ModItems.SOUL_CINDER.get(), 4,
                ModItems.REAPERS_GAMBIT_MODIFIER.get(), 1
        );
        addRecipe("altar_trial_of_greed",
                ModItems.MERCHANT_BOUNTY_MODIFIER.get(),
                Items.EMERALD_BLOCK, 2,
                ModItems.COAGULATED_OMEN_BLOOD.get(), 2,
                ModItems.TRIAL_OF_GREED_MODIFIER.get(), 1
        );
        addRecipe("altar_clockwork_gatling",
                ModItems.MASTERCRAFTED_TRIGGER_MODIFIER.get(),
                ModItems.UNSTABLE_OVERCLOCK_MODIFIER.get(), 1,
                ModItems.OMINOUS_CLOCKWORK_CORE.get(), 1,
                ModItems.CLOCKWORK_GATLING_MODIFIER.get(), 1
        );
        addRecipe("altar_reapers_tempest",
                ModItems.BREEZE_CYCLONE_MODIFIER.get(),
                ModItems.REAPERS_GAMBIT_MODIFIER.get(), 1,
                ModItems.OMINOUS_CLOCKWORK_CORE.get(), 1,
                ModItems.REAPERS_TEMPEST_MODIFIER.get(), 1
        );
        addRecipe("altar_midas_touch_chamber",
                ModItems.MERCHANT_BOUNTY_MODIFIER.get(),
                ModItems.TRIAL_OF_GREED_MODIFIER.get(), 1,
                ModItems.OMINOUS_CLOCKWORK_CORE.get(), 1,
                ModItems.MIDAS_TOUCH_CHAMBER_MODIFIER.get(), 1
        );
        addRecipe("altar_craft_unstable_overclock",
                ModItems.FORBIDDEN_BLUEPRINT.get(),
                ModItems.CURSED_BRASS_INGOT.get(), 2,
                ModItems.SOUL_CINDER.get(), 4,
                ModItems.UNSTABLE_OVERCLOCK_MODIFIER.get(), 1
        );
        addRecipe("altar_craft_bloodbound_calamity",
                ModItems.PISTON_RAMROD_MODIFIER.get(),
                ModItems.VOID_CASING.get(), 1,
                ModItems.COAGULATED_OMEN_BLOOD.get(), 2,
                ModItems.BLOODBOUND_CALAMITY_MODIFIER.get(), 1
        );
        addRecipe("altar_echoing_sonic_core",
                ModItems.VOID_CASING.get(),
                Items.ECHO_SHARD, 2,
                Blocks.SCULK_CATALYST, 1,
                ModItems.ECHOING_SONIC_CORE_MODIFIER.get(), 1
        );
        addRecipe("altar_sculk_whisper_silencer",
                ModItems.CURSED_BRASS_INGOT.get(),
                Blocks.SCULK_SENSOR, 1,
                Items.BLACK_WOOL, 4,
                ModItems.SCULK_WHISPER_SILENCER_MODIFIER.get(), 1
        );
        addRecipe("altar_sculk_devourer",
                ItemRegistry.CLOCKWORK_COMPONENTS.get(),
                Blocks.SCULK_CATALYST, 1,
                ModItems.COAGULATED_OMEN_BLOOD.get(), 2,
                ModItems.SCULK_DEVOURER_MODIFIER.get(), 1
        );
        addRecipe("altar_shrieking_dread",
                ModItems.VOID_CASING.get(),
                Blocks.SCULK_SHRIEKER, 1,
                ModItems.SOUL_CINDER.get(), 4,
                ModItems.SHRIEKING_DREAD_MODIFIER.get(), 1
        );
        addRecipe("altar_summon_apostle",
                ModItems.FORBIDDEN_BLUEPRINT.get(),
                ModItems.COAGULATED_OMEN_BLOOD.get(), 2,
                Items.ROTTEN_FLESH, 1,
                ModItems.APOSTLE_SUMMON_RITUAL.get(), 1
        );
    }

    public void addRecipe(String recipeName, ItemLike base, ItemLike material, int materialCount, ItemLike catalyst, int catalystCount, ItemLike result, int resultCount) {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.modId + ":cursed_altar");
        JsonArray baseArr = new JsonArray();
        baseArr.add(BuiltInRegistries.ITEM.getKey(base.asItem()).toString());
        json.add("base", baseArr);
        JsonObject matJson = new JsonObject();
        JsonArray matArr = new JsonArray();
        matArr.add(BuiltInRegistries.ITEM.getKey(material.asItem()).toString());
        matJson.add("ingredient", matArr);
        matJson.addProperty("count", materialCount);
        json.add("material", matJson);
        JsonObject catJson = new JsonObject();
        JsonArray catArr = new JsonArray();
        catArr.add(BuiltInRegistries.ITEM.getKey(catalyst.asItem()).toString());
        catJson.add("ingredient", catArr);
        catJson.addProperty("count", catalystCount);
        json.add("catalyst", catJson);
        JsonObject resJson = new JsonObject();
        resJson.addProperty("id", BuiltInRegistries.ITEM.getKey(result.asItem()).toString());
        resJson.addProperty("count", resultCount);
        json.add("result", resJson);
        this.recipes.put(recipeName, json);
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
        return "Cursed Altar Recipes: " + this.modId;
    }
}