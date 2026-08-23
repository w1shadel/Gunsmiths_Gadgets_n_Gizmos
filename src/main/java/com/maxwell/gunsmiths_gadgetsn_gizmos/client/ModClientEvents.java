package com.maxwell.gunsmiths_gadgetsn_gizmos.client;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.client.gui.AmmoPouchScreen;
import com.maxwell.gunsmiths_gadgetsn_gizmos.client.gui.CursedAltarScreen;
import com.maxwell.gunsmiths_gadgetsn_gizmos.client.gui.GunsmithBenchScreen;
import com.maxwell.gunsmiths_gadgetsn_gizmos.registry.ModMenuTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.CURSED_ALTAR_MENU.get(), CursedAltarScreen::new);
        event.register(ModMenuTypes.GUNSMITH_BENCH_MENU.get(), GunsmithBenchScreen::new);
        event.register(ModMenuTypes.AMMO_POUCH_MENU.get(), AmmoPouchScreen::new);
    }
}