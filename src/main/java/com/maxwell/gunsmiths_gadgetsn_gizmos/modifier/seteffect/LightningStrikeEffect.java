package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.seteffect;

import com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy.SetBonusEffect;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.irons_artifice.entity.Bullet;
import io.redspace.irons_artifice.gun.HitEntityAccumulator;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public record LightningStrikeEffect(float chance) implements SetBonusEffect {
    public static final MapCodec<LightningStrikeEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.FLOAT.optionalFieldOf("chance", 1.0F).forGetter(LightningStrikeEffect::chance)
    ).apply(builder, LightningStrikeEffect::new));

    @Override
    public MapCodec<? extends SetBonusEffect> codec() {
        return CODEC;
    }

    @Override
    public void onHit(ServerLevel level, Bullet bullet, HitResult hitResult, HitEntityAccumulator accumulator) {
        if (level.getRandom().nextFloat() < chance) {
            Vec3 pos = hitResult.getLocation();
            LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level, net.minecraft.world.entity.EntitySpawnReason.TRIGGERED);
            if (lightning != null) {
                lightning.setPos(pos);
                level.addFreshEntity(lightning);
            }
        }
    }
}