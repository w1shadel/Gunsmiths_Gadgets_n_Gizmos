package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier;

import com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.on_hit.TownBellFlareOnHit;
import io.redspace.irons_artifice.client.particle.ColorTransitionParticleOption;
import io.redspace.irons_artifice.data.ShotComponentMap;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.modifier.GunModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public final class TownBellFlareModifier implements GunModifier {
    private static final int FLARE_COLOR_FROM = 0xFFD700;
    private static final int FLARE_COLOR_TO = 0xFFF8DC;

    @Override
    public void apply(ShotComponentMap components) {
        components.getOrCreate(ShotComponents.ON_HIT).add(new TownBellFlareOnHit());
        components.getOrCreate(ShotComponents.PARTICLE_TRAIL).add(
                ColorTransitionParticleOption.bulletTrail(FLARE_COLOR_FROM, FLARE_COLOR_TO)
        );
        components.getOrCreate(ShotComponents.MUZZLE_FLASH).addTint(FLARE_COLOR_FROM);
    }

    @Override
    public void getDescriptionText(Consumer<Component> builder) {
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.town_bell_flare")
                .withStyle(ChatFormatting.GOLD));
    }
}