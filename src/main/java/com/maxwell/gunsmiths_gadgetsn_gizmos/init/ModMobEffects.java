package com.maxwell.gunsmiths_gadgetsn_gizmos.init;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.effect.BleedEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(Registries.MOB_EFFECT, GunsmithsGadgetsnGizmos.MODID);
    public static final DeferredHolder<MobEffect, BleedEffect> BLEEDING =
            MOB_EFFECTS.register("bleeding", BleedEffect::new);

    public static void register(IEventBus bus) {
        MOB_EFFECTS.register(bus);
    }
}