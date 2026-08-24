package com.maxwell.gunsmiths_gadgetsn_gizmos.registry;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.item.AmmoPouchItem;
import com.maxwell.gunsmiths_gadgetsn_gizmos.item.InfiniteAmmoBagItem;
import com.maxwell.gunsmiths_gadgetsn_gizmos.item.UnidentifiedCrateItem;
import com.maxwell.gunsmiths_gadgetsn_gizmos.item.curios.GunsmithCurioItem;
import com.maxwell.gunsmiths_gadgetsn_gizmos.item.curios.HeavyBandolierItem;
import com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.*;
import io.redspace.irons_artifice.item.GunItem;
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
    public static final DeferredItem<ModifierItem> MASTERCRAFTED_TRIGGER_MODIFIER = ITEMS.registerItem(
            "mastercrafted_trigger_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new MastercraftedTriggerModifier())
    );
    public static final DeferredItem<ModifierItem> MERCHANT_BOUNTY_MODIFIER = ITEMS.registerItem(
            "merchant_bounty_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new MerchantBountyModifier())
    );
    public static final DeferredItem<Item> UNIDENTIFIED_CRATE = ITEMS.registerItem(
            "unidentified_crate",
            UnidentifiedCrateItem::new
    );
    public static final DeferredItem<ModifierItem> CRIMSON_SINGULARITY_MODIFIER = ITEMS.registerItem(
            "crimson_singularity_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new CrimsonSingularityModifier())
    );
    public static final DeferredItem<ModifierItem> CLOCKWORK_GATLING_MODIFIER = ITEMS.registerItem(
            "clockwork_gatling_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new ClockworkGatlingModifier())
    );
    public static final DeferredItem<ModifierItem> REAPERS_TEMPEST_MODIFIER = ITEMS.registerItem(
            "reapers_tempest_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new ReapersTempestModifier())
    );
    public static final DeferredItem<ModifierItem> MIDAS_TOUCH_CHAMBER_MODIFIER = ITEMS.registerItem(
            "midas_touch_chamber_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new MidasTouchChamberModifier())
    );
    public static final DeferredItem<ModifierItem> ECHOING_SONIC_CORE_MODIFIER = ITEMS.registerItem(
            "echoing_sonic_core_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new EchoingSonicCoreModifier())
    );
    public static final DeferredItem<ModifierItem> SCULK_WHISPER_SILENCER_MODIFIER = ITEMS.registerItem(
            "sculk_whisper_silencer_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new SculkWhisperSilencerModifier())
    );
    public static final DeferredItem<ModifierItem> SCULK_DEVOURER_MODIFIER = ITEMS.registerItem(
            "sculk_devourer_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new SculkDevourerModifier())
    );
    public static final DeferredItem<ModifierItem> SHRIEKING_DREAD_MODIFIER = ITEMS.registerItem(
            "shrieking_dread_modifier",
            properties -> new ModifierItem(properties.stacksTo(1), new ShriekingDreadModifier())
    );
    public static final DeferredItem<Item> INFINITE_AMMO_BAG = ITEMS.registerItem(
            "infinite_ammo_bag",
            InfiniteAmmoBagItem::new
    );
    public static final DeferredItem<Item> SILVER_BULLET = ITEMS.registerSimpleItem("silver_bullet");
    public static final DeferredItem<Item> AP_BULLET = ITEMS.registerSimpleItem("ap_bullet");
    public static final DeferredItem<Item> AMMO_POUCH = ITEMS.registerItem(
            "ammo_pouch",
            AmmoPouchItem::new
    );
    // クランクライフル（魔改造レバーアクション銃）
    public static final DeferredItem<GunItem> CLUNKER_RIFLE = ITEMS.registerItem(
            "clunker_rifle",
            properties -> new GunItem(properties, ModGuns.CLUNKER_RIFLE)
    );
    public static final DeferredItem<Item> RANGEFINDER_MONOCLE = ITEMS.registerItem("rangefinder_monocle",
            p -> new GunsmithCurioItem(p, "rangefinder_monocle"));
    public static final DeferredItem<Item> WELDER_GOGGLES = ITEMS.registerItem("welder_goggles",
            p -> new GunsmithCurioItem(p, "welder_goggles"));
    public static final DeferredItem<Item> HEAVY_BANDOLIER = ITEMS.registerItem("heavy_bandolier",
            HeavyBandolierItem::new);
    public static final DeferredItem<Item> RECOIL_HARNESS = ITEMS.registerItem("recoil_harness",
            p -> new GunsmithCurioItem(p, "recoil_harness"));
    public static final DeferredItem<Item> QUICK_DRAW_HOLSTER = ITEMS.registerItem("quick_draw_holster",
            p -> new GunsmithCurioItem(p, "quick_draw_holster"));
    public static final DeferredItem<Item> SPEEDLOADER_BELT = ITEMS.registerItem("speedloader_belt",
            p -> new GunsmithCurioItem(p, "speedloader_belt"));
    public static final DeferredItem<Item> MAGNETIC_POUCH = ITEMS.registerItem("magnetic_pouch",
            p -> new GunsmithCurioItem(p, "magnetic_pouch"));
    public static final DeferredItem<Item> GUNSMITHS_GLOVES = ITEMS.registerItem("gunsmiths_gloves",
            p -> new GunsmithCurioItem(p, "gunsmiths_gloves"));
    public static final DeferredItem<Item> GAMBLERS_RING = ITEMS.registerItem("gamblers_ring",
            p -> new GunsmithCurioItem(p, "gamblers_ring"));
    public static final DeferredItem<Item> GUNSLINGERS_SPURS = ITEMS.registerItem("gunslingers_spurs",
            p -> new GunsmithCurioItem(p, "gunslingers_spurs"));
    public static final DeferredItem<Item> GUNSMITH_CHASSIS_KIT = ITEMS.registerSimpleItem("gunsmith_chassis_kit");
    public static final DeferredItem<Item> VOID_CASING = ITEMS.registerSimpleItem("void_casing");
    public static final DeferredItem<Item> SOUL_CINDER = ITEMS.registerSimpleItem("soul_cinder");
    public static final DeferredItem<Item> CURSED_BRASS_INGOT = ITEMS.registerSimpleItem("cursed_brass_ingot");
    public static final DeferredItem<Item> COAGULATED_OMEN_BLOOD = ITEMS.registerSimpleItem("coagulated_omen_blood");
    public static final DeferredItem<Item> FORBIDDEN_BLUEPRINT = ITEMS.registerSimpleItem("forbidden_blueprint");

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}