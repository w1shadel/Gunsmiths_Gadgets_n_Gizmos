package com.maxwell.gunsmiths_gadgetsn_gizmos.api.detagen;

import com.maxwell.gunsmiths_gadgetsn_gizmos.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.concurrent.CompletableFuture;

public class CuriosTagDataProvider extends IntrinsicHolderTagsProvider<Item> {
    private static final TagKey<Item> CURIO_HEAD = curiosTag("head");
    private static final TagKey<Item> CURIO_BODY = curiosTag("body");
    private static final TagKey<Item> CURIO_BACK = curiosTag("back");
    private static final TagKey<Item> CURIO_BELT = curiosTag("belt");
    private static final TagKey<Item> CURIO_HANDS = curiosTag("hands");
    private static final TagKey<Item> CURIO_RING = curiosTag("ring");
    private static final TagKey<Item> CURIO_FEET = curiosTag("feet");
    private static final TagKey<Item> CURIO_CHARM = curiosTag("charm");
    private static final TagKey<Item> CURIO_ANY = curiosTag("curio");

    public CuriosTagDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Registries.ITEM, lookupProvider, item -> item.builtInRegistryHolder().key());
    }

    private static TagKey<Item> curiosTag(String slotName) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("curios", slotName));
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        this.tag(CURIO_HEAD)
                .add(ModItems.RANGEFINDER_MONOCLE.get())
                .add(ModItems.WELDER_GOGGLES.get());
        this.tag(CURIO_CHARM)
                .add(ModItems.RANGEFINDER_MONOCLE.get());
        this.tag(CURIO_BODY)
                .add(ModItems.HEAVY_BANDOLIER.get())
                .add(ModItems.RECOIL_HARNESS.get());
        this.tag(CURIO_BACK)
                .add(ModItems.HEAVY_BANDOLIER.get())
                .add(ModItems.RECOIL_HARNESS.get());
        this.tag(CURIO_BELT)
                .add(ModItems.QUICK_DRAW_HOLSTER.get())
                .add(ModItems.SPEEDLOADER_BELT.get())
                .add(ModItems.MAGNETIC_POUCH.get());
        this.tag(CURIO_HANDS)
                .add(ModItems.GUNSMITHS_GLOVES.get());
        this.tag(CURIO_RING)
                .add(ModItems.GAMBLERS_RING.get());
        this.tag(CURIO_FEET)
                .add(ModItems.GUNSLINGERS_SPURS.get());
        this.tag(CURIO_ANY)
                .add(ModItems.GUNSLINGERS_SPURS.get());
    }
}