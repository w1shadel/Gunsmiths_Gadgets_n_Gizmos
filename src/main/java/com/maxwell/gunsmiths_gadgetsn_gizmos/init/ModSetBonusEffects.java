package com.maxwell.gunsmiths_gadgetsn_gizmos.init;

import com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy.SetBonusEffect;
import com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.seteffect.*;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSetBonusEffects {
    public static final ResourceKey<Registry<MapCodec<? extends SetBonusEffect>>> REGISTRY_KEY =
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath("gunsmiths_gadgetsn_gizmos", "set_bonus_effect"));
    public static final DeferredRegister<MapCodec<? extends SetBonusEffect>> EFFECTS =
            DeferredRegister.create(REGISTRY_KEY, "gunsmiths_gadgetsn_gizmos");
    public static final Registry<MapCodec<? extends SetBonusEffect>> REGISTRY = EFFECTS.makeRegistry(builder -> {
    });
    public static final DeferredHolder<MapCodec<? extends SetBonusEffect>, MapCodec<LightningStrikeEffect>> LIGHTNING_STRIKE =
            EFFECTS.register("lightning_strike", () -> LightningStrikeEffect.CODEC);
    public static final DeferredHolder<MapCodec<? extends SetBonusEffect>, MapCodec<AbsoluteZeroEffect>> ABSOLUTE_ZERO =
            EFFECTS.register("absolute_zero", () -> AbsoluteZeroEffect.CODEC);
    public static final DeferredHolder<MapCodec<? extends SetBonusEffect>, MapCodec<CorpsePoisonBloomEffect>> CORPSE_POISON_BLOOM =
            EFFECTS.register("corpse_poison_bloom", () -> CorpsePoisonBloomEffect.CODEC);
    public static final DeferredHolder<MapCodec<? extends SetBonusEffect>, MapCodec<GravitationalCollapseEffect>> GRAVITATIONAL_COLLAPSE =
            EFFECTS.register("gravitational_collapse", () -> GravitationalCollapseEffect.CODEC);
    public static final DeferredHolder<MapCodec<? extends SetBonusEffect>, MapCodec<MultiLightningStrikeEffect>> MULTI_LIGHTNING =
            EFFECTS.register("multi_lightning", () -> MultiLightningStrikeEffect.CODEC);
    public static final DeferredHolder<MapCodec<? extends SetBonusEffect>, MapCodec<SoulOfEternityEffect>> SOUL_OF_ETERNITY =
            EFFECTS.register("soul_of_eternity", () -> SoulOfEternityEffect.CODEC);

    public static void register(IEventBus bus) {
        EFFECTS.register(bus);
    }
}