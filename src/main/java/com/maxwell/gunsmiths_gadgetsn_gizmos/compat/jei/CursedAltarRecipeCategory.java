package com.maxwell.gunsmiths_gadgetsn_gizmos.compat.jei;

import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModBlocks;
import com.maxwell.gunsmiths_gadgetsn_gizmos.recipe.CursedAltarRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.NonNull;

import java.util.List;

public final class CursedAltarRecipeCategory implements IRecipeCategory<CursedAltarRecipe> {
    public static final String MODID = "gunsmiths_gadgetsn_gizmos";
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic slotBackground;

    public CursedAltarRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(160, 65);
        this.slotBackground = helper.getSlotDrawable();
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CURSED_ALTAR.get()));
    }

    @Override
    public @NonNull IRecipeType<CursedAltarRecipe> getRecipeType() {
        return JeiRecipeTypes.CURSED_ALTAR;
    }

    @Override
    public @NonNull Component getTitle() {
        return Component.translatable("gui.gunsmiths_gadgetsn_gizmos.cursed_altar");
    }

    @Override
    public int getWidth() {
        return 160;
    }

    @Override
    public int getHeight() {
        return 65;
    }

    @Override
    public @NonNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CursedAltarRecipe recipe, @NonNull IFocusGroup focuses) {
        List<ItemStack> baseStacks = recipe.baseItems().stream()
                .filter(item -> item != null && item != Items.AIR)
                .map(ItemStack::new)
                .toList();
        if (!baseStacks.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 15, 35)
                    .addItemStacks(baseStacks)
                    .setBackground(slotBackground, -1, -1);
        }
        List<ItemStack> matStacks = recipe.materialItems().stream()
                .filter(item -> item != null && item != Items.AIR)
                .map(item -> new ItemStack(item, recipe.materialCount()))
                .toList();
        if (!matStacks.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 55, 35)
                    .addItemStacks(matStacks)
                    .setBackground(slotBackground, -1, -1);
        }
        List<ItemStack> catStacks = recipe.catalystItems().stream()
                .filter(item -> item != null && item != Items.AIR)
                .map(item -> new ItemStack(item, recipe.catalystCount()))
                .toList();
        if (!catStacks.isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 35, 10)
                    .addItemStacks(catStacks)
                    .setBackground(slotBackground, -1, -1);
        }
        if (!recipe.result().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 23)
                    .addItemStack(recipe.result())
                    .setBackground(slotBackground, -1, -1);
        }
    }

    @Override
    public void draw(@NonNull CursedAltarRecipe recipe, @NonNull IRecipeSlotsView recipeSlotsView, @NonNull GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
    }
}