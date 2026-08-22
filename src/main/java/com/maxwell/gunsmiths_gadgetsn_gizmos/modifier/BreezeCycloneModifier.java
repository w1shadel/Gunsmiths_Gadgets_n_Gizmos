package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier;

import com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.on_hit.CycloneBurstOnHit;
import io.redspace.irons_artifice.data.ShotComponentMap;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.data.ValueModifier;
import io.redspace.irons_artifice.modifier.GunModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public final class BreezeCycloneModifier implements GunModifier {
    @Override
    public void apply(ShotComponentMap components) {
        components.getOrCreate(ShotComponents.ON_HIT).add(new CycloneBurstOnHit());
        components.getOrCreate(ShotComponents.CHARACTER_BLOWBACK)
                .addModifier(new ValueModifier(0.8, ValueModifier.Operation.ADD, ValueModifier.Type.BENEFICIAL));
        components.getOrCreate(ShotComponents.IN_AIR_PENALTY)
                .addModifier(new ValueModifier(-0.6, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL));
    }

    @Override
    public void getDescriptionText(Consumer<Component> builder) {
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.breeze_cyclone")
                .withStyle(ChatFormatting.AQUA));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.rocket_jump_capable")
                .withStyle(ChatFormatting.GREEN));
    }
}
