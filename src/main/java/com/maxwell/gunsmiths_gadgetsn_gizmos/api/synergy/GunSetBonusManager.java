package com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import io.redspace.irons_artifice.menu.GunContainer;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GunSetBonusManager extends SimplePreparableReloadListener<Map<Identifier, GunSetBonus>> {
    private static final FileToIdConverter CONVERTER = FileToIdConverter.json("gun_set_bonuses");
    private static final Map<Identifier, GunSetBonus> ACTIVE_BONUSES = new HashMap<>();

    
    public static List<GunSetBonus> getMatchingBonuses(ItemStack gun) {
        List<GunSetBonus> matched = new ArrayList<>();
        GunContainer container = new GunContainer(gun);
        List<ItemStack> installedModifiers = container.getItems();
        for (GunSetBonus bonus : ACTIVE_BONUSES.values()) {
            if (bonus.matches(installedModifiers)) {
                matched.add(bonus);
            }
        }
        return matched;
    }
    public static java.util.Collection<GunSetBonus> getAllBonuses() {
        return ACTIVE_BONUSES.values();
    }
    @Override
    protected @NonNull Map<Identifier, GunSetBonus> prepare(@NonNull ResourceManager resourceManager, @NonNull ProfilerFiller profiler) {
        Map<Identifier, GunSetBonus> map = new HashMap<>();
        for (var entry : CONVERTER.listMatchingResources(resourceManager).entrySet()) {
            Identifier fullPath = entry.getKey();
            Identifier id = CONVERTER.fileToId(fullPath);
            try (Reader reader = entry.getValue().openAsReader()) {
                JsonElement json = JsonParser.parseReader(reader);
                DataResult<GunSetBonus> result = GunSetBonus.CODEC.parse(JsonOps.INSTANCE, json);
                result.ifSuccess(bonus -> map.put(id, bonus));
                result.ifError(err -> System.err.println("[Gunsmiths SetBonus] Error parsing " + id + ": " + err.message()));
            } catch (Exception e) {
                System.err.println("[Gunsmiths SetBonus] Failed to read JSON file: " + id);
                e.printStackTrace();
            }
        }
        return map;
    }

    @Override
    protected void apply(@NonNull Map<Identifier, GunSetBonus> map, @NonNull ResourceManager resourceManager, @NonNull ProfilerFiller profiler) {
        ACTIVE_BONUSES.clear();
        ACTIVE_BONUSES.putAll(map);
        System.out.println("[Gunsmiths SetBonus] Successfully Loaded " + ACTIVE_BONUSES.size() + " Gun Set Bonuses from datapacks!");
    }
}