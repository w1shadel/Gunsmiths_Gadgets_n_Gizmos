package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.on_hit.CrimsonSingularityOnHit;
import io.redspace.irons_artifice.api.GunShootEvent;
import io.redspace.irons_artifice.client.particle.ColorTransitionParticleOption;
import io.redspace.irons_artifice.damage.DamageSources;
import io.redspace.irons_artifice.data.ShotComponentMap;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.data.ValueModifier;
import io.redspace.irons_artifice.modifier.GunModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.function.Consumer;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID)
public final class CrimsonSingularityModifier implements GunModifier {
    @SubscribeEvent
    public static void onShoot(GunShootEvent.Pre event) {
        if (event.getShotProfile().components().has(ShotComponents.PIERCING)) {
            event.getEntity().hurtServer((ServerLevel) event.getEntity().level(), event.getEntity().damageSources().magic(), 1.0F);
        }
    }

    @SubscribeEvent
    public static void onKill(LivingDeathEvent event) {
        if (event.getSource().is(DamageSources.BULLET_DAMAGE_TYPE) && event.getSource().getEntity() instanceof LivingEntity killer) {
            killer.heal(2.0F);
        }
    }

    @Override
    public void apply(ShotComponentMap components) {
        components.getOrCreate(ShotComponents.ON_HIT).add(new CrimsonSingularityOnHit());
        components.getOrCreate(ShotComponents.PIERCING)
                .addModifier(new ValueModifier(3, ValueModifier.Operation.ADD, ValueModifier.Type.BENEFICIAL));
        components.getOrCreate(ShotComponents.GRAVITY)
                .addModifier(new ValueModifier(-1.0, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL));
        components.getOrCreate(ShotComponents.PARTICLE_TRAIL).add(
                ColorTransitionParticleOption.bulletTrail(0xFF0033, 0x330000)
        );
        components.getOrCreate(ShotComponents.MUZZLE_FLASH).addTint(0xFF0033);
    }

    @Override
    public void getDescriptionText(Consumer<Component> builder) {
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.crimson_singularity").withStyle(ChatFormatting.RED));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.crimson_singularity.desc").withStyle(ChatFormatting.GREEN));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.crimson_singularity.cost").withStyle(ChatFormatting.DARK_RED));
    }
}