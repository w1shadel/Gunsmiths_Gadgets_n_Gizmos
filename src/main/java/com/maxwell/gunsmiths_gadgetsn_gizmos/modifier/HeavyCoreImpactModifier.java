package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier;

import com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.on_hit.HeavyCoreImpactOnHit;
import io.redspace.irons_artifice.data.ShotComponentMap;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.data.ValueModifier;
import io.redspace.irons_artifice.modifier.GunModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public final class HeavyCoreImpactModifier implements GunModifier {
    @Override
    public void apply(ShotComponentMap components) {
        components.getOrCreate(ShotComponents.ON_HIT).add(new HeavyCoreImpactOnHit());
        components.getOrCreate(ShotComponents.KNOCKBACK)
                .addModifier(new ValueModifier(1.5, ValueModifier.Operation.ADD, ValueModifier.Type.BENEFICIAL));
        components.getOrCreate(ShotComponents.BULLET_SPEED)
                .addModifier(new ValueModifier(-0.15, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.HARMFUL));
    }

    @Override
    public void getDescriptionText(Consumer<Component> builder) {
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.heavy_core_impact",
                String.format("%.1f", HeavyCoreImpactOnHit.DAMAGE_PER_BLOCK)).withStyle(ChatFormatting.AQUA));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.heavy_core_impact.desc_bonus").withStyle(ChatFormatting.GREEN));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.heavy_core_impact.desc_penalty").withStyle(ChatFormatting.RED));
    }

}
