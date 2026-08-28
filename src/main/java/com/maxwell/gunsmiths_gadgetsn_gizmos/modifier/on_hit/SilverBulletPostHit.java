package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.on_hit;

import io.redspace.irons_artifice.damage.DamageSources;
import io.redspace.irons_artifice.entity.Bullet;
import io.redspace.irons_artifice.modifier.PostHitEffect;
import io.redspace.irons_artifice.utils.Utils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.HitResult;

public class SilverBulletPostHit implements PostHitEffect {
    @Override
    public void postHit(ServerLevel level, Bullet bullet, HitResult hitResult, Entity entity) {
        if (entity instanceof LivingEntity target) {
            boolean isUndeadOrMonster = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(target.getType()).is(EntityTypeTags.UNDEAD)
                    || target instanceof Monster;
            if (isUndeadOrMonster) {
                float extraDamage = bullet.resolveDamage();
                target.hurtServer(level, DamageSources.bullet(level, bullet, bullet.getOwner()), extraDamage);
                level.playSound(null, target.getX(), target.getY(), target.getZ(),
                        SoundEvents.PLAYER_ATTACK_CRIT, SoundSource.PLAYERS, 1.0F, 1.5F);
                Utils.spawnParticles(level, ParticleTypes.END_ROD,
                        target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ(),
                        10, 0.2, 0.3, 0.2, 0.05, true);
            }
        }
    }
}