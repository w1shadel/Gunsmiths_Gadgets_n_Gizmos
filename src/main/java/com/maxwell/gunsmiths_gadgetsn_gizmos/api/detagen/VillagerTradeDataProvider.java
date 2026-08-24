package com.maxwell.gunsmiths_gadgetsn_gizmos.api.detagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class VillagerTradeDataProvider implements DataProvider {
    private final PackOutput output;
    private final String modId;
    private final Map<String, JsonObject> trades = new HashMap<>();
    private final Map<String, JsonObject> tradeSets = new HashMap<>();

    public VillagerTradeDataProvider(PackOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    public VillagerTradeDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        this(output, modId);
    }

    protected void buildTrades() {
        addTrade("iron_to_emerald", "minecraft:iron_ingot", 4, "minecraft:emerald", 1, 16, 2, 0.05F);
        addTrade("copper_to_emerald", "minecraft:copper_ingot", 6, "minecraft:emerald", 1, 16, 2, 0.05F);
        addTrade("charcoal_to_emerald", "minecraft:charcoal", 16, "minecraft:emerald", 1, 16, 2, 0.05F);
        addTrade("emerald_to_bullets", "minecraft:emerald", 1, "irons_artifice:bullet", 16, 12, 1, 0.05F);
        addTrade("emerald_to_blackpowder", "minecraft:emerald", 1, "irons_artifice:blackpowder", 4, 12, 1, 0.05F);
        addTradeSet("gunsmith_level_1", 2, List.of(
                "iron_to_emerald",
                "copper_to_emerald",
                "charcoal_to_emerald",
                "emerald_to_bullets",
                "emerald_to_blackpowder"
        ));
        addTrade("gunpowder_to_emerald", "minecraft:gunpowder", 6, "minecraft:emerald", 1, 16, 5, 0.05F);
        addTrade("redstone_to_emerald", "minecraft:redstone", 12, "minecraft:emerald", 1, 16, 5, 0.05F);
        addTrade("emerald_to_simple_parts", "minecraft:emerald", 3, "irons_artifice:simple_mechanical_components", 1, 8, 5, 0.05F);
        addTrade("emerald_to_gun_oil", "minecraft:emerald", 4, "irons_artifice:gun_oil_modifier", 1, 6, 5, 0.05F);
        addTradeSet("gunsmith_level_2", 2, List.of(
                "gunpowder_to_emerald",
                "redstone_to_emerald",
                "emerald_to_simple_parts",
                "emerald_to_gun_oil"
        ));
        addTrade("gold_to_emerald", "minecraft:gold_ingot", 3, "minecraft:emerald", 1, 16, 10, 0.05F);
        addTrade("blaze_powder_to_emerald", "minecraft:blaze_powder", 4, "minecraft:emerald", 1, 12, 10, 0.05F);
        addTrade("emerald_to_flare_mod", "minecraft:emerald", 5, modId + ":town_bell_flare_modifier", 1, 4, 10, 0.05F);
        addTrade("emerald_to_steel_core", "minecraft:emerald", 6, "irons_artifice:steel_core_modifier", 1, 4, 10, 0.05F);
        addTrade("emerald_to_unidentified_crate", "minecraft:emerald", 4, modId + ":unidentified_crate", 1, 6, 10, 0.05F);
        addTradeSet("gunsmith_level_3", 2, List.of(
                "gold_to_emerald",
                "blaze_powder_to_emerald",
                "emerald_to_flare_mod",
                "emerald_to_steel_core",
                "emerald_to_unidentified_crate"
        ));
        addTrade("amethyst_to_emerald", "minecraft:amethyst_shard", 5, "minecraft:emerald", 1, 12, 15, 0.05F);
        addTrade("breeze_rod_to_emerald", "minecraft:breeze_rod", 2, "minecraft:emerald", 3, 8, 15, 0.05F);
        addTrade("emerald_to_piston_ramrod", "minecraft:emerald", 7, modId + ":piston_ramrod_modifier", 1, 3, 15, 0.05F);
        addTrade("emerald_to_breeze_cyclone", "minecraft:emerald", 8, modId + ":breeze_cyclone_modifier", 1, 3, 15, 0.05F);
        addTrade("emerald_to_merchant_bounty", "minecraft:emerald", 8, modId + ":merchant_bounty_modifier", 1, 3, 15, 0.05F);
        addTradeSet("gunsmith_level_4", 2, List.of(
                "amethyst_to_emerald",
                "breeze_rod_to_emerald",
                "emerald_to_piston_ramrod",
                "emerald_to_breeze_cyclone",
                "emerald_to_merchant_bounty"
        ));
        addTrade("netherite_scrap_to_emerald", "minecraft:netherite_scrap", 1, "minecraft:emerald", 10, 4, 30, 0.05F);
        addTrade("emerald_to_six_shooter", "minecraft:emerald", 18, "irons_artifice:six_shooter", 1, 2, 30, 0.05F);
        addTrade("emerald_to_clockwork_parts", "minecraft:emerald", 10, "irons_artifice:clockwork_components", 1, 4, 30, 0.05F);
        addTrade("emerald_to_heavy_core_mod", "minecraft:emerald", 14, "minecraft:iron_block", 1, modId + ":heavy_core_impact_modifier", 1, 2, 30, 0.05F);
        addTrade("emerald_to_master_trigger", "minecraft:emerald", 16, modId + ":mastercrafted_trigger_modifier", 1, 2, 30, 0.05F);
        addTradeSet("gunsmith_level_5", 2, List.of(
                "netherite_scrap_to_emerald",
                "emerald_to_six_shooter",
                "emerald_to_clockwork_parts",
                "emerald_to_heavy_core_mod",
                "emerald_to_master_trigger"
        ));
        addTrade("rotten_flesh_to_emerald", "minecraft:rotten_flesh", 24, "minecraft:emerald", 1, 16, 2, 0.05F);
        addTrade("soul_sand_to_cinder", "minecraft:soul_sand", 8, modId + ":soul_cinder", 2, 12, 2, 0.05F);
        addTrade("emerald_to_void_casing", "minecraft:emerald", 2, modId + ":void_casing", 1, 12, 1, 0.05F);
        addTradeSet("cultist_level_1", 2, List.of(
                "rotten_flesh_to_emerald",
                "soul_sand_to_cinder",
                "emerald_to_void_casing"
        ));
        addTrade("glass_bottle_to_emerald", "minecraft:glass_bottle", 8, "minecraft:emerald", 1, 16, 5, 0.05F);
        addTrade("cinder_and_gold_to_brass", modId + ":soul_cinder", 2, "minecraft:gold_ingot", 2, modId + ":cursed_brass_ingot", 1, 8, 5, 0.05F);
        addTrade("emerald_to_bloodbound_mod", "minecraft:emerald", 6, modId + ":bloodbound_calamity_modifier", 1, 4, 5, 0.05F);
        addTradeSet("cultist_level_2", 2, List.of(
                "glass_bottle_to_emerald",
                "cinder_and_gold_to_brass",
                "emerald_to_bloodbound_mod"
        ));
        addTrade("ominous_bottle_to_blood", "minecraft:ominous_bottle", 1, modId + ":coagulated_omen_blood", 1, 6, 10, 0.05F);
        addTrade("emerald_to_ominous_core", "minecraft:emerald", 10, modId + ":ominous_clockwork_core", 1, 4, 10, 0.05F);
        addTrade("emerald_to_unstable_overclock", "minecraft:emerald", 8, modId + ":unstable_overclock_modifier", 1, 3, 10, 0.05F);
        addTradeSet("cultist_level_3", 2, List.of(
                "ominous_bottle_to_blood",
                "emerald_to_ominous_core",
                "emerald_to_unstable_overclock"
        ));
        addTrade("wither_skull_to_emerald", "minecraft:wither_skeleton_skull", 1, "minecraft:emerald", 8, 4, 15, 0.05F);
        addTrade("emerald_to_forbidden_blueprint", "minecraft:emerald", 12, modId + ":forbidden_blueprint", 1, 3, 15, 0.05F);
        addTrade("emerald_to_reapers_gambit", "minecraft:emerald", 10, modId + ":reapers_gambit_modifier", 1, 2, 15, 0.05F);
        addTradeSet("cultist_level_4", 2, List.of(
                "wither_skull_to_emerald",
                "emerald_to_forbidden_blueprint",
                "emerald_to_reapers_gambit"
        ));
        addTrade("emerald_to_trial_of_greed", "minecraft:emerald", 20, modId + ":coagulated_omen_blood", 2, modId + ":trial_of_greed_modifier", 1, 2, 30, 0.05F);
        addTrade("nether_star_to_omen_cores", "minecraft:nether_star", 1, modId + ":ominous_clockwork_core", 4, 2, 30, 0.05F);
        addTradeSet("cultist_level_5", 2, List.of(
                "emerald_to_trial_of_greed",
                "nether_star_to_omen_cores"
        ));
    }

    public void addTrade(String tradeName, String wantsItemId, int wantsCount, String givesItemId, int givesCount, int maxUses, int xp, float reputationDiscount) {
        addTrade(tradeName, wantsItemId, wantsCount, null, 0, givesItemId, givesCount, maxUses, xp, reputationDiscount);
    }

    public void addTrade(String tradeName, String wantsItemId1, int wantsCount1, @Nullable String wantsItemId2, int wantsCount2, String givesItemId, int givesCount, int maxUses, int xp, float reputationDiscount) {
        JsonObject json = new JsonObject();
        JsonObject wants = new JsonObject();
        wants.addProperty("id", wantsItemId1);
        wants.addProperty("count", wantsCount1);
        json.add("wants", wants);
        if (wantsItemId2 != null && wantsCount2 > 0) {
            JsonObject secondWants = new JsonObject();
            secondWants.addProperty("id", wantsItemId2);
            secondWants.addProperty("count", wantsCount2);
            json.add("second_wants", secondWants);
        }
        JsonObject gives = new JsonObject();
        gives.addProperty("id", givesItemId);
        gives.addProperty("count", givesCount);
        json.add("gives", gives);
        json.addProperty("max_uses", maxUses);
        json.addProperty("xp", xp);
        json.addProperty("reputation_discount", reputationDiscount);
        this.trades.put(tradeName, json);
    }

    public void addTradeSet(String setName, int amount, List<String> tradeNames) {
        JsonObject json = new JsonObject();
        json.addProperty("amount", amount);
        JsonArray array = new JsonArray();
        for (String trade : tradeNames) {
            String fullId = trade.contains(":") ? trade : (this.modId + ":" + trade);
            array.add(fullId);
        }
        json.add("trades", array);
        this.tradeSets.put(setName, json);
    }

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput output) {
        this.trades.clear();
        this.tradeSets.clear();
        buildTrades();
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (Map.Entry<String, JsonObject> entry : trades.entrySet()) {
            Path path = this.output.getOutputFolder(PackOutput.Target.DATA_PACK)
                    .resolve(this.modId)
                    .resolve("villager_trade")
                    .resolve(entry.getKey() + ".json");
            futures.add(DataProvider.saveStable(output, entry.getValue(), path));
        }
        for (Map.Entry<String, JsonObject> entry : tradeSets.entrySet()) {
            Path path = this.output.getOutputFolder(PackOutput.Target.DATA_PACK)
                    .resolve(this.modId)
                    .resolve("trade_set")
                    .resolve(entry.getKey() + ".json");
            futures.add(DataProvider.saveStable(output, entry.getValue(), path));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public @NonNull String getName() {
        return "Villager Trades & Trade Sets: " + this.modId;
    }
}