package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier;

import com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.on_hit.ReapersTempestOnHit;
import io.redspace.irons_artifice.client.particle.ColorTransitionParticleOption;
import io.redspace.irons_artifice.data.ShotComponentMap;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.modifier.GunModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public final class ReapersTempestModifier implements GunModifier {
    @Override
    public void apply(ShotComponentMap components) {
        components.getOrCreate(ShotComponents.ON_HIT).add(new ReapersTempestOnHit());
        components.getOrCreate(ShotComponents.PARTICLE_TRAIL).add(
                ColorTransitionParticleOption.bulletTrail(0x00FFFF, 0x1A1A40)
        );
        components.getOrCreate(ShotComponents.MUZZLE_FLASH).addTint(0x00FFFF);
    }

    @Override
    public void getDescriptionText(Consumer<Component> builder) {
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.reapers_tempest").withStyle(ChatFormatting.DARK_AQUA));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.reapers_tempest.desc").withStyle(ChatFormatting.GREEN));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.reapers_tempest.curse").withStyle(ChatFormatting.YELLOW));
    }
}