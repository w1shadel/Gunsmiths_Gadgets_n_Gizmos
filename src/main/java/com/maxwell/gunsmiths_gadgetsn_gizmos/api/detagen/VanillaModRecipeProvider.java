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
                .define('C', Items.BONE)
                .unlockedBy("has_bone", has(Items.BONE))
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
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, ModItems.BREEZE_CYCLONE_MODIFIER.get())
                .pattern(" R ")
                .pattern("RWR")
                .pattern(" M ")
                .define('R', Items.BREEZE_ROD)
                .define('W', ItemRegistry.WIND_CHAMBER.get())
                .define('M', ItemRegistry.SIMPLE_MECHANICAL_COMPONENTS.get())
                .unlockedBy("has_breeze_rod", has(Items.BREEZE_ROD))
                .save(this.output, recipeId("breeze_cyclone_modifier"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, ModItems.HEAVY_CORE_IMPACT_MODIFIER.get())
                .pattern(" I ")
                .pattern("ILI")
                .pattern(" M ")
                .define('I', Items.IRON_BLOCK)
                .define('L', ItemRegistry.LEAD_CORE.get())
                .define('M', ItemRegistry.MECHANICAL_COMPONENTS.get())
                .unlockedBy("has_lead_core", has(ItemRegistry.LEAD_CORE.get()))
                .save(this.output, recipeId("heavy_core_impact_modifier"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, ModItems.CLUNKER_RIFLE.get())
                .pattern("PI ")
                .pattern(" AM")
                .pattern(" LB")
                .define('P', Blocks.PISTON)
                .define('I', Items.IRON_BLOCK)
                .define('A', ItemRegistry.ARQUEBUS.get())
                .define('M', ItemRegistry.MECHANICAL_COMPONENTS.get())
                .define('L', ItemTags.LOGS)
                .define('B', ItemRegistry.BLACKPOWDER.get())
                .unlockedBy("has_arquebus", has(ItemRegistry.ARQUEBUS.get()))
                .save(this.output, recipeId("clunker_rifle"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.COMBAT, ModItems.MINIGUN.get())
                .pattern("IBI")
                .pattern("CMC")
                .pattern(" H ")
                .define('I', Items.IRON_BLOCK)
                .define('B', ItemRegistry.CLOCKWORK_RIFLE.get())
                .define('C', Items.COPPER_CHAIN.unaffected())
                .define('M', ItemRegistry.CLOCKWORK_COMPONENTS.get())
                .define('H', Items.HOPPER)
                .unlockedBy("has_clockwork_rifle", has(ItemRegistry.CLOCKWORK_RIFLE.get()))
                .save(this.output, recipeId("minigun"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, ModItems.RANGEFINDER_MONOCLE.get())
                .pattern(" G")
                .pattern("SM")
                .define('G', Items.GOLD_INGOT)
                .define('S', Items.SPYGLASS)
                .define('M', ItemRegistry.SIMPLE_MECHANICAL_COMPONENTS.get())
                .unlockedBy("has_spyglass", has(Items.SPYGLASS))
                .save(this.output, recipeId("rangefinder_monocle"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, ModItems.WELDER_GOGGLES.get())
                .pattern("LIL")
                .pattern("T T")
                .define('L', Items.LEATHER)
                .define('I', Items.IRON_INGOT)
                .define('T', Blocks.TINTED_GLASS)
                .unlockedBy("has_tinted_glass", has(Blocks.TINTED_GLASS))
                .save(this.output, recipeId("welder_goggles"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, ModItems.HEAVY_BANDOLIER.get())
                .pattern("LCL")
                .pattern("LPL")
                .pattern("LIL")
                .define('L', Items.LEATHER)
                .define('C', Items.IRON_CHAIN)
                .define('P', ModItems.AMMO_POUCH.get())
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_pouch", has(ModItems.AMMO_POUCH.get()))
                .save(this.output, recipeId("heavy_bandolier"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, ModItems.RECOIL_HARNESS.get())
                .pattern("LPL")
                .pattern("LSL")
                .pattern("LIL")
                .define('L', Items.LEATHER)
                .define('P', Blocks.PISTON)
                .define('S', ItemRegistry.BUFFER_SPRING.get())
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_buffer_spring", has(ItemRegistry.BUFFER_SPRING.get()))
                .save(this.output, recipeId("recoil_harness"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, ModItems.QUICK_DRAW_HOLSTER.get())
                .pattern("LGL")
                .pattern("S S")
                .pattern(" L ")
                .define('L', Items.LEATHER)
                .define('G', Items.GOLD_INGOT)
                .define('S', ItemRegistry.SIMPLE_MECHANICAL_COMPONENTS.get())
                .unlockedBy("has_leather", has(Items.LEATHER))
                .save(this.output, recipeId("quick_draw_holster"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, ModItems.SPEEDLOADER_BELT.get())
                .pattern("LHL")
                .pattern("LML")
                .pattern("LIL")
                .define('L', Items.LEATHER)
                .define('H', Items.HOPPER)
                .define('M', ItemRegistry.MECHANICAL_COMPONENTS.get())
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_hopper", has(Items.HOPPER))
                .save(this.output, recipeId("speedloader_belt"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, ModItems.MAGNETIC_POUCH.get())
                .pattern(" R ")
                .pattern("CPC")
                .pattern(" I ")
                .define('R', Items.REDSTONE)
                .define('C', Items.COPPER_INGOT)
                .define('P', ModItems.AMMO_POUCH.get())
                .define('I', Items.IRON_BLOCK)
                .unlockedBy("has_pouch", has(ModItems.AMMO_POUCH.get()))
                .save(this.output, recipeId("magnetic_pouch"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, ModItems.GUNSMITHS_GLOVES.get())
                .pattern(" O ")
                .pattern("L L")
                .pattern("L L")
                .define('O', ItemRegistry.GUN_OIL.get())
                .define('L', Items.LEATHER)
                .unlockedBy("has_gun_oil", has(ItemRegistry.GUN_OIL.get()))
                .save(this.output, recipeId("gunsmiths_gloves"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, ModItems.GAMBLERS_RING.get())
                .pattern(" E ")
                .pattern("B B")
                .pattern(" B ")
                .define('E', Items.EMERALD)
                .define('B', ModItems.CURSED_BRASS_INGOT.get())
                .unlockedBy("has_cursed_brass", has(ModItems.CURSED_BRASS_INGOT.get()))
                .save(this.output, recipeId("gamblers_ring"));
        ShapedRecipeBuilder.shaped(itemLookup, RecipeCategory.TOOLS, ModItems.GUNSLINGERS_SPURS.get())
                .pattern("I I")
                .pattern("C C")
                .pattern("M M")
                .define('I', Items.IRON_INGOT)
                .define('C', Items.COPPER_CHAIN.unaffected())
                .define('M', ItemRegistry.SIMPLE_MECHANICAL_COMPONENTS.get())
                .unlockedBy("has_iron", has(Items.IRON_INGOT))
                .save(this.output, recipeId("gunslingers_spurs"));
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