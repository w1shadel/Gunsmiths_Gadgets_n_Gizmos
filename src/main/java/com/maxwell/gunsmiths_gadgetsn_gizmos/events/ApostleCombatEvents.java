package com.maxwell.gunsmiths_gadgetsn_gizmos.events;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.ApostleGunEntity;
import io.redspace.irons_artifice.entity.Bullet;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;

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
    public static void onMinionChangeTarget(LivingChangeTargetEvent event) {
        if (event.getEntity().getPersistentData().getBoolean("apostle_minion")) {
            if (event.getNewAboutToBeSetTarget() instanceof ApostleGunEntity) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onMinionDrops(LivingDropsEvent event) {
        if (event.getEntity().getPersistentData().getBoolean("apostle_minion")) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMinionExperienceDrop(LivingExperienceDropEvent event) {
        if (event.getEntity().getPersistentData().getBoolean("apostle_minion")) {
            event.setCanceled(true);
        }
    }
}