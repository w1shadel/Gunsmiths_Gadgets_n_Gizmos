package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier;

import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.data.ValueModifier;
import io.redspace.irons_artifice.modifier.ValueStackModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.Map;
import java.util.function.Consumer;

public final class MastercraftedTriggerModifier extends ValueStackModifier {
    public MastercraftedTriggerModifier() {
        super(Map.of(
                ShotComponents.CAMERA_RECOIL_MULTIPLIER, new ValueModifier(-0.30, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL),
                ShotComponents.SPREAD, new ValueModifier(-2.5, ValueModifier.Operation.ADD, ValueModifier.Type.BENEFICIAL)
        ));
    }

    @Override
    public void getDescriptionText(Consumer<Component> builder) {
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.mastercrafted_trigger")
                .withStyle(ChatFormatting.GOLD));
        super.getDescriptionText(builder);
    }
}