package com.maxwell.gunsmiths_gadgetsn_gizmos.init;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModDataComponents {
    public static final DeferredRegister.DataComponents COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, GunsmithsGadgetsnGizmos.MODID);
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> EXTRA_MODIFIER_SLOTS =
            COMPONENTS.registerComponentType("extra_modifier_slots", builder -> builder
                    .persistent(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<String>> LOADED_AMMO_TYPE =
            COMPONENTS.registerComponentType("loaded_ammo_type", builder -> builder
                    .persistent(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8));

    public static void register(IEventBus bus) {
        COMPONENTS.register(bus);
    }
}