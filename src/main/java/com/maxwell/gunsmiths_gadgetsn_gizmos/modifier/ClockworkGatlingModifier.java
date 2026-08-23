package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier;

import io.redspace.irons_artifice.data.ShotComponentMap;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.data.ValueModifier;
import io.redspace.irons_artifice.modifier.GunModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public final class ClockworkGatlingModifier implements GunModifier {
    @Override
    public void apply(ShotComponentMap components) {
        components.set(ShotComponents.FORCE_AUTO_FIRE, true);
        components.getOrCreate(ShotComponents.FIRE_RATE)
                .addModifier(new ValueModifier(2.5, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL));
        components.getOrCreate(ShotComponents.SPREAD)
                .addModifier(new ValueModifier(-1.0, ValueModifier.Operation.ADD, ValueModifier.Type.BENEFICIAL));
        components.getOrCreate(ShotComponents.CAMERA_RECOIL_MULTIPLIER)
                .addModifier(new ValueModifier(0.10, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.NEUTRAL));
        components.getOrCreate(ShotComponents.CHARACTER_BLOWBACK)
                .addModifier(new ValueModifier(0.9, ValueModifier.Operation.ADD, ValueModifier.Type.BENEFICIAL));
    }

    @Override
    public void getDescriptionText(Consumer<Component> builder) {
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.clockwork_gatling").withStyle(ChatFormatting.GOLD));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.clockwork_gatling.desc").withStyle(ChatFormatting.GREEN));
    }
}