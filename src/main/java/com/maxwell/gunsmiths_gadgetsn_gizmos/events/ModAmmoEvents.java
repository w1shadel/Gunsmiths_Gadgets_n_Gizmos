package com.maxwell.gunsmiths_gadgetsn_gizmos.events;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.api.ammo.AmmoType;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModAmmoTypes;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModDataComponents;
import io.redspace.irons_artifice.api.ComposeShotEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID)
public class ModAmmoEvents {
    @SubscribeEvent
    public static void onComposeShot(ComposeShotEvent event) {
        ItemStack gun = event.getShotProfile().itemStack();
        String ammoId = gun.get(ModDataComponents.LOADED_AMMO_TYPE.get());
        if (ammoId != null) {
            Identifier id = Identifier.tryParse(ammoId);
            if (id != null) {
                AmmoType type = ModAmmoTypes.REGISTRY.getValue(id);
                if (type != null) {
                    type.applyToShot(event.getShotProfile(), event.getEntity());
                }
            }
        }
    }
}