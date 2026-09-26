package com.maxwell.gunsmiths_gadgetsn_gizmos.init;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.ApostleGunEntity;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.TownMarksmanEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(Registries.ENTITY_TYPE, GunsmithsGadgetsnGizmos.MODID);
    public static final DeferredHolder<EntityType<?>, EntityType<TownMarksmanEntity>> TOWN_MARKSMAN =
            ENTITIES.register("town_marksman",
                    () -> EntityType.Builder.of(TownMarksmanEntity::new, MobCategory.MISC)
                            .sized(0.6F, 1.95F)
                            .clientTrackingRange(48)
                            .build("town_marksman"));
    public static final DeferredHolder<EntityType<?>, EntityType<ApostleGunEntity>> APOSTLE_GUN =
            ENTITIES.register("apostle_gun",
                    () -> EntityType.Builder.of(ApostleGunEntity::new, MobCategory.MONSTER)
                            .sized(0.6F, 1.95F)
                            .clientTrackingRange(48)
                            .build("apostle_gun"));

    public static void register(IEventBus bus) {
        ENTITIES.register(bus);
        bus.addListener(ModEntities::registerAttributes);
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(TOWN_MARKSMAN.get(), TownMarksmanEntity.createAttributes().build());
        event.put(APOSTLE_GUN.get(), ApostleGunEntity.createAttributes().build());
    }
}