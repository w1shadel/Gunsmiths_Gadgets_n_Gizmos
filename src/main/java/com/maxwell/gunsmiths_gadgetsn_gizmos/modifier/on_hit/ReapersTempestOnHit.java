package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.on_hit;

import io.redspace.irons_artifice.entity.Bullet;
import io.redspace.irons_artifice.gun.HitEntityAccumulator;
import io.redspace.irons_artifice.modifier.OnHitEffect;
import io.redspace.irons_artifice.utils.Utils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.*;

public class ReapersTempestOnHit implements OnHitEffect {
    private static final double BURST_RADIUS = 5.0;

    @Override
    public void onHit(ServerLevel level, Bullet bullet, HitResult hitResult, HitEntityAccumulator accumulator) {
        Vec3 hitPos = hitResult.getLocation();
        Entity owner = bullet.getOwner();
        if (hitResult instanceof EntityHitResult) {
            AABB area = AABB.ofSize(hitPos, BURST_RADIUS * 2, BURST_RADIUS * 2, BURST_RADIUS * 2);
            float blastDamage = bullet.resolveDamage() * 2.5F;
            for (Entity entity : level.getEntities(bullet, area, e -> e instanceof LivingEntity && Utils.canHarm(owner, e))) {
                LivingEntity target = (LivingEntity) entity;
                target.hurtServer(level, level.damageSources().wither(), blastDamage);
                target.setDeltaMovement(target.getDeltaMovement().x * 0.3, 0.7, target.getDeltaMovement().z * 0.3);
                target.hurtMarked = true;
                accumulator.add(target);
            }
            level.playSound(null, hitPos.x, hitPos.y, hitPos.z,
                    SoundEvents.WIND_CHARGE_BURST.value(), SoundSource.PLAYERS, 2.0F, 0.7F);
            level.playSound(null, hitPos.x, hitPos.y, hitPos.z,
                    SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 2.0F, 1.5F);
            Utils.spawnParticles(level, ParticleTypes.SOUL_FIRE_FLAME, hitPos.x, hitPos.y + 0.5, hitPos.z,
                    50, 0.8, 0.8, 0.8, 0.15, true);
        } else if (hitResult instanceof BlockHitResult) {
            if (owner instanceof LivingEntity livingOwner) {
                livingOwner.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * 2, 0));
                level.playSound(null, livingOwner.getX(), livingOwner.getY(), livingOwner.getZ(),
                        SoundEvents.SOUL_ESCAPE.value(), SoundSource.PLAYERS, 0.8F, 1.8F);
            }
        }
    }
}