package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier;

import com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.on_hit.OminousTrialPostHit;
import io.redspace.irons_artifice.client.particle.ColorTransitionParticleOption;
import io.redspace.irons_artifice.data.ShotComponentMap;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.data.ValueModifier;
import io.redspace.irons_artifice.modifier.GunModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public final class OminousChamberModifier implements GunModifier {
    private static final int OMINOUS_COLOR_FROM = 0x2A134D;
    private static final int OMINOUS_COLOR_TO = 0x8C38FF;

    @Override
    public void apply(ShotComponentMap components) {
        components.getOrCreate(ShotComponents.POST_HIT_EFFECTS).add(new OminousTrialPostHit());
        components.getOrCreate(ShotComponents.DAMAGE)
                .addModifier(new ValueModifier(0.25, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL));
        components.getOrCreate(ShotComponents.PARTICLE_TRAIL).add(
                ColorTransitionParticleOption.bulletTrail(OMINOUS_COLOR_FROM, OMINOUS_COLOR_TO)
        );
        components.getOrCreate(ShotComponents.MUZZLE_FLASH).addTint(OMINOUS_COLOR_TO);
    }

    @Override
    public void getDescriptionText(Consumer<Component> builder) {
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.ominous_chamber").withStyle(ChatFormatting.DARK_PURPLE));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.ominous_chamber.bonus").withStyle(ChatFormatting.GREEN));
    }
}