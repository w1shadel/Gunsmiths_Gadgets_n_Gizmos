package com.maxwell.gunsmiths_gadgetsn_gizmos.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

public class CursedAltarRecipe {
    private final Identifier id;
    private final List<String> baseRawList;
    private final List<String> materialRawList;
    private final int materialCount;
    private final List<String> catalystRawList;
    private final int catalystCount;
    private final String resultItemId;
    private final int resultCount;
    private List<Item> resolvedBaseItems = null;
    private List<Item> resolvedMaterialItems = null;
    private List<Item> resolvedCatalystItems = null;

    public CursedAltarRecipe(Identifier id, List<String> baseRawList, List<String> materialRawList, int materialCount, List<String> catalystRawList, int catalystCount, String resultItemId, int resultCount) {
        this.id = id;
        this.baseRawList = baseRawList;
        this.materialRawList = materialRawList;
        this.materialCount = materialCount;
        this.catalystRawList = catalystRawList;
        this.catalystCount = catalystCount;
        this.resultItemId = resultItemId;
        this.resultCount = resultCount;
    }

    public static CursedAltarRecipe fromJson(Identifier id, JsonObject json) {
        List<String> baseList = parseRawList(json.get("base"));
        List<String> matList = new ArrayList<>();
        int matCount = 1;
        if (json.has("material")) {
            JsonElement matEl = json.get("material");
            if (matEl.isJsonObject()) {
                JsonObject matObj = matEl.getAsJsonObject();
                matList = parseRawList(matObj.has("ingredient") ? matObj.get("ingredient") : matObj.get("item"));
                matCount = matObj.has("count") ? matObj.get("count").getAsInt() : 1;
            } else {
                matList = parseRawList(matEl);
            }
        }
        List<String> catList = new ArrayList<>();
        int catCount = 1;
        if (json.has("catalyst")) {
            JsonElement catEl = json.get("catalyst");
            if (catEl.isJsonObject()) {
                JsonObject catObj = catEl.getAsJsonObject();
                catList = parseRawList(catObj.has("ingredient") ? catObj.get("ingredient") : catObj.get("item"));
                catCount = catObj.has("count") ? catObj.get("count").getAsInt() : 1;
            } else {
                catList = parseRawList(catEl);
            }
        }
        String resItemId = "";
        int resCount = 1;
        if (json.has("result")) {
            JsonElement resEl = json.get("result");
            if (resEl.isJsonObject()) {
                JsonObject resObj = resEl.getAsJsonObject();
                resItemId = resObj.has("id") ? resObj.get("id").getAsString() : (resObj.has("item") ? resObj.get("item").getAsString() : "");
                resCount = resObj.has("count") ? resObj.get("count").getAsInt() : 1;
            } else if (resEl.isJsonPrimitive()) {
                resItemId = resEl.getAsString();
            }
        }
        return new CursedAltarRecipe(id, baseList, matList, matCount, catList, catCount, resItemId, resCount);
    }

    private static List<String> parseRawList(JsonElement element) {
        List<String> list = new ArrayList<>();
        if (element == null) return list;
        if (element.isJsonArray()) {
            for (JsonElement e : element.getAsJsonArray()) {
                if (e.isJsonPrimitive()) list.add(e.getAsString());
            }
        } else if (element.isJsonPrimitive()) {
            list.add(element.getAsString());
        }
        return list;
    }

    private static List<Item> resolveItems(List<String> rawList) {
        List<Item> list = new ArrayList<>();
        for (String str : rawList) {
            if (str.startsWith("#")) {
                Identifier tagId = Identifier.tryParse(str.substring(1));
                if (tagId != null) {
                    TagKey<Item> tagKey = TagKey.create(Registries.ITEM, tagId);
                    BuiltInRegistries.ITEM.get(tagKey).ifPresent(holders -> {
                        for (Holder<Item> holder : holders) {
                            list.add(holder.value());
                        }
                    });
                }
            } else {
                Identifier itemId = Identifier.tryParse(str);
                if (itemId != null) {
                    BuiltInRegistries.ITEM.getOptional(itemId).ifPresent(list::add);
                }
            }
        }
        return list;
    }

    public Identifier id() {
        return id;
    }

    public int materialCount() {
        return materialCount;
    }

    public int catalystCount() {
        return catalystCount;
    }

    public List<Item> baseItems() {
        if (resolvedBaseItems == null) {
            resolvedBaseItems = resolveItems(baseRawList);
        }
        return resolvedBaseItems;
    }

    public List<Item> materialItems() {
        if (resolvedMaterialItems == null) {
            resolvedMaterialItems = resolveItems(materialRawList);
        }
        return resolvedMaterialItems;
    }

    public List<Item> catalystItems() {
        if (resolvedCatalystItems == null) {
            resolvedCatalystItems = resolveItems(catalystRawList);
        }
        return resolvedCatalystItems;
    }

    public ItemStack result() {
        if (resultItemId == null || resultItemId.isEmpty()) return ItemStack.EMPTY;
        Identifier resId = Identifier.tryParse(resultItemId);
        if (resId == null) return ItemStack.EMPTY;
        Item item = BuiltInRegistries.ITEM.getValue(resId);
        if (item == Items.AIR) return ItemStack.EMPTY;
        return new ItemStack(item, resultCount);
    }

    public boolean matches(ItemStack base, ItemStack mat, ItemStack cat) {
        if (base.isEmpty() || mat.isEmpty() || cat.isEmpty()) return false;
        List<Item> baseList = baseItems();
        List<Item> matList = materialItems();
        List<Item> catList = catalystItems();
        if (baseList.isEmpty() || matList.isEmpty() || catList.isEmpty()) return false;
        boolean baseOk = baseList.contains(base.getItem());
        boolean matOk = matList.contains(mat.getItem()) && mat.getCount() >= materialCount;
        boolean catOk = catList.contains(cat.getItem()) && cat.getCount() >= catalystCount;
        boolean swappedMatOk = matList.contains(cat.getItem()) && cat.getCount() >= materialCount;
        boolean swappedCatOk = catList.contains(mat.getItem()) && mat.getCount() >= catalystCount;
        return baseOk && ((matOk && catOk) || (swappedMatOk && swappedCatOk));
    }
}