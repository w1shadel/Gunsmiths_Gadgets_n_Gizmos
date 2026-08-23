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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EchoingSonicCoreOnHit implements OnHitEffect {
    private static final double PENETRATION_RANGE = 8.0;

    @Override
    public void onHit(ServerLevel level, Bullet bullet, HitResult hitResult, HitEntityAccumulator accumulator) {
        Vec3 hitPos = hitResult.getLocation();
        Vec3 direction = bullet.getDeltaMovement().normalize();
        Entity owner = bullet.getOwner();
        Vec3 endPos = hitPos.add(direction.scale(PENETRATION_RANGE));
        AABB scanArea = new AABB(hitPos, endPos).inflate(1.5);
        float sonicDamage = bullet.resolveDamage() * 1.5F;
        for (Entity entity : level.getEntities(bullet, scanArea, e -> e instanceof LivingEntity && Utils.canHarm(owner, e))) {
            LivingEntity living = (LivingEntity) entity;
            living.hurtServer(level, DamageSources.bullet(level, bullet, owner), sonicDamage);
            living.knockback(1.2, -direction.x, -direction.z);
            accumulator.add(living);
        }
        level.playSound(null, hitPos.x, hitPos.y, hitPos.z,
                SoundEvents.WARDEN_SONIC_BOOM, SoundSource.PLAYERS, 2.0F, 1.0F);
        int steps = 16;
        for (int i = 0; i < steps; i++) {
            Vec3 p = hitPos.lerp(endPos, (float) i / steps);
            Utils.spawnParticles(level, ParticleTypes.SONIC_BOOM, p.x, p.y, p.z, 1, 0, 0, 0, 0, true);
        }
    }
}