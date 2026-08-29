package com.maxwell.gunsmiths_gadgetsn_gizmos.events;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.compat.curios.CuriosCompat;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.GunsmithConfig;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModItems;
import io.redspace.irons_artifice.api.ComposeShotEvent;
import io.redspace.irons_artifice.api.ConsumeAmmoEvent;
import io.redspace.irons_artifice.api.GunShootEvent;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.data.ValueModifier;
import io.redspace.irons_artifice.gun.ArmPoseKind;
import io.redspace.irons_artifice.gun.ShotProfile;
import io.redspace.irons_artifice.item.FireDelayState;
import io.redspace.irons_artifice.registry.ItemRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID)
public class CuriosAccessoryEvents {
    @SubscribeEvent
    public static void onWelderGogglesHurt(net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player) {
            if (CuriosCompat.isEquipped(player, ModItems.WELDER_GOGGLES.get())) {
                if (event.getSource().is(net.minecraft.tags.DamageTypeTags.IS_EXPLOSION)) {
                    event.setNewDamage(event.getOriginalDamage() * 0.50F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onComposeShot(ComposeShotEvent event) {
        LivingEntity living = event.getEntity();
        ShotProfile profile = event.getShotProfile();
        if (CuriosCompat.isEquipped(living, ModItems.RECOIL_HARNESS.get())) {
            profile.get(ShotComponents.CAMERA_RECOIL_MULTIPLIER).addModifier(
                    new ValueModifier(-0.40, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL)
            );
        }
        if (CuriosCompat.isEquipped(living, ModItems.RANGEFINDER_MONOCLE.get())) {
            profile.get(ShotComponents.SPREAD).addModifier(
                    new ValueModifier(-1.5, ValueModifier.Operation.ADD, ValueModifier.Type.BENEFICIAL)
            );
        }
        if (CuriosCompat.isEquipped(living, ModItems.GUNSLINGERS_SPURS.get())) {
            profile.get(ShotComponents.IN_AIR_PENALTY).addModifier(
                    new ValueModifier(-1.0, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL)
            );
        }
        if (CuriosCompat.isEquipped(living, ModItems.GUNSMITHS_GLOVES.get())) {
            profile.get(ShotComponents.RELOAD_SPEED_MULTIPLIER).addModifier(
                    new ValueModifier(0.15, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL)
            );
        }
        if (CuriosCompat.isEquipped(living, ModItems.SPEEDLOADER_BELT.get()) && profile.gun().armPoseKind() == ArmPoseKind.PISTOL) {
            profile.get(ShotComponents.RELOAD_SPEED_MULTIPLIER).addModifier(
                    new ValueModifier(0.30, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL)
            );
        }
        if (CuriosCompat.isEquipped(living, ModItems.GAMBLERS_RING.get())) {
            float procChance = GunsmithConfig.COMMON.gamblersRingChance.get().floatValue();
            if (living.getRandom().nextFloat() < procChance) {
                profile.get(ShotComponents.DAMAGE).addModifier(
                        new ValueModifier(1.0, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL)
                );
            }
        }
    }

    @SubscribeEvent
    public static void onConsumeAmmo(ConsumeAmmoEvent event) {
        if (CuriosCompat.isEquipped(event.getEntity(), ModItems.GAMBLERS_RING.get())) {
            float procChance = GunsmithConfig.COMMON.gamblersRingChance.get().floatValue();
            if (event.getEntity().getRandom().nextFloat() < procChance) {
                event.setAmmoToConsume(0);
                event.getEntity().level().playSound(null, event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(),
                        SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 0.8F, 1.8F);
            }
        }
    }

    @SubscribeEvent
    public static void onGunShoot(GunShootEvent.Post event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
            if (CuriosCompat.isEquipped(player, ModItems.MAGNETIC_POUCH.get())) {
                float recoveryChance = GunsmithConfig.COMMON.magneticPouchChance.get().floatValue();
                RandomSource random = player.getRandom();
                if (random.nextFloat() < recoveryChance) {
                    ItemStack recovered = random.nextBoolean()
                            ? new ItemStack(ItemRegistry.BULLET.get(), 1)
                            : new ItemStack(ModItems.VOID_CASING.get(), 1);
                    if (!player.addItem(recovered)) {
                        player.drop(recovered, false);
                    }
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.5F, 1.5F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (CuriosCompat.isEquipped(player, ModItems.QUICK_DRAW_HOLSTER.get())) {
                ItemStack newStack = event.getTo();
                if (newStack.getItem() instanceof io.redspace.irons_artifice.item.GunItem) {
                    FireDelayState.remove(newStack);
                    player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                            SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 0.8F, 1.6F);
                }
            }
        }
    }
}