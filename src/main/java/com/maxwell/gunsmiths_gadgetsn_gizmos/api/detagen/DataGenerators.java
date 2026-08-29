package com.maxwell.gunsmiths_gadgetsn_gizmos.api.detagen;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import net.minecraft.data.advancements.AdvancementProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;

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
        event.createProvider(VanillaModRecipeProvider.Runner::new);
        event.createProvider((out, lookup) -> new ModBlockStateDataProvider(out, GunsmithsGadgetsnGizmos.MODID));
        event.createProvider((out, lookup) -> new ModItemModelDataProvider(out, GunsmithsGadgetsnGizmos.MODID));
        event.createProvider((output, lookup) -> new AdvancementProvider(output, lookup, List.of(new ModAdvancementProvider())));
        event.createProvider(ModLootTableProvider::create);
        event.createProvider(ModBlockTagsProvider::new);
    }

    @SubscribeEvent
    public static void gatherServerData(GatherDataEvent.Server event) {
        event.createProvider((output, lookup) -> new VillagerTradeDataProvider(output, GunsmithsGadgetsnGizmos.MODID));
        event.createProvider((output, lookup) -> new CursedAltarRecipeDataProvider(output, GunsmithsGadgetsnGizmos.MODID));
        event.createProvider(CuriosTagDataProvider::new);
        event.createProvider(CuriosSlotDataProvider::new);
        event.createProvider((output, lookup) -> new GunSetBonusDataProvider(output, GunsmithsGadgetsnGizmos.MODID));
        event.createProvider(VanillaModRecipeProvider.Runner::new);
        event.createProvider((out, lookup) -> new ModBlockStateDataProvider(out, GunsmithsGadgetsnGizmos.MODID));
        event.createProvider((out, lookup) -> new ModItemModelDataProvider(out, GunsmithsGadgetsnGizmos.MODID));
        event.createProvider((output, lookup) -> new AdvancementProvider(output, lookup, List.of(new ModAdvancementProvider())));
        event.createProvider(ModLootTableProvider::create);
        event.createProvider(ModBlockTagsProvider::new);
    }
}