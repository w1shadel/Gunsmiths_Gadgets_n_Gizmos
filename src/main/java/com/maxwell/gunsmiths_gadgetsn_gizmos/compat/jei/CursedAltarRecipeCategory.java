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
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jspecify.annotations.NonNull;

@SuppressWarnings("removal")
public final class CursedAltarRecipeCategory implements IRecipeCategory<RecipeHolder<CursedAltarRecipe>> {
    public static final String MODID = "gunsmiths_gadgetsn_gizmos";
    private static final Identifier BACKGROUND_LOC = Identifier.fromNamespaceAndPath(MODID, "textures/gui/cursed_altar.png");
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic slotBackground;

    public CursedAltarRecipeCategory(IGuiHelper helper) {
        this.background = helper.createBlankDrawable(160, 65);
        this.slotBackground = helper.getSlotDrawable();
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CURSED_ALTAR.get()));
    }

    @Override
    public @NonNull IRecipeType<RecipeHolder<CursedAltarRecipe>> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CursedAltarRecipe> holder, @NonNull IFocusGroup focuses) {
        CursedAltarRecipe recipe = holder.value();
        builder.addSlot(RecipeIngredientRole.INPUT, 15, 25)
                .addIngredients(recipe.base())
                .setBackground(slotBackground, -1, -1);
        builder.addSlot(RecipeIngredientRole.INPUT, 45, 25)
                .addIngredients(recipe.material().ingredient())
                .setBackground(slotBackground, -1, -1);
        builder.addSlot(RecipeIngredientRole.INPUT, 75, 25)
                .addIngredients(recipe.catalyst().ingredient())
                .setBackground(slotBackground, -1, -1);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 125, 25)
                .addItemStack(recipe.result())
                .setBackground(slotBackground, -1, -1);
    }

    @Override
    public void draw(@NonNull RecipeHolder<CursedAltarRecipe> recipe, @NonNull IRecipeSlotsView recipeSlotsView, @NonNull GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        background.draw(guiGraphics);
    }
}