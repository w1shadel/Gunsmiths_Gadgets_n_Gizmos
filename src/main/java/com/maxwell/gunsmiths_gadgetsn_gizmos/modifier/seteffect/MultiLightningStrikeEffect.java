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

public record MultiLightningStrikeEffect(int strikeCount) implements SetBonusEffect {
    public static final MapCodec<MultiLightningStrikeEffect> CODEC = RecordCodecBuilder.mapCodec(builder -> builder.group(
            Codec.INT.optionalFieldOf("strike_count", 3).forGetter(MultiLightningStrikeEffect::strikeCount)
    ).apply(builder, MultiLightningStrikeEffect::new));

    @Override
    public MapCodec<? extends SetBonusEffect> codec() {
        return CODEC;
    }

    @Override
    public void onHit(ServerLevel level, Bullet bullet, HitResult hitResult, HitEntityAccumulator accumulator) {
        Vec3 pos = hitResult.getLocation();
        for (int i = 0; i < strikeCount; i++) {
            double offsetX = (level.getRandom().nextDouble() - 0.5) * 2.0;
            double offsetZ = (level.getRandom().nextDouble() - 0.5) * 2.0;
            LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level, net.minecraft.world.entity.EntitySpawnReason.TRIGGERED);
            if (bolt != null) {
                bolt.setPos(pos.x + offsetX, pos.y, pos.z + offsetZ);
                level.addFreshEntity(bolt);
            }
        }
    }
}