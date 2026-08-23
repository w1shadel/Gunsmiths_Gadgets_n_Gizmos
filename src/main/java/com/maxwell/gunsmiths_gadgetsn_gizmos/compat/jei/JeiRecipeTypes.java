package com.maxwell.gunsmiths_gadgetsn_gizmos.compat.jei;

import com.maxwell.gunsmiths_gadgetsn_gizmos.recipe.CursedAltarRecipe;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeHolder;

public final class JeiRecipeTypes {
    public static final String MODID = "gunsmiths_gadgetsn_gizmos";
    @SuppressWarnings("unchecked")
    public static final IRecipeType<RecipeHolder<CursedAltarRecipe>> CURSED_ALTAR =
            IRecipeType.create(
                    Identifier.fromNamespaceAndPath(MODID, "cursed_altar"),
                    (Class<RecipeHolder<CursedAltarRecipe>>) (Class<?>) RecipeHolder.class
            );
}