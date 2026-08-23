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
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class CrimsonSingularityOnHit implements OnHitEffect {
    public static final float DAMAGE_PER_BLOCK = 1.0F;

    @Override
    public void onHit(ServerLevel level, Bullet bullet, HitResult hitResult, HitEntityAccumulator accumulator) {
        Vec3 hitPos = hitResult.getLocation();
        Entity owner = bullet.getOwner();
        double distance = owner != null ? owner.position().distanceTo(hitPos) : 0.0;
        if (distance > 3.0 && hitResult instanceof EntityHitResult entityHit) {
            Entity target = entityHit.getEntity();
            if (target instanceof LivingEntity livingTarget && Utils.canHarm(owner, target)) {
                float bonusDamage = (float) ((distance - 3.0) * DAMAGE_PER_BLOCK);
                target.hurtServer(level, DamageSources.bullet(level, bullet, owner), bonusDamage);
                accumulator.add(target);
            }
        }
        level.playSound(null, hitPos.x, hitPos.y, hitPos.z,
                SoundEvents.HEAVY_CORE_HIT, SoundSource.PLAYERS, 2.0F, 0.5F);
        Utils.spawnParticles(level, ParticleTypes.CRIMSON_SPORE, hitPos.x, hitPos.y + 0.5, hitPos.z,
                30, 0.4, 0.4, 0.4, 0.1, true);
    }
}