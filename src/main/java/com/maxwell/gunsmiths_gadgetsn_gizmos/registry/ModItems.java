package com.maxwell.gunsmiths_gadgetsn_gizmos.registry;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.*;
import io.redspace.irons_artifice.modifier.ModifierItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(GunsmithsGadgetsnGizmos.MODID);
    public static final DeferredItem<ModifierItem> PISTON_RAMROD_MODIFIER = ITEMS.registerItem(
            "piston_ramrod_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new PistonRamrodModifier())
    );
    public static final DeferredItem<ModifierItem> TOWN_BELL_FLARE_MODIFIER = ITEMS.registerItem(
            "town_bell_flare_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new TownBellFlareModifier())
    );
    public static final DeferredItem<ModifierItem> BREEZE_CYCLONE_MODIFIER = ITEMS.registerItem(
            "breeze_cyclone_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new BreezeCycloneModifier())
    );
    public static final DeferredItem<ModifierItem> OMINOUS_CHAMBER_MODIFIER = ITEMS.registerItem(
            "ominous_chamber_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new OminousChamberModifier())
    );
    public static final DeferredItem<ModifierItem> HEAVY_CORE_IMPACT_MODIFIER = ITEMS.registerItem(
            "heavy_core_impact_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new HeavyCoreImpactModifier())
    );
    public static final DeferredItem<ModifierItem> GRAPPLING_ANCHOR_MODIFIER = ITEMS.registerItem(
            "grappling_anchor_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new GrapplingAnchorModifier())
    );
    public static final DeferredItem<Item> OMINOUS_CLOCKWORK_CORE = ITEMS.registerSimpleItem("ominous_clockwork_core");
    public static final DeferredItem<ModifierItem> BLOODBOUND_CALAMITY_MODIFIER = ITEMS.registerItem(
            "bloodbound_calamity_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new BloodboundCalamityModifier())
    );
    public static final DeferredItem<ModifierItem> UNSTABLE_OVERCLOCK_MODIFIER = ITEMS.registerItem(
            "unstable_overclock_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new UnstableOverclockModifier())
    );
    public static final DeferredItem<ModifierItem> REAPERS_GAMBIT_MODIFIER = ITEMS.registerItem(
            "reapers_gambit_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new ReapersGambitModifier())
    );
    public static final DeferredItem<ModifierItem> TRIAL_OF_GREED_MODIFIER = ITEMS.registerItem(
            "trial_of_greed_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new TrialOfGreedModifier())
    );

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}