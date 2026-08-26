package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModItems;
import com.maxwell.gunsmiths_gadgetsn_gizmos.util.ModifierHelper;
import io.redspace.irons_artifice.api.GunShootEvent;
import io.redspace.irons_artifice.client.particle.ColorTransitionParticleOption;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID)
public final class MidasTouchChamberModifier implements GunModifier {
    @SubscribeEvent
    public static void onShoot(GunShootEvent.Post event) {
        if (!ModifierHelper.hasModifier(event.getShotProfile(), ModItems.MIDAS_TOUCH_CHAMBER_MODIFIER.get())) {
            return;
        }
        LivingEntity shooter = event.getEntity();
        if (!(shooter.level() instanceof ServerLevel level)) return;
        AABB area = shooter.getBoundingBox().inflate(16.0);
        for (Entity entity : level.getEntities(shooter, area, e -> e instanceof Mob)) {
            Mob mob = (Mob) entity;
            mob.addEffect(new MobEffectInstance(MobEffects.SPEED, 20 * 8, 1));
            mob.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 20 * 8, 1));
            mob.setTarget(shooter);
        }
    }

    @SubscribeEvent
    public static void onMobDrops(LivingDropsEvent event) {
        if (event.getSource().getDirectEntity() instanceof io.redspace.irons_artifice.entity.Bullet bullet) {
            if (ModifierHelper.hasModifier(bullet.getProfile().itemStack(), ModItems.MIDAS_TOUCH_CHAMBER_MODIFIER.get())) {
                var entity = event.getEntity();
                var random = entity.getRandom();
                List<ItemEntity> extra = new ArrayList<>();
                for (ItemEntity drop : event.getDrops()) {
                    for (int i = 0; i < 4; i++) {
                        extra.add(new ItemEntity(drop.level(), drop.getX(), drop.getY(), drop.getZ(), drop.getItem().copy()));
                    }
                }
                extra.add(new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(),
                        new ItemStack(Items.EMERALD, random.nextIntBetweenInclusive(3, 6))));
                event.getDrops().addAll(extra);
                entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0F, 1.5F);
            }
        }
    }

    @Override
    public void apply(ShotComponentMap components) {
        components.getOrCreate(ShotComponents.PARTICLE_TRAIL).add(
                ColorTransitionParticleOption.bulletTrail(0xFFD700, 0x00FF88)
        );
        components.getOrCreate(ShotComponents.MUZZLE_FLASH).addTint(0xFFD700);
    }

    @Override
    public void getDescriptionText(Consumer<Component> builder) {
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.midas_touch_chamber").withStyle(ChatFormatting.GOLD));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.midas_touch_chamber.desc").withStyle(ChatFormatting.GREEN));
    }
}