package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import io.redspace.irons_artifice.api.GunShootEvent;
import io.redspace.irons_artifice.client.particle.ColorTransitionParticleOption;
import io.redspace.irons_artifice.data.ShotComponentMap;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.data.ValueModifier;
import io.redspace.irons_artifice.modifier.GunModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.function.Consumer;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID)
public final class BloodboundCalamityModifier implements GunModifier {
    private static final String MODIFIER_TAG = "gunsmiths_gadgetsn_gizmos:bloodbound_calamity";

    @SubscribeEvent
    public static void onGunShoot(GunShootEvent.Pre event) {
        LivingEntity shooter = event.getEntity();
        if (event.getShotProfile().components().has(ShotComponents.PIERCING)
                && event.getShotProfile().itemStack().getHoverName().getString() != null) {
            shooter.hurtServer((ServerLevel) event.getEntity().level(), shooter.damageSources().magic(), 2.0F);
        }
    }

    @Override
    public void apply(ShotComponentMap components) {
        components.getOrCreate(ShotComponents.DAMAGE)
                .addModifier(new ValueModifier(1.20, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL));
        components.getOrCreate(ShotComponents.PIERCING)
                .addModifier(new ValueModifier(2, ValueModifier.Operation.ADD, ValueModifier.Type.BENEFICIAL));
        components.getOrCreate(ShotComponents.GRAVITY)
                .addModifier(new ValueModifier(-1.0, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL));
        components.getOrCreate(ShotComponents.PARTICLE_TRAIL).add(
                ColorTransitionParticleOption.bulletTrail(0x8B0000, 0x1A0000)
        );
        components.getOrCreate(ShotComponents.MUZZLE_FLASH).addTint(0x8B0000);
    }

    @Override
    public void getDescriptionText(Consumer<Component> builder) {
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.bloodbound_calamity").withStyle(ChatFormatting.DARK_RED));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.bloodbound_calamity.damage").withStyle(ChatFormatting.GREEN));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.bloodbound_calamity.cost").withStyle(ChatFormatting.RED));
    }
}