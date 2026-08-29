package com.maxwell.gunsmiths_gadgetsn_gizmos.init;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.ApostleGunEntity;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.TownMarksmanEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister.Entities ENTITY_TYPES =
            DeferredRegister.createEntities(GunsmithsGadgetsnGizmos.MODID);
    public static final DeferredHolder<EntityType<?>, EntityType<TownMarksmanEntity>> TOWN_MARKSMAN =
            ENTITY_TYPES.registerEntityType(
                    "town_marksman",
                    TownMarksmanEntity::new,
                    MobCategory.CREATURE,
                    builder -> builder.sized(0.6F, 1.95F).clientTrackingRange(48)
            );
    public static final DeferredHolder<EntityType<?>, EntityType<ApostleGunEntity>> APOSTLE_GUN =
            ENTITY_TYPES.registerEntityType(
                    "apostle_gun",
                    ApostleGunEntity::new,
                    MobCategory.CREATURE,
                    builder -> builder.sized(0.6F, 1.95F).clientTrackingRange(48)
            );

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
        bus.addListener(ModEntities::registerAttributes);
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(TOWN_MARKSMAN.get(), TownMarksmanEntity.createAttributes().build());
        event.put(APOSTLE_GUN.get(), ApostleGunEntity.createAttributes().build());
    }
}