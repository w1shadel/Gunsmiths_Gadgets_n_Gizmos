package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.on_hit;

import io.redspace.irons_artifice.entity.Bullet;
import io.redspace.irons_artifice.modifier.PostHitEffect;
import io.redspace.irons_artifice.utils.Utils;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class OminousTrialPostHit implements PostHitEffect {
    private static final List<Holder<MobEffect>> OMINOUS_EFFECTS = List.of(
            MobEffects.SLOWNESS,
            MobEffects.POISON,
            MobEffects.WEAKNESS,
            MobEffects.LEVITATION
    );

    @Override
    public void postHit(ServerLevel level, Bullet bullet, HitResult hitResult, Entity entity) {
        if (!(entity instanceof LivingEntity target) || !Utils.canHarm(bullet.getOwner(), target)) {
            return;
        }
        RandomSource random = level.getRandom();
        Holder<MobEffect> selected = OMINOUS_EFFECTS.get(random.nextInt(OMINOUS_EFFECTS.size()));
        int duration = 20 * 6;
        int amplifier = selected == MobEffects.LEVITATION ? 0 : 1;
        target.addEffect(new MobEffectInstance(selected, duration, amplifier), bullet.getOwner());
        Vec3 pos = target.position();
        Utils.spawnParticles(level, ParticleTypes.TRIAL_OMEN, pos.x, pos.y + 1.0, pos.z,
                8, 0.3, 0.5, 0.3, 0.05, true);
    }
}