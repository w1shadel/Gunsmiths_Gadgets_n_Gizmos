package com.maxwell.gunsmiths_gadgetsn_gizmos.events;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModVillagers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.raid.Raider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID)
public class CultistAllianceEvents {
    public static boolean isCultist(LivingEntity entity) {
        if (entity instanceof Villager villager) {
            return villager.getVillagerData().profession().is(ModVillagers.CULTIST.getKey());
        }
        return false;
    }

    @SubscribeEvent
    public static void onIllagerTarget(LivingChangeTargetEvent event) {
        if (event.getEntity() instanceof Raider) {
            LivingEntity target = event.getNewAboutToBeSetTarget();
            if (target != null && isCultist(target)) {
                event.setCanceled(true);
            }
        }
    }
}