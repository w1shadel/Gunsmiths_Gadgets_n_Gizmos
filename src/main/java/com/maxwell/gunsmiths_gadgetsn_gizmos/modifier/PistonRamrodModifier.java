package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier;

import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.data.ValueModifier;
import io.redspace.irons_artifice.modifier.ValueStackModifier;

import java.util.Map;

public final class PistonRamrodModifier extends ValueStackModifier {
    public PistonRamrodModifier() {
        super(Map.of(
                ShotComponents.BULLET_SPEED, new ValueModifier(0.40, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL),
                ShotComponents.KNOCKBACK, new ValueModifier(1.0, ValueModifier.Operation.ADD, ValueModifier.Type.BENEFICIAL),
                ShotComponents.FIRE_DELAY, new ValueModifier(0.50, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.HARMFUL)
        ));
    }
}