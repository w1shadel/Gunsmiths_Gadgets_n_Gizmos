package com.maxwell.gunsmiths_gadgetsn_gizmos.api.detagen;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModBlocks;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModItems;
import io.redspace.irons_artifice.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.Blocks;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

public class VanillaModRecipeProvider extends RecipeProvider {
    public VanillaModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    private static ResourceKey<Recipe<?>> recipeId(String name) {
        return ResourceKey.create(Registries.RECIPE, Identifier.fromNamespaceAndPath(GunsmithsGadgetsnGizmos.MODID, name));
    }

    @Override
    protected void buildRecipes() {
        var itemLookup = this.registries.lookupOrThrow(Registries.ITEM);
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.BUILDING_BLOCKS, ModBlocks.GUNSMITH_BENCH.get())
                .pattern(" I ")
                .pattern("PAP")
                .pattern("LLL")
                .define('A', Blocks.ANVIL)
                .define('I', Items.IRON_BLOCK)
                .define('P', ItemRegistry.SIMPLE_MECHANICAL_COMPONENTS.get())
                .define('L', ItemTags.LOGS)
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(this.output, recipeId("gunsmith_bench"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CURSED_ALTAR.get())
                .pattern(" C ")
                .pattern("OBO")
                .pattern("SSS")
                .define('B', ModBlocks.GUNSMITH_BENCH.get())
                .define('O', Blocks.CRYING_OBSIDIAN)
                .define('S', Blocks.SOUL_SAND)
                .define('C', ModItems.OMINOUS_CLOCKWORK_CORE.get())
                .unlockedBy("has_omen_core", has(ModItems.OMINOUS_CLOCKWORK_CORE.get()))
                .save(this.output, recipeId("cursed_altar"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, ModItems.AMMO_POUCH.get())
                .pattern("LIL")
                .pattern("S#S")
                .pattern("LLL")
                .define('L', Items.LEATHER)
                .define('I', Items.IRON_INGOT)
                .define('S', Items.STRING)
                .define('#', ItemRegistry.BULLET.get())
                .unlockedBy("has_bullet", has(ItemRegistry.BULLET.get()))
                .save(this.output, recipeId("ammo_pouch"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.MISC, ModItems.GUNSMITH_CHASSIS_KIT.get())
                .pattern("IMI")
                .pattern("MCM")
                .pattern("IGI")
                .define('I', Items.IRON_BLOCK)
                .define('M', ItemRegistry.MECHANICAL_COMPONENTS.get())
                .define('C', ItemRegistry.CLOCKWORK_COMPONENTS.get())
                .define('G', Items.GOLD_INGOT)
                .unlockedBy("has_mechanical", has(ItemRegistry.MECHANICAL_COMPONENTS.get()))
                .save(this.output, recipeId("gunsmith_chassis_kit"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, ModItems.SILVER_BULLET.get(), 16)
                .pattern(" G ")
                .pattern(" I ")
                .pattern(" B ")
                .define('G', Items.GOLD_INGOT)
                .define('I', Items.IRON_INGOT)
                .define('B', ItemRegistry.BLACKPOWDER.get())
                .unlockedBy("has_blackpowder", has(ItemRegistry.BLACKPOWDER.get()))
                .save(this.output, recipeId("silver_bullet"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, ModItems.AP_BULLET.get(), 16)
                .pattern(" C ")
                .pattern(" I ")
                .pattern(" B ")
                .define('C', ModItems.COAGULATED_OMEN_BLOOD.get())
                .define('I', Items.IRON_INGOT)
                .define('B', ItemRegistry.BLACKPOWDER.get())
                .unlockedBy("has_omen_blood", has(ModItems.COAGULATED_OMEN_BLOOD.get()))
                .save(this.output, recipeId("ap_bullet"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, ModItems.GLASS_BULLET.get(), 16)
                .pattern(" G ")
                .pattern(" N ")
                .pattern(" B ")
                .define('G', Blocks.GLASS_PANE)
                .define('N', Items.IRON_NUGGET)
                .define('B', ItemRegistry.BLACKPOWDER.get())
                .unlockedBy("has_blackpowder", has(ItemRegistry.BLACKPOWDER.get()))
                .save(this.output, recipeId("glass_bullet"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, ModItems.TRACER_BULLET.get(), 16)
                .pattern(" D ")
                .pattern(" I ")
                .pattern(" B ")
                .define('D', Items.GLOWSTONE_DUST)
                .define('I', Items.IRON_INGOT)
                .define('B', ItemRegistry.BLACKPOWDER.get())
                .unlockedBy("has_blackpowder", has(ItemRegistry.BLACKPOWDER.get()))
                .save(this.output, recipeId("tracer_bullet"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, ModItems.PISTON_RAMROD_MODIFIER.get())
                .pattern(" P ")
                .pattern(" I ")
                .pattern(" M ")
                .define('P', Blocks.PISTON)
                .define('I', Items.IRON_INGOT)
                .define('M', ItemRegistry.SIMPLE_MECHANICAL_COMPONENTS.get())
                .unlockedBy("has_simple_parts", has(ItemRegistry.SIMPLE_MECHANICAL_COMPONENTS.get()))
                .save(this.output, recipeId("piston_ramrod_modifier"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, ModItems.GRAPPLING_ANCHOR_MODIFIER.get())
                .pattern(" A ")
                .pattern("CSC")
                .pattern(" M ")
                .define('A', Items.ARROW)
                .define('C', Items.IRON_CHAIN)
                .define('S', Items.SLIME_BALL)
                .define('M', ItemRegistry.SIMPLE_MECHANICAL_COMPONENTS.get())
                .unlockedBy("has_simple_parts", has(ItemRegistry.SIMPLE_MECHANICAL_COMPONENTS.get()))
                .save(this.output, recipeId("grappling_anchor_modifier"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, ModItems.TOWN_BELL_FLARE_MODIFIER.get())
                .pattern(" G ")
                .pattern(" F ")
                .pattern(" M ")
                .define('G', Items.GOLD_INGOT)
                .define('F', Items.FIREWORK_STAR)
                .define('M', ItemRegistry.SIMPLE_MECHANICAL_COMPONENTS.get())
                .unlockedBy("has_simple_parts", has(ItemRegistry.SIMPLE_MECHANICAL_COMPONENTS.get()))
                .save(this.output, recipeId("town_bell_flare_modifier"));
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new VanillaModRecipeProvider(registries, output);
        }

        @Override
        public @NonNull String getName() {
            return GunsmithsGadgetsnGizmos.MODID + " Crafting Recipes";
        }
    }
}