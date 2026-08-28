package com.maxwell.gunsmiths_gadgetsn_gizmos.recipe;

import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModRecipes;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public record CursedAltarRecipe(
        Ingredient base,
        SizedIngredient material,
        SizedIngredient catalyst,
        ItemStack result
) implements Recipe<CursedAltarRecipeInput> {
    public static final MapCodec<CursedAltarRecipe> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Ingredient.CODEC.fieldOf("base").forGetter(CursedAltarRecipe::base),
            SizedIngredient.CODEC.fieldOf("material").forGetter(CursedAltarRecipe::material),
            SizedIngredient.CODEC.fieldOf("catalyst").forGetter(CursedAltarRecipe::catalyst),
            ItemStack.CODEC.fieldOf("result").forGetter(CursedAltarRecipe::result)
    ).apply(builder, CursedAltarRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CursedAltarRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC, CursedAltarRecipe::base,
            SizedIngredient.STREAM_CODEC, CursedAltarRecipe::material,
            SizedIngredient.STREAM_CODEC, CursedAltarRecipe::catalyst,
            ItemStack.STREAM_CODEC, CursedAltarRecipe::result,
            CursedAltarRecipe::new
    );

    @Override
    public boolean matches(CursedAltarRecipeInput input, @NonNull Level level) {
        boolean baseMatch = base.test(input.base());
        boolean matMatch = material.test(input.material());
        boolean catMatch = catalyst.test(input.catalyst());
        return baseMatch && matMatch && catMatch;
    }

    @Override
    public @NonNull ItemStack assemble(@NonNull CursedAltarRecipeInput input) {
        return this.result.copy();
    }

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public @NonNull String group() {
        return "";
    }

    @Override
    public @NonNull PlacementInfo placementInfo() {
        return PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public @Nullable RecipeBookCategory recipeBookCategory() {
        return null;
    }

    @Override
    public @NonNull RecipeType<? extends Recipe<CursedAltarRecipeInput>> getType() {
        return ModRecipes.CURSED_ALTAR_TYPE.get();
    }

    @Override
    public @NonNull RecipeSerializer<? extends Recipe<CursedAltarRecipeInput>> getSerializer() {
        return ModRecipes.CURSED_ALTAR_SERIALIZER.get();
    }

    public record SizedIngredient(Ingredient ingredient, int count) {
        public static final Codec<SizedIngredient> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(SizedIngredient::ingredient),
                Codec.INT.optionalFieldOf("count", 1).forGetter(SizedIngredient::count)
        ).apply(builder, SizedIngredient::new));
        public static final StreamCodec<RegistryFriendlyByteBuf, SizedIngredient> STREAM_CODEC = StreamCodec.composite(
                Ingredient.CONTENTS_STREAM_CODEC, SizedIngredient::ingredient,
                ByteBufCodecs.VAR_INT, SizedIngredient::count,
                SizedIngredient::new
        );

        public boolean test(ItemStack stack) {
            return ingredient.test(stack) && stack.getCount() >= count;
        }
    }
}