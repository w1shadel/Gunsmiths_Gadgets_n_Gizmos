package com.maxwell.gunsmiths_gadgetsn_gizmos.init;

import io.redspace.irons_artifice.client.sounds.GunShotSoundSettings;
import io.redspace.irons_artifice.data.*;
import io.redspace.irons_artifice.entity.Bullet;
import io.redspace.irons_artifice.gun.ArmPoseKind;
import io.redspace.irons_artifice.gun.GunProfile;
import io.redspace.irons_artifice.item.AnimationAdjuster;
import io.redspace.irons_artifice.registry.SoundRegistry;
import net.minecraft.sounds.SoundEvents;

import java.util.Map;

public class ModGuns {

    public static final GunProfile CLUNKER_RIFLE = new GunProfile(
            () -> {
                ShotComponentMap map = new ShotComponentMap();
                map.set(ShotComponents.PROJECTILE_COUNT, Value.of(1));

                map.set(ShotComponents.DAMAGE, Value.of(36.0));
                map.set(ShotComponents.BULLET_SPEED, Value.of(Bullet.BASE_SPEED * 2.2));
                map.set(ShotComponents.SPREAD, Value.of(0.4)); 

                map.set(ShotComponents.CAMERA_RECOIL, RecoilProfile.of(42.0F, 0.3F, 1.5F, 999));
                map.set(ShotComponents.CHARACTER_BLOWBACK, Value.of(1.0)); 
                map.set(ShotComponents.FIRE_DELAY, Value.of(1));

                GunShotSoundStack soundStack = new GunShotSoundStack(
                        GunShotSoundSettings.standardShot(SoundRegistry.ARQUEBUS_SHOOT, 0.75F), 
                        GunShotSoundSettings.standardEcho(SoundRegistry.BULLET_ECHO_MUZZLELOADER, 0.9F),
                        PlayableSound.of(PlayableSound.holder(SoundEvents.DISPENSER_FAIL), 0.75F, 1.4F, 1.6F)
                );

                soundStack.addAccent(PlayableSound.of(PlayableSound.holder(SoundEvents.LAVA_EXTINGUISH), 1.5F, 0.6F, 0.8F));
                map.set(ShotComponents.GUNSHOT_SOUND, soundStack);

                map.set(ShotComponents.MUZZLE_FLASH, MuzzleFlashSettings.of(2.8F, MuzzleFlashType.LARGE));
                return map;
            },
            1,  
            8,  
            70, 
            FireMode.SEMI,
            null, 
            ArmPoseKind.RIFLE,

            ReloadCueStack.of(
                    new ReloadCue(0.1F, PlayableSound.of(SoundRegistry.ARQUEBUS_OPEN_BREECH, 1.2F, 0.8F, 0.9F)),
                    new ReloadCue(0.8F, PlayableSound.of(SoundRegistry.ARQUEBUS_LOAD, 1.2F, 0.9F, 1.0F)),
                    new ReloadCue(1.8F, PlayableSound.of(PlayableSound.holder(SoundEvents.PISTON_EXTEND), 1.2F, 0.6F, 0.7F)), 
                    new ReloadCue(2.6F, PlayableSound.of(SoundRegistry.COCK_HAMMER, 1.2F, 0.8F, 0.9F)),
                    new ReloadCue(3.2F, PlayableSound.of(SoundRegistry.ARQUEBUS_CLOSE_BREECH, 1.2F, 0.85F, 0.95F))
            ),
            PlayableSound.of(SoundRegistry.ARQUEBUS_EQUIP, 0.8F, 0.8F, 1.0F),
            FireCycleCueStack.EMPTY,
            AnimationAdjuster.LOWER_HAMMER,
            Map.of()
    );

    public static final GunProfile MINIGUN = new GunProfile(
            () -> {
                ShotComponentMap map = new ShotComponentMap();
                map.set(ShotComponents.PROJECTILE_COUNT, Value.of(1));

                map.set(ShotComponents.DAMAGE, Value.of(1.5));
                map.set(ShotComponents.BULLET_SPEED, Value.of(Bullet.BASE_SPEED * 1.0)); 

                map.set(ShotComponents.SPREAD, Value.of(5.0));

                map.set(ShotComponents.CAMERA_RECOIL, RecoilProfile.of(1.8F, 0.5F, 1.8F, 4321));
                map.set(ShotComponents.CHARACTER_BLOWBACK, Value.of(0.02));
                map.set(ShotComponents.FIRE_DELAY, Value.of(2)); 

                GunShotSoundStack soundStack = new GunShotSoundStack(
                        GunShotSoundSettings.standardShot(SoundRegistry.CLOCKWORK_RIFLE_SHOOT, 1.2F),
                        GunShotSoundSettings.standardEcho(SoundRegistry.BULLET_ECHO_GENERIC, 1.0F),
                        PlayableSound.of(PlayableSound.holder(SoundEvents.DISPENSER_FAIL), 0.5F, 1.6F, 1.8F)
                );
                soundStack.addAccent(PlayableSound.of(PlayableSound.holder(SoundEvents.ITEM_PICKUP), 0.4F, 1.5F, 1.8F));
                map.set(ShotComponents.GUNSHOT_SOUND, soundStack);

                map.set(ShotComponents.MUZZLE_FLASH, MuzzleFlashSettings.of(1.0F, MuzzleFlashType.TRIANGLE, MuzzleFlashType.SMALL_STAR));
                return map;
            },
            100, 
            4,   
            90,  
            FireMode.AUTO,
            null,
            ArmPoseKind.RIFLE,

            ReloadCueStack.of(
                    new ReloadCue(0.3F, PlayableSound.of(SoundRegistry.CLOCKWORK_RIFLE_EJECT_MAG, 1.2F, 0.85F, 0.95F)),
                    new ReloadCue(1.5F, PlayableSound.of(PlayableSound.holder(SoundEvents.IRON_TRAPDOOR_OPEN), 1.0F, 0.8F, 0.9F)),
                    new ReloadCue(2.6F, PlayableSound.of(PlayableSound.holder(SoundEvents.CHAIN_STEP), 1.2F, 0.9F, 1.1F)),
                    new ReloadCue(3.6F, PlayableSound.of(SoundRegistry.CLOCKWORK_RIFLE_INSERT_MAG, 1.2F, 0.85F, 0.95F)),
                    new ReloadCue(4.2F, PlayableSound.of(PlayableSound.holder(SoundEvents.PISTON_EXTEND), 1.0F, 1.4F, 1.6F))
            ),
            PlayableSound.of(SoundRegistry.CLOCKWORK_RIFLE_EQUIP, 0.8F, 0.9F, 1.1F),
            FireCycleCueStack.EMPTY,
            AnimationAdjuster.NONE,
            Map.of()
    );
}