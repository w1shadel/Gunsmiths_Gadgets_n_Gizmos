package com.maxwell.gunsmiths_gadgetsn_gizmos.registry;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.trading.TradeSet;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModVillagers {
    public static final String MODID = "gunsmiths_gadgetsn_gizmos";
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
    public static final ResourceKey<TradeSet> GUNSMITH_LEVEL_1 = tradeSetKey("gunsmith_level_1");
    public static final ResourceKey<TradeSet> GUNSMITH_LEVEL_2 = tradeSetKey("gunsmith_level_2");
    public static final ResourceKey<TradeSet> GUNSMITH_LEVEL_3 = tradeSetKey("gunsmith_level_3");
    public static final ResourceKey<TradeSet> GUNSMITH_LEVEL_4 = tradeSetKey("gunsmith_level_4");
    public static final ResourceKey<TradeSet> GUNSMITH_LEVEL_5 = tradeSetKey("gunsmith_level_5");
    public static final DeferredHolder<VillagerProfession, VillagerProfession> GUNSMITH = PROFESSIONS.register("gunsmith",
            () -> new VillagerProfession(
                    Component.translatable("entity." + MODID + ".villager.gunsmith"),
                    holder -> holder.is(GUNSMITH_POI.getKey()),
                    holder -> holder.is(GUNSMITH_POI.getKey()),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    SoundEvents.ANVIL_USE,
                    Int2ObjectMap.ofEntries(
                            Int2ObjectMap.entry(1, GUNSMITH_LEVEL_1),
                            Int2ObjectMap.entry(2, GUNSMITH_LEVEL_2),
                            Int2ObjectMap.entry(3, GUNSMITH_LEVEL_3),
                            Int2ObjectMap.entry(4, GUNSMITH_LEVEL_4),
                            Int2ObjectMap.entry(5, GUNSMITH_LEVEL_5)
                    )
            )
    );
    public static final ResourceKey<TradeSet> CULTIST_LEVEL_1 = tradeSetKey("cultist_level_1");
    public static final ResourceKey<TradeSet> CULTIST_LEVEL_2 = tradeSetKey("cultist_level_2");
    public static final ResourceKey<TradeSet> CULTIST_LEVEL_3 = tradeSetKey("cultist_level_3");
    public static final ResourceKey<TradeSet> CULTIST_LEVEL_4 = tradeSetKey("cultist_level_4");
    public static final ResourceKey<TradeSet> CULTIST_LEVEL_5 = tradeSetKey("cultist_level_5");
    public static final DeferredHolder<VillagerProfession, VillagerProfession> CULTIST = PROFESSIONS.register("cultist",
            () -> new VillagerProfession(
                    Component.translatable("entity." + MODID + ".villager.cultist"),
                    holder -> holder.is(CULTIST_POI.getKey()),
                    holder -> holder.is(CULTIST_POI.getKey()),
                    ImmutableSet.of(),
                    ImmutableSet.of(),
                    SoundEvents.SOUL_ESCAPE.value(),
                    Int2ObjectMap.ofEntries(
                            Int2ObjectMap.entry(1, CULTIST_LEVEL_1),
                            Int2ObjectMap.entry(2, CULTIST_LEVEL_2),
                            Int2ObjectMap.entry(3, CULTIST_LEVEL_3),
                            Int2ObjectMap.entry(4, CULTIST_LEVEL_4),
                            Int2ObjectMap.entry(5, CULTIST_LEVEL_5)
                    )
            )
    );

    private static ResourceKey<TradeSet> tradeSetKey(String name) {
        return ResourceKey.create(Registries.TRADE_SET, Identifier.fromNamespaceAndPath(MODID, name));
    }

    public static void register(IEventBus bus) {
        POI_TYPES.register(bus);
        PROFESSIONS.register(bus);
    }
}