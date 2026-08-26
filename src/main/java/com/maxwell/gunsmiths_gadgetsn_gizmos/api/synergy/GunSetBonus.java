package com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.redspace.irons_artifice.client.particle.ColorTransitionParticleOption;
import io.redspace.irons_artifice.data.ShotComponentMap;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.data.ValueModifier;
import io.redspace.irons_artifice.gun.ShotProfile;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public record GunSetBonus(
        String nameKey,
        String descKey,
        List<Identifier> requiredModifiers,
        BonusStats bonuses,
        List<SetBonusEffect> customEffects
) {
    public static final Codec<GunSetBonus> CODEC = RecordCodecBuilder.create(builder -> builder.group(
            Codec.STRING.fieldOf("name").forGetter(GunSetBonus::nameKey),
            Codec.STRING.fieldOf("description").forGetter(GunSetBonus::descKey),
            Identifier.CODEC.listOf().fieldOf("required_modifiers").forGetter(GunSetBonus::requiredModifiers),
            BonusStats.CODEC.optionalFieldOf("bonuses", BonusStats.EMPTY).forGetter(GunSetBonus::bonuses),
            SetBonusEffect.CODEC.listOf().optionalFieldOf("custom_effects", List.of()).forGetter(GunSetBonus::customEffects)
    ).apply(builder, GunSetBonus::new));

    public boolean matches(List<ItemStack> installedModifiers) {
        Set<Identifier> installedIds = installedModifiers.stream()
                .filter(stack -> !stack.isEmpty())
                .map(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem()))
                .collect(Collectors.toSet());

        return installedIds.containsAll(requiredModifiers);
    }

    public void apply(ShotProfile profile, net.minecraft.world.entity.LivingEntity shooter) {
        ShotComponentMap map = profile.components();

        if (bonuses.damageMultiplier != 0) {
            map.getOrCreate(ShotComponents.DAMAGE).addModifier(
                    new ValueModifier(bonuses.damageMultiplier, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL)
            );
        }
        if (bonuses.bulletSpeedMultiplier != 0) {
            map.getOrCreate(ShotComponents.BULLET_SPEED).addModifier(
                    new ValueModifier(bonuses.bulletSpeedMultiplier, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL)
            );
        }
        if (bonuses.recoilMultiplier != 0) {
            map.getOrCreate(ShotComponents.CAMERA_RECOIL_MULTIPLIER).addModifier(
                    new ValueModifier(bonuses.recoilMultiplier, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL)
            );
        }
        if (bonuses.spreadAdd != 0) {
            map.getOrCreate(ShotComponents.SPREAD).addModifier(
                    new ValueModifier(bonuses.spreadAdd, ValueModifier.Operation.ADD, ValueModifier.Type.BENEFICIAL)
            );
        }
        if (bonuses.piercingAdd != 0) {
            map.getOrCreate(ShotComponents.PIERCING).addModifier(
                    new ValueModifier(bonuses.piercingAdd, ValueModifier.Operation.ADD, ValueModifier.Type.BENEFICIAL)
            );
        }

        bonuses.trailColor().ifPresent(colorHex -> {
            try {
                int color = (int) Long.parseLong(colorHex.replace("#", ""), 16);
                map.getOrCreate(ShotComponents.PARTICLE_TRAIL).add(ColorTransitionParticleOption.bulletTrail(color, 0x000000));
            } catch (Exception ignored) {}
        });

        bonuses.muzzleFlashColor().ifPresent(colorHex -> {
            try {
                int color = (int) Long.parseLong(colorHex.replace("#", ""), 16);
                map.getOrCreate(ShotComponents.MUZZLE_FLASH).addTint(color);
            } catch (Exception ignored) {}
        });

        for (SetBonusEffect effect : customEffects) {
            effect.onCompose(profile, shooter);
            map.getOrCreate(ShotComponents.ON_HIT).add((level, bullet, hitResult, accumulator) -> {
                effect.onHit(level, bullet, hitResult, accumulator);
            });
        }
    }

    public record BonusStats(
            double damageMultiplier,
            double bulletSpeedMultiplier,
            double recoilMultiplier,
            double spreadAdd,
            int piercingAdd,
            Optional<String> trailColor,
            Optional<String> muzzleFlashColor,
            Optional<ParticleOptions> auraParticle 
    ) {
        public static final BonusStats EMPTY = new BonusStats(0, 0, 0, 0, 0, Optional.empty(), Optional.empty(), Optional.empty());

        public static final Codec<BonusStats> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                Codec.DOUBLE.optionalFieldOf("damage_multiplier", 0.0).forGetter(BonusStats::damageMultiplier),
                Codec.DOUBLE.optionalFieldOf("bullet_speed_multiplier", 0.0).forGetter(BonusStats::bulletSpeedMultiplier),
                Codec.DOUBLE.optionalFieldOf("recoil_multiplier", 0.0).forGetter(BonusStats::recoilMultiplier),
                Codec.DOUBLE.optionalFieldOf("spread_add", 0.0).forGetter(BonusStats::spreadAdd),
                Codec.INT.optionalFieldOf("piercing_add", 0).forGetter(BonusStats::piercingAdd),
                Codec.STRING.optionalFieldOf("trail_color").forGetter(BonusStats::trailColor),
                Codec.STRING.optionalFieldOf("muzzle_flash_color").forGetter(BonusStats::muzzleFlashColor),

                ParticleTypes.CODEC.optionalFieldOf("aura_particle").forGetter(BonusStats::auraParticle)
        ).apply(builder, BonusStats::new));
    }
}