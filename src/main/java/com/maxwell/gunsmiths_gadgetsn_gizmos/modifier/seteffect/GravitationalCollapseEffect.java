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
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public record GravitationalCollapseEffect(double radius, float collapseDamage) implements SetBonusEffect {
    public static final MapCodec<GravitationalCollapseEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.DOUBLE.optionalFieldOf("radius", 8.0).forGetter(GravitationalCollapseEffect::radius),
            Codec.FLOAT.optionalFieldOf("collapse_damage", 15.0F).forGetter(GravitationalCollapseEffect::collapseDamage)
    ).apply(builder, GravitationalCollapseEffect::new));

    @Override
    public MapCodec<? extends SetBonusEffect> codec() {
        return CODEC;
    }

    @Override
    public void onHit(ServerLevel level, Bullet bullet, HitResult hitResult, HitEntityAccumulator accumulator) {
        Vec3 center = hitResult.getLocation();
        AABB pullArea = AABB.ofSize(center, radius * 2, radius * 2, radius * 2);
        Entity owner = bullet.getOwner();

        for (Entity entity : level.getEntities(bullet, pullArea, e -> e instanceof LivingEntity && Utils.canHarm(owner, e))) {
            LivingEntity target = (LivingEntity) entity;
            Vec3 pullVec = center.subtract(target.position());
            if (pullVec.lengthSqr() > 0.01) {
                target.setDeltaMovement(pullVec.normalize().scale(1.2));
                target.hurtMarked = true;
            }

            target.hurtServer(level, level.damageSources().wither(), collapseDamage);
            accumulator.add(target);
        }

        level.playSound(null, center.x, center.y, center.z,
                SoundEvents.END_PORTAL_SPAWN, SoundSource.PLAYERS, 2.0F, 1.8F);
        Utils.spawnParticles(level, ParticleTypes.REVERSE_PORTAL, center.x, center.y + 0.5, center.z,
                60, 0.8, 0.8, 0.8, 0.2, true);
        Utils.spawnParticles(level, ParticleTypes.EXPLOSION_EMITTER, center.x, center.y + 0.5, center.z,
                1, 0, 0, 0, 0, true);
    }
}