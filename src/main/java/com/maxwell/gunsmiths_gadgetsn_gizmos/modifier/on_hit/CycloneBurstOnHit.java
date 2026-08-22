package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.on_hit;

import io.redspace.irons_artifice.entity.Bullet;
import io.redspace.irons_artifice.gun.HitEntityAccumulator;
import io.redspace.irons_artifice.modifier.OnHitEffect;
import io.redspace.irons_artifice.utils.Utils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class CycloneBurstOnHit implements OnHitEffect {
    private static final double BURST_RADIUS = 3.5;
    private static final double UPWARD_FORCE = 0.65;

    @Override
    public void onHit(ServerLevel level, Bullet bullet, HitResult hitResult, HitEntityAccumulator accumulator) {
        Vec3 center = hitResult.getLocation();
        AABB area = AABB.ofSize(center, BURST_RADIUS * 2, BURST_RADIUS * 2, BURST_RADIUS * 2);
        Entity owner = bullet.getOwner();
        for (Entity entity : level.getEntities(bullet, area, e -> e instanceof LivingEntity && Utils.canHarm(owner, e))) {
            LivingEntity living = (LivingEntity) entity;
            double distSq = living.position().distanceToSqr(center);
            if (distSq <= BURST_RADIUS * BURST_RADIUS) {
                Vec3 currentDelta = living.getDeltaMovement();
                living.setDeltaMovement(currentDelta.x * 0.5, UPWARD_FORCE, currentDelta.z * 0.5);
                living.hurtMarked = true;
                accumulator.add(living);
            }
        }
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.WIND_CHARGE_BURST.value(), SoundSource.PLAYERS, 1.5f, 1.0f);
        Utils.spawnParticles(level, ParticleTypes.GUST_EMITTER_LARGE, center.x, center.y, center.z,
                1, 0, 0, 0, 0, true);
    }
}