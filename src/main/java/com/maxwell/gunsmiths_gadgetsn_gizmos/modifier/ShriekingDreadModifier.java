package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier;

import com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.on_hit.ShriekingDreadOnHit;
import io.redspace.irons_artifice.client.particle.ColorTransitionParticleOption;
import io.redspace.irons_artifice.data.ShotComponentMap;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.modifier.GunModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public final class ShriekingDreadModifier implements GunModifier {
    @Override
    public void apply(ShotComponentMap components) {
        components.getOrCreate(ShotComponents.ON_HIT).add(new ShriekingDreadOnHit());
        components.getOrCreate(ShotComponents.PARTICLE_TRAIL).add(
                ColorTransitionParticleOption.bulletTrail(0x05131A, 0x00FFFF)
        );
        components.getOrCreate(ShotComponents.MUZZLE_FLASH).addTint(0x00FFFF);
    }

    @Override
    public void getDescriptionText(Consumer<Component> builder) {
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.shrieking_dread").withStyle(ChatFormatting.DARK_PURPLE));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.shrieking_dread.desc").withStyle(ChatFormatting.AQUA));
    }
}