package com.maxwell.gunsmiths_gadgetsn_gizmos.compat.emi;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModBlocks;
import com.maxwell.gunsmiths_gadgetsn_gizmos.recipe.CursedAltarRecipe;
import com.maxwell.gunsmiths_gadgetsn_gizmos.recipe.CursedAltarRecipeManager;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;

@EmiEntrypoint
public class GunsmithsEmiPlugin implements EmiPlugin {
    public static final EmiRecipeCategory CURSED_ALTAR_CATEGORY = new EmiRecipeCategory(
            ResourceLocation.fromNamespaceAndPath(GunsmithsGadgetsnGizmos.MODID, "cursed_altar"),
            EmiStack.of(ModBlocks.CURSED_ALTAR.get())
    );

    @Override
    public void register(EmiRegistry registry) {

        registry.addCategory(CURSED_ALTAR_CATEGORY);
        registry.addWorkstation(CURSED_ALTAR_CATEGORY, EmiStack.of(ModBlocks.CURSED_ALTAR.get()));

        for (CursedAltarRecipe recipe : CursedAltarRecipeManager.getAllRecipes()) {
            registry.addRecipe(new CursedAltarEmiRecipe(recipe));
        }
    }
}