package com.maxwell.gunsmiths_gadgetsn_gizmos.compat.jei;

import com.maxwell.gunsmiths_gadgetsn_gizmos.recipe.CursedAltarRecipe;
import mezz.jei.api.recipe.RecipeType;

public final class JeiRecipeTypes {
    public static final String MODID = "gunsmiths_gadgetsn_gizmos";

    public static final RecipeType<CursedAltarRecipe> CURSED_ALTAR =
            RecipeType.create(
                    MODID,
                    "cursed_altar",
                    CursedAltarRecipe.class
            );

    private JeiRecipeTypes() {
    }
}