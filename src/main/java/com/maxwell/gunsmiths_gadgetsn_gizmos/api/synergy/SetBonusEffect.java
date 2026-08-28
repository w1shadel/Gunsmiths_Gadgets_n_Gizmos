package com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy;

import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModSetBonusEffects;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import io.redspace.irons_artifice.entity.Bullet;
import io.redspace.irons_artifice.gun.HitEntityAccumulator;
import io.redspace.irons_artifice.gun.ShotProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.function.Function;

public interface SetBonusEffect {
    Codec<SetBonusEffect> CODEC = ModSetBonusEffects.REGISTRY.byNameCodec()
            .dispatch(SetBonusEffect::codec, Function.identity());

    MapCodec<? extends SetBonusEffect> codec();

    default void onCompose(ShotProfile profile, LivingEntity shooter) {
    }

    default void onShoot(ServerLevel level, LivingEntity shooter, ShotProfile profile, Vec3 origin, Vec3 direction) {
    }

    default void onHit(ServerLevel level, Bullet bullet, HitResult hitResult, HitEntityAccumulator accumulator) {
    }

    default void onKill(ServerLevel level, LivingEntity killer, LivingEntity victim, Bullet bullet) {
    }
}