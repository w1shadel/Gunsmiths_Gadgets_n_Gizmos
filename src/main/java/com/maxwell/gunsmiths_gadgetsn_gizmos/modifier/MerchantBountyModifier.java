package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModItems;
import com.maxwell.gunsmiths_gadgetsn_gizmos.util.ModifierHelper;
import io.redspace.irons_artifice.client.particle.ColorTransitionParticleOption;
import io.redspace.irons_artifice.data.ShotComponentMap;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.modifier.GunModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;

import java.util.function.Consumer;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID)
public final class MerchantBountyModifier implements GunModifier {
    private static final int EMERALD_COLOR = 0x17DD62;

    @SubscribeEvent
    public static void onMobKill(LivingDropsEvent event) {
        if (event.getSource().getDirectEntity() instanceof io.redspace.irons_artifice.entity.Bullet bullet) {
            if (ModifierHelper.hasModifier(bullet.getProfile().itemStack(), ModItems.MERCHANT_BOUNTY_MODIFIER.get())) {
                var entity = event.getEntity();
                var random = entity.getRandom();
                int count = random.nextIntBetweenInclusive(1, 3);
                event.getDrops().add(new ItemEntity(
                        entity.level(), entity.getX(), entity.getY(), entity.getZ(),
                        new ItemStack(Items.EMERALD, count)
                ));
                entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1.0F, 1.2F);
            }
        }
    }

    @Override
    public void apply(ShotComponentMap components) {
        components.getOrCreate(ShotComponents.PARTICLE_TRAIL).add(
                ColorTransitionParticleOption.bulletTrail(EMERALD_COLOR, 0x00441B)
        );
        components.getOrCreate(ShotComponents.MUZZLE_FLASH).addTint(EMERALD_COLOR);
    }

    @Override
    public void getDescriptionText(Consumer<Component> builder) {
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.merchant_bounty")
                .withStyle(ChatFormatting.GREEN));
    }
}