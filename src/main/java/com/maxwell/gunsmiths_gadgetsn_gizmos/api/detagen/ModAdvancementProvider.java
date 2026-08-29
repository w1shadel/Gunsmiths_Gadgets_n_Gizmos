package com.maxwell.gunsmiths_gadgetsn_gizmos.api.detagen;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModBlocks;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModEntities;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModItems;
import io.redspace.irons_artifice.registry.ItemRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.criterion.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class ModAdvancementProvider implements AdvancementSubProvider {
    private static String id(String name) {
        return GunsmithsGadgetsnGizmos.MODID + ":" + name;
    }

    @Override
    public void generate(HolderLookup.@NonNull Provider registries, @NonNull Consumer<AdvancementHolder> saver) {
        var itemLookup = registries.lookupOrThrow(Registries.ITEM);
        var entityLookup = registries.lookupOrThrow(Registries.ENTITY_TYPE);
        AdvancementHolder root = Advancement.Builder.advancement()
                .display(
                        ModBlocks.GUNSMITH_BENCH.get(),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.root.title"),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.root.desc"),
                        Identifier.withDefaultNamespace("textures/gui/advancements/backgrounds/stone.png"),
                        AdvancementType.TASK,
                        false, false, false
                )
                .addCriterion("has_bullet", InventoryChangeTrigger.TriggerInstance.hasItems(ItemRegistry.BULLET.get()))
                .save(saver, id("root"));
        AdvancementHolder craftBench = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        ModBlocks.GUNSMITH_BENCH.get(),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.craft_bench.title"),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.craft_bench.desc"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_bench", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.GUNSMITH_BENCH.get()))
                .save(saver, id("craft_gunsmith_bench"));
        AdvancementHolder craftPouch = Advancement.Builder.advancement()
                .parent(craftBench)
                .display(
                        ModItems.AMMO_POUCH.get(),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.craft_pouch.title"),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.craft_pouch.desc"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_pouch", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.AMMO_POUCH.get()))
                .save(saver, id("craft_ammo_pouch"));
        AdvancementHolder specialAmmo = Advancement.Builder.advancement()
                .parent(craftPouch)
                .display(
                        ModItems.SILVER_BULLET.get(),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.special_ammo.title"),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.special_ammo.desc"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_special_ammo", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(itemLookup,
                                ModItems.SILVER_BULLET.get(), ModItems.AP_BULLET.get(), ModItems.GLASS_BULLET.get(), ModItems.TRACER_BULLET.get()
                        )
                ))
                .save(saver, id("special_ammo"));
        AdvancementHolder expandChassis = Advancement.Builder.advancement()
                .parent(craftBench)
                .display(
                        ModItems.GUNSMITH_CHASSIS_KIT.get(),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.expand_chassis.title"),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.expand_chassis.desc"),
                        null,
                        AdvancementType.GOAL,
                        true, true, false
                )
                .addCriterion("has_kit", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.GUNSMITH_CHASSIS_KIT.get()))
                .save(saver, id("expand_chassis"));
        AdvancementHolder findAltar = Advancement.Builder.advancement()
                .parent(root)
                .display(
                        ModBlocks.CURSED_ALTAR.get(),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.find_altar.title"),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.find_altar.desc"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_altar", InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.CURSED_ALTAR.get()))
                .save(saver, id("find_cursed_altar"));
        AdvancementHolder getOmenCore = Advancement.Builder.advancement()
                .parent(findAltar)
                .display(
                        ModItems.OMINOUS_CLOCKWORK_CORE.get(),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.get_omen_core.title"),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.get_omen_core.desc"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_core", InventoryChangeTrigger.TriggerInstance.hasItems(ModItems.OMINOUS_CLOCKWORK_CORE.get()))
                .save(saver, id("get_omen_core"));
        AdvancementHolder firstAltarCraft = Advancement.Builder.advancement()
                .parent(getOmenCore)
                .display(
                        ModItems.BLOODBOUND_CALAMITY_MODIFIER.get(),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.altar_craft.title"),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.altar_craft.desc"),
                        null,
                        AdvancementType.TASK,
                        true, true, false
                )
                .addCriterion("has_forbidden_mod", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(itemLookup,
                                ModItems.BLOODBOUND_CALAMITY_MODIFIER.get(), ModItems.UNSTABLE_OVERCLOCK_MODIFIER.get(),
                                ModItems.REAPERS_GAMBIT_MODIFIER.get(), ModItems.TRIAL_OF_GREED_MODIFIER.get()
                        )
                ))
                .save(saver, id("first_altar_craft"));
        AdvancementHolder tier3Refined = Advancement.Builder.advancement()
                .parent(firstAltarCraft)
                .display(
                        ModItems.CRIMSON_SINGULARITY_MODIFIER.get(),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.tier3_refined.title"),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.tier3_refined.desc"),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, false
                )
                .addCriterion("has_refined_mod", InventoryChangeTrigger.TriggerInstance.hasItems(
                        ItemPredicate.Builder.item().of(itemLookup,
                                ModItems.CRIMSON_SINGULARITY_MODIFIER.get(), ModItems.CLOCKWORK_GATLING_MODIFIER.get(),
                                ModItems.REAPERS_TEMPEST_MODIFIER.get(), ModItems.MIDAS_TOUCH_CHAMBER_MODIFIER.get()
                        )
                ))
                .save(saver, id("craft_tier3_refined"));
        AdvancementHolder ruinedEnd = Advancement.Builder.advancement()
                .parent(tier3Refined)
                .display(
                        Items.DRAGON_BREATH,
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.ruined_end.title"),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.ruined_end.desc"),
                        null,
                        AdvancementType.GOAL,
                        true, true, false
                )
                .addCriterion("enter_end", ChangeDimensionTrigger.TriggerInstance.changedDimensionTo(Level.END))
                .save(saver, id("ruined_end_horizon"));

        Advancement.Builder.advancement()
                .parent(firstAltarCraft)
                .display(
                        ItemRegistry.CLOCKWORK_RIFLE.get(),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.kill_apostle.title"),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.kill_apostle.desc"),
                        null,
                        AdvancementType.GOAL,
                        true, true, false
                )

                .addCriterion("killed_apostle", KilledTrigger.TriggerInstance.playerKilledEntity(
                        EntityPredicate.Builder.entity().entityType(EntityTypePredicate.of(entityLookup, ModEntities.APOSTLE_GUN.get()))
                ))
                .save(saver, id("kill_apostle"));
        Advancement.Builder.advancement()
                .parent(ruinedEnd)
                .display(
                        ModItems.ABYSSAL_SINGULARITY_CORE.get(),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.kill_abyssal_apostle.title"),
                        Component.translatable("advancements.gunsmiths_gadgetsn_gizmos.kill_abyssal_apostle.desc"),
                        null,
                        AdvancementType.CHALLENGE,
                        true, true, false
                )

                .addCriterion("killed_abyssal_apostle", KilledTrigger.TriggerInstance.playerKilledEntity(
                        EntityPredicate.Builder.entity()
                                .entityType(EntityTypePredicate.of(entityLookup, ModEntities.APOSTLE_GUN.get()))
                                .located(LocationPredicate.Builder.inDimension(Level.END))
                ))
                .save(saver, id("kill_abyssal_apostle"));
    }
}