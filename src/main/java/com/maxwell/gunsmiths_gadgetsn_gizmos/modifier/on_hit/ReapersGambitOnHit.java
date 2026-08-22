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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ReapersGambitOnHit implements OnHitEffect {
    @Override
    public void onHit(ServerLevel level, Bullet bullet, HitResult hitResult, HitEntityAccumulator accumulator) {
        Vec3 hitPos = hitResult.getLocation();
        Entity owner = bullet.getOwner();
        if (hitResult instanceof EntityHitResult entityHit && entityHit.getEntity() instanceof LivingEntity target) {
            float fatalDamage = bullet.resolveDamage() * 3.0F;
            target.hurtServer(level, level.damageSources().wither(), fatalDamage);
            accumulator.add(target);
            level.playSound(null, hitPos.x, hitPos.y, hitPos.z,
                    SoundEvents.WITHER_SHOOT, SoundSource.PLAYERS, 2.0F, 1.2F);
            Utils.spawnParticles(level, ParticleTypes.SOUL_FIRE_FLAME, hitPos.x, hitPos.y + 0.5, hitPos.z,
                    40, 0.5, 0.5, 0.5, 0.1, true);
        } else if (hitResult instanceof BlockHitResult) {
            if (owner instanceof LivingEntity livingOwner) {
                livingOwner.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * 5, 1));
                livingOwner.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20 * 5, 0));
                level.playSound(null, livingOwner.getX(), livingOwner.getY(), livingOwner.getZ(),
                        SoundEvents.WITHER_DEATH, SoundSource.PLAYERS, 1.0F, 1.8F);
            }
        }
    }
}