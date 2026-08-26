package com.maxwell.gunsmiths_gadgetsn_gizmos.init;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.recipe.CursedAltarRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(Registries.RECIPE_TYPE, GunsmithsGadgetsnGizmos.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(Registries.RECIPE_SERIALIZER, GunsmithsGadgetsnGizmos.MODID);
    public static final DeferredHolder<RecipeType<?>, RecipeType<CursedAltarRecipe>> CURSED_ALTAR_TYPE =
            RECIPE_TYPES.register("cursed_altar", () -> RecipeType.simple(
                    Identifier.fromNamespaceAndPath(GunsmithsGadgetsnGizmos.MODID, "cursed_altar")
            ));
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CursedAltarRecipe>> CURSED_ALTAR_SERIALIZER =
            RECIPE_SERIALIZERS.register("cursed_altar",
                    () -> new RecipeSerializer<>(CursedAltarRecipe.CODEC, CursedAltarRecipe.STREAM_CODEC)
            );

    public static void register(IEventBus bus) {
        RECIPE_TYPES.register(bus);
        RECIPE_SERIALIZERS.register(bus);
    }
}