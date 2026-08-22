package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier;

import io.redspace.irons_artifice.data.ShotComponentMap;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.data.ValueModifier;
import io.redspace.irons_artifice.modifier.GunModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public final class UnstableOverclockModifier implements GunModifier {
    @Override
    public void apply(ShotComponentMap components) {
        components.set(ShotComponents.FORCE_AUTO_FIRE, true);
        components.getOrCreate(ShotComponents.FIRE_RATE)
                .addModifier(new ValueModifier(2.0, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL));
        components.getOrCreate(ShotComponents.CAMERA_RECOIL_MULTIPLIER)
                .addModifier(new ValueModifier(1.5, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.HARMFUL));
        components.getOrCreate(ShotComponents.SPREAD)
                .addModifier(new ValueModifier(6.0, ValueModifier.Operation.ADD, ValueModifier.Type.HARMFUL));
        components.getOrCreate(ShotComponents.CHARACTER_BLOWBACK)
                .addModifier(new ValueModifier(1.2, ValueModifier.Operation.ADD, ValueModifier.Type.NEUTRAL));
    }

    @Override
    public void getDescriptionText(Consumer<Component> builder) {
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.unstable_overclock").withStyle(ChatFormatting.GOLD));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.unstable_overclock.bonus").withStyle(ChatFormatting.GREEN));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.unstable_overclock.penalty").withStyle(ChatFormatting.RED));
    }
}
