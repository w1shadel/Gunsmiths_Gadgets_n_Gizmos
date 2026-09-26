package com.maxwell.gunsmiths_gadgetsn_gizmos.api.detagen;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;
@SuppressWarnings("removal")
@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class DataGenerators {
    private DataGenerators() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        boolean runClient = event.includeClient();
        generator.addProvider(runClient, new ModBlockStateDataProvider(packOutput, GunsmithsGadgetsnGizmos.MODID));
        generator.addProvider(runClient, new ModItemModelDataProvider(packOutput, GunsmithsGadgetsnGizmos.MODID));

        boolean runServer = event.includeServer();
        ModBlockTagsProvider blockTagsProvider = new ModBlockTagsProvider(packOutput, lookupProvider);
        generator.addProvider(runServer, blockTagsProvider);

        generator.addProvider(runServer, ModLootTableProvider.create(packOutput, lookupProvider));
        generator.addProvider(runServer, new VanillaModRecipeProvider(packOutput, lookupProvider));
        generator.addProvider(runServer, new AdvancementProvider(packOutput, lookupProvider, List.of(new ModAdvancementProvider())));


        generator.addProvider(runServer, new CursedAltarRecipeDataProvider(packOutput, GunsmithsGadgetsnGizmos.MODID));
        generator.addProvider(runServer, new CuriosTagDataProvider(packOutput, lookupProvider));
        generator.addProvider(runServer, new CuriosSlotDataProvider(packOutput, lookupProvider));
        generator.addProvider(runServer, new GunSetBonusDataProvider(packOutput, GunsmithsGadgetsnGizmos.MODID));
    }
}