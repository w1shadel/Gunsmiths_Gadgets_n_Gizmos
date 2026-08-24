package com.maxwell.gunsmiths_gadgetsn_gizmos.api.detagen;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID)
public final class DataGenerators {
    private DataGenerators() {
    }

    @SubscribeEvent
    public static void gatherClientData(GatherDataEvent.Client event) {
        event.createProvider((output, lookup) -> new VillagerTradeDataProvider(output, GunsmithsGadgetsnGizmos.MODID));
        event.createProvider((output, lookup) -> new CursedAltarRecipeDataProvider(output, GunsmithsGadgetsnGizmos.MODID));
        event.createProvider(CuriosTagDataProvider::new);
        event.createProvider(CuriosSlotDataProvider::new);
        event.createProvider((output, lookup) -> new GunSetBonusDataProvider(output, GunsmithsGadgetsnGizmos.MODID));
        event.createProvider((out, lookup) -> new VanillaCraftingRecipeDataProvider(out, GunsmithsGadgetsnGizmos.MODID));
        event.createProvider((out, lookup) -> new ModItemModelDataProvider(out, GunsmithsGadgetsnGizmos.MODID));
    }

    @SubscribeEvent
    public static void gatherServerData(GatherDataEvent.Server event) {
        event.createProvider((output, lookup) -> new VillagerTradeDataProvider(output, GunsmithsGadgetsnGizmos.MODID));
        event.createProvider((output, lookup) -> new CursedAltarRecipeDataProvider(output, GunsmithsGadgetsnGizmos.MODID));
        event.createProvider(CuriosTagDataProvider::new);
        event.createProvider(CuriosSlotDataProvider::new);
        event.createProvider((output, lookup) -> new GunSetBonusDataProvider(output, GunsmithsGadgetsnGizmos.MODID));
        event.createProvider((out, lookup) -> new VanillaCraftingRecipeDataProvider(out, GunsmithsGadgetsnGizmos.MODID));
        event.createProvider((out, lookup) -> new ModItemModelDataProvider(out, GunsmithsGadgetsnGizmos.MODID));
    }
}