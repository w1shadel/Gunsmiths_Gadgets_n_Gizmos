package com.maxwell.gunsmiths_gadgetsn_gizmos.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ParticleStatus;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public class ClientApostleHandler {

    /**
     * パケット受信時にクライアント側で実行される豪雨エフェクト
     */
    public static void handleAshStormPacket() {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        ClientLevel level = mc.level;
        if (player == null || level == null || mc.isPaused()) return;

        RandomSource random = level.getRandom();
        int particleCount = mc.options.particles().get() == ParticleStatus.ALL ? 35 : 15;

        for (int i = 0; i < particleCount; i++) {
            double rx = player.getX() + (random.nextDouble() - 0.5) * 32.0;
            double rz = player.getZ() + (random.nextDouble() - 0.5) * 32.0;

            int groundY = level.getHeight(Heightmap.Types.MOTION_BLOCKING, (int) rx, (int) rz);
            double spawnY = groundY + 8.0 + random.nextDouble() * 6.0;

            level.addParticle(ParticleTypes.ASH,
                    rx, spawnY, rz,
                    -0.05, -0.8, -0.02);

            if (random.nextFloat() < 0.3F) {
                level.addParticle(ParticleTypes.FALLING_OBSIDIAN_TEAR,
                        rx, spawnY, rz,
                        0.0, -0.6, 0.0);
            }

            if (random.nextFloat() < 0.4F) {
                level.addParticle(ParticleTypes.SMOKE,
                        rx, groundY + 0.05, rz,
                        (random.nextDouble() - 0.5) * 0.05, 0.02, (random.nextDouble() - 0.5) * 0.05);
            }
        }

        if (player.tickCount % 20 == 0) {
            level.playLocalSound(player.getX(), player.getY(), player.getZ(),
                    SoundEvents.WEATHER_RAIN, SoundSource.WEATHER, 0.4F, 0.8F, false);
        }
    }
}