package com.maxwell.gunsmiths_gadgetsn_gizmos;

import com.maxwell.gunsmiths_gadgetsn_gizmos.init.*;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(GunsmithsGadgetsnGizmos.MODID)
public class GunsmithsGadgetsnGizmos {
    public static final String MODID = "gunsmiths_gadgetsn_gizmos";
    public static final Logger LOGGER = LogUtils.getLogger();

    public GunsmithsGadgetsnGizmos(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModDataComponents.register(modEventBus);
        ModRecipes.register(modEventBus);
        ModMobEffects.register(modEventBus);
        modEventBus.addListener(ModPayloads::register);
        ModEntities.register(modEventBus);
        ModAmmoTypes.register(modEventBus);
        ModSetBonusEffects.register(modEventBus);
    }
}
