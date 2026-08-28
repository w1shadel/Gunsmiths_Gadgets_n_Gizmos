package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.on_hit;

import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModMobEffects;
import io.redspace.irons_artifice.entity.Bullet;
import io.redspace.irons_artifice.modifier.PostHitEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.HitResult;

import java.util.List;

public class GlassBulletPostHit implements PostHitEffect {
    @Override
    public void postHit(ServerLevel level, Bullet bullet, HitResult hitResult, Entity entity) {
        if (entity instanceof LivingEntity target) {
            int unarmoredSlots = 0;
            for (EquipmentSlot slot : List.of(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET)) {
                if (target.getItemBySlot(slot).isEmpty()) {
                    unarmoredSlots++;
                }
            }
            float bleedChance = unarmoredSlots * 0.25F;
            if (target.getRandom().nextFloat() < bleedChance) {
                target.addEffect(new MobEffectInstance(ModMobEffects.BLEEDING, 20 * 6, 0), bullet.getOwner());
                level.playSound(null, target.getX(), target.getY(), target.getZ(),
                        SoundEvents.GLASS_BREAK, SoundSource.PLAYERS, 1.0F, 1.4F);
            }
        }
    }
}