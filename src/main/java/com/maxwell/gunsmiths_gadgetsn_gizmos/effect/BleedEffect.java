package com.maxwell.gunsmiths_gadgetsn_gizmos.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.NonNull;

public class BleedEffect extends MobEffect {
    public BleedEffect() {
        super(MobEffectCategory.HARMFUL, 0x8B0000);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, @NonNull LivingEntity living, int amplifier) {
        float bleedDamage = 1.5F + (amplifier * 0.75F);
        living.hurtServer(level, level.damageSources().generic(), bleedDamage);
        level.sendParticles(ParticleTypes.CRIMSON_SPORE,
                living.getX(), living.getY() + living.getBbHeight() * 0.5, living.getZ(),
                8, 0.2, 0.3, 0.2, 0.05);
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}