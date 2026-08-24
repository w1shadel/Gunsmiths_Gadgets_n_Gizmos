package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.registry.ModItems;
import com.maxwell.gunsmiths_gadgetsn_gizmos.util.ModifierHelper;
import io.redspace.irons_artifice.client.particle.ColorTransitionParticleOption;
import io.redspace.irons_artifice.data.ShotComponentMap;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.item.GunItem;
import io.redspace.irons_artifice.item.MagazineContents;
import io.redspace.irons_artifice.modifier.GunModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.function.Consumer;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID)
public final class SculkDevourerModifier implements GunModifier {
    @SubscribeEvent
    public static void onKill(LivingDeathEvent event) {
        if (event.getSource().getDirectEntity() instanceof io.redspace.irons_artifice.entity.Bullet bullet) {
            if (ModifierHelper.hasModifier(bullet.getProfile().itemStack(), ModItems.SCULK_DEVOURER_MODIFIER.get())) {
                if (event.getSource().getEntity() instanceof LivingEntity killer) {
                    ItemStack gun = killer.getMainHandItem();
                    if (gun.getItem() instanceof GunItem gunItem) {
                        MagazineContents mag = GunItem.getMagazine(gun);
                        if (mag.count() < gunItem.magazineCapacity()) {
                            GunItem.setMagazine(gun, mag.with(mag.count() + 1));
                            killer.level().playSound(null, killer.getX(), killer.getY(), killer.getZ(),
                                    SoundEvents.SCULK_CATALYST_BLOOM, SoundSource.PLAYERS, 1.0F, 1.5F);
                            event.getEntity().level().addParticle(ParticleTypes.SCULK_SOUL,
                                    event.getEntity().getX(), event.getEntity().getY() + 1.0, event.getEntity().getZ(),
                                    0, 0.05, 0);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void apply(ShotComponentMap components) {
        components.getOrCreate(ShotComponents.PARTICLE_TRAIL).add(
                ColorTransitionParticleOption.bulletTrail(0x005555, 0x00FFAA)
        );
        components.getOrCreate(ShotComponents.MUZZLE_FLASH).addTint(0x00FFAA);
    }

    @Override
    public void getDescriptionText(Consumer<Component> builder) {
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.sculk_devourer").withStyle(ChatFormatting.DARK_GREEN));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.sculk_devourer.desc").withStyle(ChatFormatting.AQUA));
    }
}