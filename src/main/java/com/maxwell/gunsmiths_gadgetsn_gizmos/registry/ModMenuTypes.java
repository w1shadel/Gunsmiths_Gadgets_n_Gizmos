package com.maxwell.gunsmiths_gadgetsn_gizmos.registry;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.container.AmmoPouchMenu;
import com.maxwell.gunsmiths_gadgetsn_gizmos.container.CursedAltarMenu;
import com.maxwell.gunsmiths_gadgetsn_gizmos.container.GunsmithBenchMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, GunsmithsGadgetsnGizmos.MODID);

    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }    public static final DeferredHolder<MenuType<?>, MenuType<CursedAltarMenu>> CURSED_ALTAR_MENU =
            MENUS.register("cursed_altar",
                    () -> IMenuTypeExtension.create(CursedAltarMenu::new)
            );
    public static final DeferredHolder<MenuType<?>, MenuType<GunsmithBenchMenu>> GUNSMITH_BENCH_MENU =
            MENUS.register("gunsmith_bench",
                    () -> new MenuType<>(GunsmithBenchMenu::new, FeatureFlags.DEFAULT_FLAGS)
            );
    public static final DeferredHolder<MenuType<?>, MenuType<AmmoPouchMenu>> AMMO_POUCH_MENU =
            MENUS.register("ammo_pouch",
                    () -> new MenuType<>(AmmoPouchMenu::new, FeatureFlags.DEFAULT_FLAGS)
            );


}