package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier;

import com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.on_hit.EchoingSonicCoreOnHit;
import io.redspace.irons_artifice.client.particle.ColorTransitionParticleOption;
import io.redspace.irons_artifice.data.ShotComponentMap;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.modifier.GunModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public final class EchoingSonicCoreModifier implements GunModifier {
    private static final int SONIC_CYAN = 0x00E5FF;
    private static final int SCULK_DARK = 0x032630;

    @Override
    public void apply(ShotComponentMap components) {
        components.getOrCreate(ShotComponents.ON_HIT).add(new EchoingSonicCoreOnHit());
        components.getOrCreate(ShotComponents.PARTICLE_TRAIL).add(
                ColorTransitionParticleOption.bulletTrail(SONIC_CYAN, SCULK_DARK)
        );
        components.getOrCreate(ShotComponents.MUZZLE_FLASH).addTint(SONIC_CYAN);
    }

    @Override
    public void getDescriptionText(Consumer<Component> builder) {
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.echoing_sonic_core").withStyle(ChatFormatting.AQUA));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.echoing_sonic_core.desc").withStyle(ChatFormatting.DARK_AQUA));
    }
}