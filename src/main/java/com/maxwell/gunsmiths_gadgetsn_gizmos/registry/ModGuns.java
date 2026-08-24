package com.maxwell.gunsmiths_gadgetsn_gizmos.registry;

import io.redspace.irons_artifice.client.sounds.GunShotSoundSettings;
import io.redspace.irons_artifice.data.*;
import io.redspace.irons_artifice.entity.Bullet;
import io.redspace.irons_artifice.gun.ArmPoseKind;
import io.redspace.irons_artifice.gun.GunProfile;
import io.redspace.irons_artifice.gun.Guns;
import io.redspace.irons_artifice.item.AnimationAdjuster;
import io.redspace.irons_artifice.item.TopLoadConfig;
import io.redspace.irons_artifice.registry.SoundRegistry;
import net.minecraft.sounds.SoundEvents;

import java.util.Map;

public class ModGuns {

    // ★ 過圧クランクライフル (Overpressurized Clunker Rifle)
    public static final GunProfile CLUNKER_RIFLE = new GunProfile(
            () -> {
                ShotComponentMap map = new ShotComponentMap();
                map.set(ShotComponents.PROJECTILE_COUNT, Value.of(1));

                // 1. 【超高圧力】単発 36.0 の圧倒的破壊力 ＆ 超高速弾道
                map.set(ShotComponents.DAMAGE, Value.of(36.0));
                map.set(ShotComponents.BULLET_SPEED, Value.of(Bullet.BASE_SPEED * 2.2));
                map.set(ShotComponents.SPREAD, Value.of(0.4)); // 高精度

                // 2. 【過圧の代償】激しい画面反動 ＆ 自身への強烈な後退ノックバック
                map.set(ShotComponents.CAMERA_RECOIL, RecoilProfile.of(42.0F, 0.3F, 1.5F, 999));
                map.set(ShotComponents.CHARACTER_BLOWBACK, Value.of(1.0)); // 撃つと後ろへ大きく吹き飛ぶ
                map.set(ShotComponents.FIRE_DELAY, Value.of(1));

                // 3. 【サウンド】重厚な砲撃音 ＋ スチーム過圧解放音
                GunShotSoundStack soundStack = new GunShotSoundStack(
                        GunShotSoundSettings.standardShot(SoundRegistry.ARQUEBUS_SHOOT, 0.75F), // 低音の重撃
                        GunShotSoundSettings.standardEcho(SoundRegistry.BULLET_ECHO_MUZZLELOADER, 0.9F),
                        PlayableSound.of(PlayableSound.holder(SoundEvents.DISPENSER_FAIL), 0.75F, 1.4F, 1.6F)
                );
                // 撃った瞬間にプシューッと高圧蒸気が抜けるアクセント音
                soundStack.addAccent(PlayableSound.of(PlayableSound.holder(SoundEvents.LAVA_EXTINGUISH), 1.5F, 0.6F, 0.8F));
                map.set(ShotComponents.GUNSHOT_SOUND, soundStack);

                // 巨大マズルフラッシュ
                map.set(ShotComponents.MUZZLE_FLASH, MuzzleFlashSettings.of(2.8F, MuzzleFlashType.LARGE));
                return map;
            },
            1,  // ★ 装弾数: 1発！
            8,  // ★ 魔改造用スロット: 8個！
            70, // ★ リロード時間: 3.5秒 (70 tick)
            FireMode.SEMI,
            null, // 単発なのでトップロードループなし
            ArmPoseKind.RIFLE,
            // リロード時のサウンド演出 (3.5秒かけてじっくり圧力を溜める音)
            ReloadCueStack.of(
                    new ReloadCue(0.1F, PlayableSound.of(SoundRegistry.ARQUEBUS_OPEN_BREECH, 1.2F, 0.8F, 0.9F)),
                    new ReloadCue(0.8F, PlayableSound.of(SoundRegistry.ARQUEBUS_LOAD, 1.2F, 0.9F, 1.0F)),
                    new ReloadCue(1.8F, PlayableSound.of(PlayableSound.holder(SoundEvents.PISTON_EXTEND), 1.2F, 0.6F, 0.7F)), // クランク加圧音
                    new ReloadCue(2.6F, PlayableSound.of(SoundRegistry.COCK_HAMMER, 1.2F, 0.8F, 0.9F)),
                    new ReloadCue(3.2F, PlayableSound.of(SoundRegistry.ARQUEBUS_CLOSE_BREECH, 1.2F, 0.85F, 0.95F))
            ),
            PlayableSound.of(SoundRegistry.ARQUEBUS_EQUIP, 0.8F, 0.8F, 1.0F),
            FireCycleCueStack.EMPTY,
            AnimationAdjuster.LOWER_HAMMER,
            Map.of()
    );
}