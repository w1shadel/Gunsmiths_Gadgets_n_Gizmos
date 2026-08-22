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
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class TownBellFlareOnHit implements OnHitEffect {
    private static final double FLARE_RADIUS = 32.0;
    private static final int GLOW_DURATION_TICKS = 20 * 8;

    @Override
    public void onHit(ServerLevel level, Bullet bullet, HitResult hitResult, HitEntityAccumulator accumulator) {
        Vec3 center = hitResult.getLocation();
        AABB searchArea = AABB.ofSize(center, FLARE_RADIUS * 2, FLARE_RADIUS * 2, FLARE_RADIUS * 2);
        for (Entity entity : level.getEntities(bullet, searchArea, e -> e instanceof Enemy || e instanceof Raider)) {
            if (entity instanceof LivingEntity enemy && enemy.isAlive()) {
                enemy.addEffect(new MobEffectInstance(MobEffects.GLOWING, GLOW_DURATION_TICKS, 0));
                accumulator.add(enemy);
            }
        }
        for (Entity entity : level.getEntities(bullet, searchArea, e -> e instanceof AbstractVillager)) {
            if (entity instanceof AbstractVillager villager && villager.isAlive()) {
                villager.getBrain().setMemory(MemoryModuleType.HEARD_BELL_TIME, level.getGameTime());
            }
        }
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.BELL_BLOCK, SoundSource.BLOCKS, 2.0f, 1.0f);
        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.FIREWORK_ROCKET_BLAST_FAR, SoundSource.PLAYERS, 1.5f, 1.2f);
        Utils.spawnParticles(level, ParticleTypes.ELECTRIC_SPARK, center.x, center.y + 0.5, center.z,
                1, 0, 0, 0, 0, true);
        Utils.spawnParticles(level, ParticleTypes.FIREWORK, center.x, center.y + 0.5, center.z,
                25, 0.4, 0.4, 0.4, 0.1, true);
        Utils.spawnParticles(level, ParticleTypes.GLOW, center.x, center.y + 0.5, center.z,
                30, 0.8, 0.8, 0.8, 0.05, true);
    }
}
