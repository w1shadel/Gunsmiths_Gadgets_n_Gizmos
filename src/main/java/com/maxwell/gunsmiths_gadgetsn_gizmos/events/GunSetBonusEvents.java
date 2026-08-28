package com.maxwell.gunsmiths_gadgetsn_gizmos.events;

import com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy.GunSetBonus;
import com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy.GunSetBonusManager;
import com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy.SetBonusEffect;
import io.redspace.irons_artifice.api.ComposeShotEvent;
import io.redspace.irons_artifice.api.GunShootEvent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddServerReloadListenersEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.List;

@EventBusSubscriber(modid = "gunsmiths_gadgetsn_gizmos")
public class GunSetBonusEvents {
    @SubscribeEvent
    public static void onAddServerReloadListeners(AddServerReloadListenersEvent event) {
        event.addListener(
                Identifier.fromNamespaceAndPath("gunsmiths_gadgetsn_gizmos", "gun_set_bonuses"),
                new GunSetBonusManager()
        );
    }

    @SubscribeEvent
    public static void onComposeShot(ComposeShotEvent event) {
        ItemStack gun = event.getShotProfile().itemStack();
        List<GunSetBonus> bonuses = GunSetBonusManager.getMatchingBonuses(gun);
        for (GunSetBonus bonus : bonuses) {
            bonus.apply(event.getShotProfile(), event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onGunShoot(GunShootEvent.Pre event) {
        if (event.getEntity().level() instanceof ServerLevel level) {
            LivingEntity shooter = event.getEntity();
            ItemStack gun = event.getShotProfile().itemStack();
            List<GunSetBonus> bonuses = GunSetBonusManager.getMatchingBonuses(gun);
            for (GunSetBonus bonus : bonuses) {
                for (SetBonusEffect effect : bonus.customEffects()) {
                    effect.onShoot(level, shooter, event.getShotProfile(), event.getOrigin(), event.getDirection());
                }
            }
        }
    }

    @SubscribeEvent
    public static void onKill(LivingDeathEvent event) {
        if (event.getSource().getDirectEntity() instanceof io.redspace.irons_artifice.entity.Bullet bullet) {
            if (event.getSource().getEntity() instanceof LivingEntity killer && killer.level() instanceof ServerLevel level) {
                List<GunSetBonus> bonuses = GunSetBonusManager.getMatchingBonuses(bullet.getProfile().itemStack());
                for (GunSetBonus bonus : bonuses) {
                    for (SetBonusEffect effect : bonus.customEffects()) {
                        effect.onKill(level, killer, event.getEntity(), bullet);
                    }
                }
            }
        }
    }
}