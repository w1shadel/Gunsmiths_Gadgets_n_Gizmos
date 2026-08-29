package com.maxwell.gunsmiths_gadgetsn_gizmos.entity.boss;

import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.ApostleGunEntity;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModItems;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModMobEffects;
import io.redspace.irons_artifice.registry.ItemRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public enum ApostleTitle {
    BLOOD_SEVERER("blood_severer", ChatFormatting.DARK_RED, ParticleTypes.CRIMSON_SPORE),
    TEMPEST_TORN("tempest_torn", ChatFormatting.WHITE, ParticleTypes.GUST_EMITTER_SMALL),
    SOUNDLESS_ECHO("soundless_echo", ChatFormatting.DARK_AQUA, ParticleTypes.SCULK_SOUL),
    GILDED_DESPOILER("gilded_despoiler", ChatFormatting.GOLD, ParticleTypes.GLOW),
    SOUL_REAPER("soul_reaper", ChatFormatting.DARK_PURPLE, ParticleTypes.SOUL),
    ANNIHILATION_HARBINGER("annihilation_harbinger", ChatFormatting.RED, ParticleTypes.FLAME),
    RULER_OF_ALL_CREATION("ruler_of_all_creation", ChatFormatting.GOLD, ParticleTypes.TOTEM_OF_UNDYING),
    ABYSSAL_RULER_OF_ALL_CREATION("abyssal_ruler_of_all_creation", ChatFormatting.LIGHT_PURPLE, ParticleTypes.DUST_PLUME),
    VOID_CLEFT_SOVEREIGN("void_cleft_sovereign", ChatFormatting.LIGHT_PURPLE, ParticleTypes.FLAME);
    private static final java.util.List<ApostleTitle> BASE_TITLES = java.util.List.of(
            BLOOD_SEVERER, TEMPEST_TORN, SOUNDLESS_ECHO, GILDED_DESPOILER, SOUL_REAPER
    );
    private static final java.util.List<ApostleTitle> SPECIAL_TITLES = java.util.List.of(
            ANNIHILATION_HARBINGER, VOID_CLEFT_SOVEREIGN
    );
    private final String id;
    private final ChatFormatting textColor;
    private final ParticleOptions smokeParticle;

    ApostleTitle(String id, ChatFormatting textColor, ParticleOptions smokeParticle) {
        this.id = id;
        this.textColor = textColor;
        this.smokeParticle = smokeParticle;
    }

    public static ApostleTitle random(RandomSource random) {
        ApostleTitle[] values = values();
        return values[random.nextInt(values.length)];
    }

    public static ApostleTitle selectRandom(ApostleTitle current, boolean isPhase2, boolean isLowHp, boolean isTheEnd, RandomSource random) {
        java.util.List<ApostleTitle> pool;
        if (isPhase2 && isLowHp) {
            java.util.List<ApostleTitle> shuffledBase = new java.util.ArrayList<>(BASE_TITLES);
            java.util.Collections.shuffle(shuffledBase);
            pool = new java.util.ArrayList<>();
            if (isTheEnd) {
                pool.add(RULER_OF_ALL_CREATION);
            }
            pool.add(ANNIHILATION_HARBINGER);
            pool.add(VOID_CLEFT_SOVEREIGN);
            pool.add(shuffledBase.get(0));
            if (!isTheEnd) {
                pool.add(shuffledBase.get(1));
            }
        } else {
            pool = BASE_TITLES;
        }
        java.util.List<ApostleTitle> candidates = pool.stream()
                .filter(t -> t != current)
                .toList();
        return candidates.isEmpty() ? pool.get(0) : candidates.get(random.nextInt(candidates.size()));
    }

    public static ApostleTitle randomExcept(ApostleTitle current, RandomSource random) {
        ApostleTitle next;
        do {
            next = random(random);
        } while (next == current);
        return next;
    }

    public Component getTitleComponent() {
        return Component.translatable("apostle.title." + this.id).withStyle(this.textColor);
    }

    public ParticleOptions getSmokeParticle() {
        return smokeParticle;
    }

    public void onShift(ApostleGunEntity boss, ServerLevel level) {
        if (this == ANNIHILATION_HARBINGER) {
            boss.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ModItems.MINIGUN.get()));
            level.playSound(null, boss.getX(), boss.getY(), boss.getZ(),
                    SoundEvents.IRON_GOLEM_REPAIR, SoundSource.HOSTILE, 2.0F, 0.5F);
        } else {
            if (!boss.getMainHandItem().is(ItemRegistry.CLOCKWORK_RIFLE.get())) {
                boss.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.CLOCKWORK_RIFLE.get()));
            }
        }
    }

    public void onBulletHit(LivingEntity victim, LivingEntity boss, ServerLevel level) {
        switch (this) {
            case BLOOD_SEVERER -> {
                victim.addEffect(new MobEffectInstance(ModMobEffects.BLEEDING, 20 * 6, 0), boss);
                boss.heal(2.0F);
            }
            case TEMPEST_TORN -> {
                victim.setDeltaMovement(victim.getDeltaMovement().x, 0.65, victim.getDeltaMovement().z);
                victim.hurtMarked = true;
                level.playSound(null, victim.getX(), victim.getY(), victim.getZ(),
                        SoundEvents.WIND_CHARGE_BURST.value(), SoundSource.PLAYERS, 1.5F, 1.0F);
            }
            case SOUNDLESS_ECHO -> {
                victim.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 20 * 8, 0), boss);
            }
            case GILDED_DESPOILER -> {
                if (victim instanceof Player player) {
                    player.getFoodData().setFoodLevel(Math.max(0, player.getFoodData().getFoodLevel() - 3));
                }
            }
            case SOUL_REAPER -> {
                victim.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * 5, 1), boss);
            }
            case ANNIHILATION_HARBINGER -> {
                level.sendParticles(ParticleTypes.EXPLOSION, victim.getX(), victim.getY() + 0.5, victim.getZ(), 1, 0, 0, 0, 0);
            }
            case VOID_CLEFT_SOVEREIGN -> {
                victim.hurtServer(level, level.damageSources().fellOutOfWorld(), 4.0F);
                victim.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20 * 3, 0), boss);
                victim.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 20 * 3, 2), boss);
            }
            case RULER_OF_ALL_CREATION, ABYSSAL_RULER_OF_ALL_CREATION -> {
                victim.addEffect(new MobEffectInstance(ModMobEffects.BLEEDING, 20 * 4, 0), boss);
                boss.heal(this == ABYSSAL_RULER_OF_ALL_CREATION ? 2.5F : 1.5F);
                victim.addEffect(new MobEffectInstance(MobEffects.WITHER, 20 * 3, 0), boss);
                float voidDmg = (this == ABYSSAL_RULER_OF_ALL_CREATION) ? 4.0F : 2.5F;
                victim.hurtServer(level, level.damageSources().fellOutOfWorld(), voidDmg);
                level.sendParticles(ParticleTypes.EXPLOSION, victim.getX(), victim.getY() + 0.5, victim.getZ(), 1, 0, 0, 0, 0);
                level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, victim.getX(), victim.getY() + 0.5, victim.getZ(), 10, 0.2, 0.2, 0.2, 0.04);
            }
        }
    }
}