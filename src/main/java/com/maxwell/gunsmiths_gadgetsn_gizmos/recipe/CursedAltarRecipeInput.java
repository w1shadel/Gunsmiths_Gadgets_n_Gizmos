package com.maxwell.gunsmiths_gadgetsn_gizmos.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

public record CursedAltarRecipeInput(ItemStack base, ItemStack material, ItemStack catalyst) implements RecipeInput {
    @Override
    public ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> base;
            case 1 -> material;
            case 2 -> catalyst;
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public int size() {
        return 3;
    }
}