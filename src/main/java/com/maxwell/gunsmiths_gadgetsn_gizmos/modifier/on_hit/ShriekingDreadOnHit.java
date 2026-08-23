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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ShriekingDreadOnHit implements OnHitEffect {
    private static final double SHRIEK_RADIUS = 6.0;

    @Override
    public void onHit(ServerLevel level, Bullet bullet, HitResult hitResult, HitEntityAccumulator accumulator) {
        Vec3 hitPos = hitResult.getLocation();
        AABB area = AABB.ofSize(hitPos, SHRIEK_RADIUS * 2, SHRIEK_RADIUS * 2, SHRIEK_RADIUS * 2);
        Entity owner = bullet.getOwner();
        for (Entity entity : level.getEntities(bullet, area, e -> e instanceof LivingEntity && Utils.canHarm(owner, e))) {
            LivingEntity target = (LivingEntity) entity;
            target.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20 * 5, 0));
            target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 20 * 5, 1));
            target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20 * 5, 1));
            accumulator.add(target);
        }
        level.playSound(null, hitPos.x, hitPos.y, hitPos.z,
                SoundEvents.SCULK_SHRIEKER_SHRIEK, SoundSource.PLAYERS, 2.0F, 0.8F);
        Utils.spawnParticles(level, ParticleTypes.SPLASH, hitPos.x, hitPos.y + 0.5, hitPos.z,
                1, 0, 0, 0, 0, true);
    }
}