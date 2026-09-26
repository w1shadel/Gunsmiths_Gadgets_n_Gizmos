package com.maxwell.gunsmiths_gadgetsn_gizmos.compat.jei;

import com.maxwell.gunsmiths_gadgetsn_gizmos.recipe.CursedAltarRecipe;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.resources.ResourceLocation;

public final class JeiRecipeTypes {
    public static final String MODID = "gunsmiths_gadgetsn_gizmos";
    public static final IRecipeType<CursedAltarRecipe> CURSED_ALTAR =
            IRecipeType.create(
                    ResourceLocation.fromNamespaceAndPath(MODID, "cursed_altar"),
                    CursedAltarRecipe.class
            );
}