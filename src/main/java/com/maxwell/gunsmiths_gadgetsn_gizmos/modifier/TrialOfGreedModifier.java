package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import io.redspace.irons_artifice.api.GunShootEvent;
import io.redspace.irons_artifice.client.particle.ColorTransitionParticleOption;
import io.redspace.irons_artifice.damage.DamageSources;
import io.redspace.irons_artifice.data.ShotComponentMap;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.modifier.GunModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID)
public final class TrialOfGreedModifier implements GunModifier {
    @SubscribeEvent
    public static void onShoot(GunShootEvent.Post event) {
        LivingEntity shooter = event.getEntity();
        if (!(shooter.level() instanceof ServerLevel level)) return;
        AABB enrageArea = shooter.getBoundingBox().inflate(16.0);
        for (Entity entity : level.getEntities(shooter, enrageArea, e -> e instanceof Mob)) {
            Mob mob = (Mob) entity;
            mob.addEffect(new MobEffectInstance(MobEffects.SPEED, 20 * 10, 1));
            mob.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 20 * 10, 0));
            mob.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 20 * 10, 0));
            mob.setTarget(shooter);
        }
        level.playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(),
                SoundEvents.RAID_HORN.value(), SoundSource.HOSTILE, 1.0F, 1.4F);
    }

    @SubscribeEvent
    public static void onMobDrops(LivingDropsEvent event) {
        if (event.getSource().is(DamageSources.BULLET_DAMAGE_TYPE)) {
            List<ItemEntity> extraDrops = new ArrayList<>();
            for (ItemEntity drop : event.getDrops()) {
                for (int i = 0; i < 3; i++) {
                    ItemEntity copy = new ItemEntity(drop.level(), drop.getX(), drop.getY(), drop.getZ(),
                            drop.getItem().copy());
                    extraDrops.add(copy);
                }
            }
            event.getDrops().addAll(extraDrops);
        }
    }

    @Override
    public void apply(ShotComponentMap components) {
        components.getOrCreate(ShotComponents.PARTICLE_TRAIL).add(
                ColorTransitionParticleOption.bulletTrail(0x550088, 0x00FF88)
        );
        components.getOrCreate(ShotComponents.MUZZLE_FLASH).addTint(0x00FF88);
    }

    @Override
    public void getDescriptionText(Consumer<Component> builder) {
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.trial_of_greed").withStyle(ChatFormatting.DARK_GREEN));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.trial_of_greed.bonus").withStyle(ChatFormatting.GREEN));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.trial_of_greed.enrage").withStyle(ChatFormatting.RED));
    }
}