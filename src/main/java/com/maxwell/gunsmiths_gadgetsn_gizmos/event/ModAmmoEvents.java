package com.maxwell.gunsmiths_gadgetsn_gizmos.event;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.api.ammo.AmmoType;
import com.maxwell.gunsmiths_gadgetsn_gizmos.registry.ModAmmoTypes;
import com.maxwell.gunsmiths_gadgetsn_gizmos.registry.ModDataComponents;
import io.redspace.irons_artifice.api.ComposeShotEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

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

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent.Pre event) {
        if (event.getSource().getDirectEntity() instanceof io.redspace.irons_artifice.entity.Bullet bullet) {
            ItemStack gun = bullet.getProfile().itemStack();
            String ammoId = gun.get(ModDataComponents.LOADED_AMMO_TYPE.get());
            if ("gunsmiths_gadgetsn_gizmos:silver".equals(ammoId) && event.getEntity() instanceof Monster) {
                event.setNewDamage(event.getOriginalDamage() * 2.0F);
            }
        }
    }
}