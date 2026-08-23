package com.maxwell.gunsmiths_gadgetsn_gizmos.registry;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.block.alter.CursedAltarBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, GunsmithsGadgetsnGizmos.MODID);

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CursedAltarBlockEntity>> CURSED_ALTAR_BE =
            BLOCK_ENTITIES.register("cursed_altar",
                    () -> new BlockEntityType<>(CursedAltarBlockEntity::new, ModBlocks.CURSED_ALTAR.get())
            );

}