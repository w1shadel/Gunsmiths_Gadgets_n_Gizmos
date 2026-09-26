package com.maxwell.gunsmiths_gadgetsn_gizmos.init;

import com.google.common.collect.ImmutableSet;
import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModVillagers {
    public static final String MODID = GunsmithsGadgetsnGizmos.MODID;

    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, MODID);

    public static final DeferredHolder<PoiType, PoiType> GUNSMITH_POI = POI_TYPES.register("gunsmith",
            () -> new PoiType(
                    ImmutableSet.copyOf(ModBlocks.GUNSMITH_BENCH.get().getStateDefinition().getPossibleStates()),
                    1, 1
            )
    );

    public static final DeferredHolder<PoiType, PoiType> CULTIST_POI = POI_TYPES.register("cultist",
            () -> new PoiType(
                    ImmutableSet.copyOf(ModBlocks.CURSED_ALTAR.get().getStateDefinition().getPossibleStates()),
                    1, 1
            )
    );

    public static final DeferredRegister<VillagerProfession> PROFESSIONS =
            DeferredRegister.create(Registries.VILLAGER_PROFESSION, MODID);

    public static final DeferredHolder<VillagerProfession, VillagerProfession> GUNSMITH = PROFESSIONS.register("gunsmith",
            () -> new VillagerProfession(
                    "gunsmith",
                    holder -> holder.is(GUNSMITH_POI.getKey()),
                    holder -> holder.is(GUNSMITH_POI.getKey()),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    SoundEvents.ANVIL_USE
            )
    );

    public static final DeferredHolder<VillagerProfession, VillagerProfession> CULTIST = PROFESSIONS.register("cultist",
            () -> new VillagerProfession(
                    "cultist",
                    holder -> holder.is(CULTIST_POI.getKey()),
                    holder -> holder.is(CULTIST_POI.getKey()),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    SoundEvents.SOUL_ESCAPE.value()
            )
    );

    public static void register(IEventBus bus) {
        POI_TYPES.register(bus);
        PROFESSIONS.register(bus);
    }
}