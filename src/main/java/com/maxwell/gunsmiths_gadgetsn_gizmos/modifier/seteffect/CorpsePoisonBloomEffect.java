package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.seteffect;

import com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy.SetBonusEffect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.irons_artifice.entity.Bullet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;

public record CorpsePoisonBloomEffect(float radius, int duration) implements SetBonusEffect {
    public static final MapCodec<CorpsePoisonBloomEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.FLOAT.optionalFieldOf("radius", 3.5F).forGetter(CorpsePoisonBloomEffect::radius),
            Codec.INT.optionalFieldOf("duration", 20 * 6).forGetter(CorpsePoisonBloomEffect::duration)
    ).apply(builder, CorpsePoisonBloomEffect::new));

    @Override
    public MapCodec<? extends SetBonusEffect> codec() {
        return CODEC;
    }

    @Override
    public void onKill(ServerLevel level, LivingEntity killer, LivingEntity victim, Bullet bullet) {
        AreaEffectCloud cloud = new AreaEffectCloud(level, victim.getX(), victim.getY(), victim.getZ());
        cloud.setOwner(killer);
        cloud.setRadius(radius);
        cloud.setDuration(duration);
        cloud.setRadiusPerTick(-radius / duration);
        cloud.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * 4, 1));
        cloud.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20 * 4, 0));
        level.addFreshEntity(cloud);
        level.playSound(null, victim.getX(), victim.getY(), victim.getZ(),
                SoundEvents.PUFFER_FISH_BLOW_OUT, SoundSource.PLAYERS, 1.5F, 0.7F);
    }
}