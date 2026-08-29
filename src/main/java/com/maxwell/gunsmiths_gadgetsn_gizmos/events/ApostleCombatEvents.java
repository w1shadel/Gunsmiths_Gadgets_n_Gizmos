package com.maxwell.gunsmiths_gadgetsn_gizmos.events;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.ApostleGunEntity;
import io.redspace.irons_artifice.entity.Bullet;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID)
public class ApostleCombatEvents {
    @SubscribeEvent
    public static void onApostleBulletHit(LivingDamageEvent.Post event) {
        if (event.getSource().getDirectEntity() instanceof Bullet bullet) {
            if (bullet.getOwner() instanceof ApostleGunEntity apostle && apostle.level() instanceof ServerLevel level) {
                apostle.getTitle().onBulletHit(event.getEntity(), apostle, level);
            }
        }
    }

    @SubscribeEvent
    public static void onMinionChangeTarget(net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent event) {
        if (event.getEntity().getPersistentData().getBooleanOr("apostle_minion", false)) {
            if (event.getNewAboutToBeSetTarget() instanceof ApostleGunEntity) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onMinionDrops(net.neoforged.neoforge.event.entity.living.LivingDropsEvent event) {
        if (event.getEntity().getPersistentData().getBooleanOr("apostle_minion", false)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMinionExperienceDrop(net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent event) {
        if (event.getEntity().getPersistentData().getBooleanOr("apostle_minion", false)) {
            event.setCanceled(true);
        }
    }
}