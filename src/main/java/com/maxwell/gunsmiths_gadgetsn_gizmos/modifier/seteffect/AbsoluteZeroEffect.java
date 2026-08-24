package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.seteffect;

import com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy.SetBonusEffect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.irons_artifice.entity.Bullet;
import io.redspace.irons_artifice.gun.HitEntityAccumulator;
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

public record AbsoluteZeroEffect(double radius, int freezeDurationTicks) implements SetBonusEffect {
    public static final MapCodec<AbsoluteZeroEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.DOUBLE.optionalFieldOf("radius", 4.5).forGetter(AbsoluteZeroEffect::radius),
            Codec.INT.optionalFieldOf("freeze_duration_ticks", 20 * 5).forGetter(AbsoluteZeroEffect::freezeDurationTicks)
    ).apply(builder, AbsoluteZeroEffect::new));

    @Override
    public MapCodec<? extends SetBonusEffect> codec() {
        return CODEC;
    }

    @Override
    public void onHit(ServerLevel level, Bullet bullet, HitResult hitResult, HitEntityAccumulator accumulator) {
        Vec3 pos = hitResult.getLocation();
        AABB area = AABB.ofSize(pos, radius * 2, radius * 2, radius * 2);
        Entity owner = bullet.getOwner();
        for (Entity entity : level.getEntities(bullet, area, e -> e instanceof LivingEntity && Utils.canHarm(owner, e))) {
            LivingEntity target = (LivingEntity) entity;
            target.setTicksFrozen(target.getTicksRequiredToFreeze() * 4);
            target.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, freezeDurationTicks, 3));
            target.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, freezeDurationTicks, 3));
            accumulator.add(target);
        }
        level.playSound(null, pos.x, pos.y, pos.z,
                SoundEvents.PLAYER_HURT_FREEZE, SoundSource.PLAYERS, 2.0F, 0.5F);
        Utils.spawnParticles(level, ParticleTypes.SNOWFLAKE, pos.x, pos.y + 0.5, pos.z,
                40, 0.8, 0.8, 0.8, 0.1, true);
    }
}