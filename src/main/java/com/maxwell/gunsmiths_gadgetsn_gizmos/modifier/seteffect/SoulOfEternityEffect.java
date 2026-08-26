package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.seteffect;


import com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy.SetBonusEffect;
import com.mojang.serialization.MapCodec;
import io.redspace.irons_artifice.entity.Bullet;
import io.redspace.irons_artifice.item.GunItem;
import io.redspace.irons_artifice.item.MagazineContents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class SoulOfEternityEffect implements SetBonusEffect {
    public static final SoulOfEternityEffect INSTANCE = new SoulOfEternityEffect();
    public static final MapCodec<SoulOfEternityEffect> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<? extends SetBonusEffect> codec() {
        return CODEC;
    }

    @Override
    public void onShoot(ServerLevel level, LivingEntity shooter, io.redspace.irons_artifice.gun.ShotProfile profile, net.minecraft.world.phys.Vec3 origin, net.minecraft.world.phys.Vec3 direction) {

        shooter.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 4, 1));
        shooter.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 20 * 4, 1));
        shooter.addEffect(new MobEffectInstance(MobEffects.SPEED, 20 * 4, 1));
    }

    @Override
    public void onKill(ServerLevel level, LivingEntity killer, LivingEntity victim, Bullet bullet) {

        ItemStack gun = killer.getMainHandItem();
        if (gun.getItem() instanceof GunItem gunItem) {
            GunItem.setMagazine(gun, MagazineContents.get(gun).with(gunItem.magazineCapacity()));

            level.playSound(null, killer.getX(), killer.getY(), killer.getZ(),
                    SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1.2F, 1.2F);
            level.sendParticles(ParticleTypes.TOTEM_OF_UNDYING,
                    killer.getX(), killer.getY() + 1.0, killer.getZ(),
                    30, 0.5, 0.5, 0.5, 0.2);
        }
    }
}