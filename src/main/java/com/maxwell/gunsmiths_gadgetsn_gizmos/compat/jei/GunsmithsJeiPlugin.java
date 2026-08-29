package com.maxwell.gunsmiths_gadgetsn_gizmos.compat.jei;

import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModBlocks;
import com.maxwell.gunsmiths_gadgetsn_gizmos.recipe.CursedAltarRecipe;
import com.maxwell.gunsmiths_gadgetsn_gizmos.recipe.CursedAltarRecipeManager;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

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
        List<CursedAltarRecipe> altarRecipes = new ArrayList<>(CursedAltarRecipeManager.getAllRecipes());
        registration.addRecipes(JeiRecipeTypes.CURSED_ALTAR, altarRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CURSED_ALTAR.get()), JeiRecipeTypes.CURSED_ALTAR);
    }
}