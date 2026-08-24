package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.on_hit;

import io.redspace.irons_artifice.entity.Bullet;
import io.redspace.irons_artifice.modifier.PostHitEffect;
import io.redspace.irons_artifice.utils.Utils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.HitResult;

public class SilverBulletPostHit implements PostHitEffect {
    @Override
    public void postHit(ServerLevel level, Bullet bullet, HitResult hitResult, Entity entity) {
        if (entity instanceof Monster monster && Utils.canHarm(bullet.getOwner(), monster)) {
            float extraDamage = bullet.resolveDamage();
            monster.hurtServer(level, level.damageSources().magic(), extraDamage);
            level.playSound(null, monster.getX(), monster.getY(), monster.getZ(),
                    SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0F, 1.5F);
            Utils.spawnParticles(level, ParticleTypes.CRIT, monster.getX(), monster.getY() + 1.0, monster.getZ(),
                    15, 0.3, 0.5, 0.3, 0.1, true);
        }
    }
}