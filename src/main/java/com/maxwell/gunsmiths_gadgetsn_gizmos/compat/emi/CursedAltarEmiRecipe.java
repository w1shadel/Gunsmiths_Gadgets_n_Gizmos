package com.maxwell.gunsmiths_gadgetsn_gizmos.compat.emi;

import com.maxwell.gunsmiths_gadgetsn_gizmos.recipe.CursedAltarRecipe;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CursedAltarEmiRecipe implements EmiRecipe {
    private final CursedAltarRecipe recipe;
    private final ResourceLocation id;
    private final EmiIngredient base;
    private final EmiIngredient material;
    private final EmiIngredient catalyst;
    private final EmiStack result;

    public CursedAltarEmiRecipe(CursedAltarRecipe recipe) {
        this.recipe = recipe;

        this.id = recipe.id() != null
                ? ResourceLocation.fromNamespaceAndPath(recipe.id().getNamespace(), "/" + recipe.id().getPath())
                : null;

        this.base = EmiIngredient.of(recipe.baseItems().stream()
                .filter(item -> item != null && item != Items.AIR)
                .map(EmiStack::of)
                .toList());

        this.material = EmiIngredient.of(recipe.materialItems().stream()
                .filter(item -> item != null && item != Items.AIR)
                .map(item -> EmiStack.of(item, recipe.materialCount()))
                .toList());

        this.catalyst = EmiIngredient.of(recipe.catalystItems().stream()
                .filter(item -> item != null && item != Items.AIR)
                .map(item -> EmiStack.of(item, recipe.catalystCount()))
                .toList());

        this.result = EmiStack.of(recipe.result());
    }

    @Override
    public EmiRecipeCategory getCategory() {
        return GunsmithsEmiPlugin.CURSED_ALTAR_CATEGORY;
    }

    @Override
    public @Nullable ResourceLocation getId() {
        return this.id;
    }

    @Override
    public List<EmiIngredient> getInputs() {
        return List.of(this.base, this.material, this.catalyst);
    }

    @Override
    public List<EmiStack> getOutputs() {
        return List.of(this.result);
    }

    @Override
    public int getDisplayWidth() {
        return 160;
    }

    @Override
    public int getDisplayHeight() {
        return 65;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        widgets.addSlot(this.base, 15, 35);
        widgets.addSlot(this.material, 55, 35);
        widgets.addSlot(this.catalyst, 35, 10);
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 85, 23);
        widgets.addSlot(this.result, 120, 23).recipeContext(this);
    }
}