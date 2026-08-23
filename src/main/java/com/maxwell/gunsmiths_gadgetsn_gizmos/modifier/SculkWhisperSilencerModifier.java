package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import io.redspace.irons_artifice.api.ComposeShotEvent;
import io.redspace.irons_artifice.client.sounds.GunShotSoundSettings;
import io.redspace.irons_artifice.data.ShotComponentMap;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.data.ValueModifier;
import io.redspace.irons_artifice.gun.ShotProfile;
import io.redspace.irons_artifice.modifier.GunModifier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.function.Consumer;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID)
public final class SculkWhisperSilencerModifier implements GunModifier {
    private static final double SNEAK_DAMAGE_BONUS = 0.60;

    @SubscribeEvent
    public static void onComposeShot(ComposeShotEvent event) {
        if (event.getEntity().isCrouching()) {
            ShotProfile profile = event.getShotProfile();
            profile.get(ShotComponents.DAMAGE).addModifier(new ValueModifier(
                    SNEAK_DAMAGE_BONUS, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL
            ));
        }
    }

    @Override
    public void apply(ShotComponentMap components) {
        components.getOrCreate(ShotComponents.GUNSHOT_SOUND).setBaseSound(
                GunShotSoundSettings.of(SoundEvents.SCULK_SENSOR_STEP, 0.9F, 1.2F, -1F, 0F, 16F)
        );
        components.getOrCreate(ShotComponents.MUZZLE_FLASH).addTint(0x05131A);
    }

    @Override
    public void getDescriptionText(Consumer<Component> builder) {
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.sculk_whisper_silencer").withStyle(ChatFormatting.DARK_AQUA));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.modifier.sculk_whisper_silencer.desc").withStyle(ChatFormatting.GREEN));
    }
}