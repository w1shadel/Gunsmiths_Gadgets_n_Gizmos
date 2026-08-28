package com.maxwell.gunsmiths_gadgetsn_gizmos.compat.jei;

import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModBlocks;
import com.maxwell.gunsmiths_gadgetsn_gizmos.recipe.CursedAltarRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("removal")
@JeiPlugin
public final class GunsmithsJeiPlugin implements IModPlugin {
    public static final String MODID = "gunsmiths_gadgetsn_gizmos";
    private static final Identifier PLUGIN_UID = Identifier.fromNamespaceAndPath(MODID, "jei_plugin");

    @Override
    public @NonNull Identifier getPluginUid() {
        return PLUGIN_UID;
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new CursedAltarRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<RecipeHolder<CursedAltarRecipe>> altarRecipes = new ArrayList<>();
        var server = Minecraft.getInstance().getSingleplayerServer();
        if (server != null) {
            var level = server.overworld();
            var resourceManager = server.getResourceManager();
            var resources = resourceManager.listResources("recipe", id -> id.getNamespace().equals(MODID));
            var ops = level.registryAccess().createSerializationContext(com.mojang.serialization.JsonOps.INSTANCE);
            int index = 0;
            for (var entry : resources.entrySet()) {
                try (var reader = entry.getValue().openAsReader()) {
                    var json = com.google.gson.JsonParser.parseReader(reader);
                    var res = net.minecraft.world.item.crafting.Recipe.CONDITIONAL_CODEC.parse(ops, json);
                    int finalIndex = index++;
                    res.ifSuccess(opt -> opt.ifPresent(withCond -> {
                        if (withCond.carrier() instanceof CursedAltarRecipe altarRecipe) {
                            var recipeKey = ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE,
                                    Identifier.fromNamespaceAndPath(MODID, "cursed_altar_" + finalIndex));
                            altarRecipes.add(new RecipeHolder<>(recipeKey, altarRecipe));
                        }
                    }));
                } catch (Exception ignored) {
                }
            }
        }
        System.out.println("[Gunsmiths JEI] Successfully Registered Cursed Altar Recipes: " + altarRecipes.size());
        registration.addRecipes(JeiRecipeTypes.CURSED_ALTAR, altarRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CURSED_ALTAR.get()), JeiRecipeTypes.CURSED_ALTAR);
    }
}