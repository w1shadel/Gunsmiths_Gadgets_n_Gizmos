package com.maxwell.gunsmiths_gadgetsn_gizmos.init;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import io.redspace.irons_artifice.IronsArtifice;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GunsmithsGadgetsnGizmos.MODID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB =
            CREATIVE_MODE_TABS.register("main", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup." + GunsmithsGadgetsnGizmos.MODID))
                    .icon(() -> new ItemStack(ModItems.PISTON_RAMROD_MODIFIER.get()))
                    .withTabsBefore(IronsArtifice.CREATIVE_TAB.getKey())
                    .displayItems((parameters, output) -> {
                        for (var item : ModItems.ITEMS.getEntries()) {
                            output.accept(item.get());
                        }
                    })
                    .build()
            );

    public static void register(IEventBus bus) {
        CREATIVE_MODE_TABS.register(bus);
    }
}