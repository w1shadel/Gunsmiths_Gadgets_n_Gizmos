package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.on_hit;

import io.redspace.irons_artifice.damage.DamageSources;
import io.redspace.irons_artifice.entity.Bullet;
import io.redspace.irons_artifice.gun.HitEntityAccumulator;
import io.redspace.irons_artifice.modifier.OnHitEffect;
import io.redspace.irons_artifice.utils.Utils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class HeavyCoreImpactOnHit implements OnHitEffect {
    public static final float DAMAGE_PER_BLOCK = 0.5f;
    public static final double MIN_DISTANCE = 5.0;

    @Override
    public void onHit(ServerLevel level, Bullet bullet, HitResult hitResult, HitEntityAccumulator accumulator) {
        Vec3 hitPos = hitResult.getLocation();
        Entity owner = bullet.getOwner();
        double distance = owner != null ? owner.position().distanceTo(hitPos) : 0.0;
        if (distance > MIN_DISTANCE) {
            float bonusDamage = (float) ((distance - MIN_DISTANCE) * DAMAGE_PER_BLOCK);
            if (hitResult instanceof EntityHitResult entityHit) {
                Entity target = entityHit.getEntity();
                if (target instanceof LivingEntity livingTarget && Utils.canHarm(owner, target)) {
                    DamageSource source = DamageSources.bullet(level, bullet, owner);
                    target.hurtServer(level, source, bonusDamage);
                    accumulator.add(target);
                }
            }
            float volume = Math.min(2.0f, (float) (distance / 20.0f) + 0.5f);
            level.playSound(null, hitPos.x, hitPos.y, hitPos.z,
                    SoundEvents.HEAVY_CORE_FALL, SoundSource.PLAYERS, volume, 0.8f);
            level.playSound(null, hitPos.x, hitPos.y, hitPos.z,
                    SoundEvents.MACE_SMASH_GROUND_HEAVY, SoundSource.PLAYERS, volume, 1.2f);
            Utils.spawnParticles(level, ParticleTypes.CRIT, hitPos.x, hitPos.y + 0.2, hitPos.z,
                    (int) Math.min(30, distance), 0.3, 0.3, 0.3, 0.15, true);
        }
    }
}