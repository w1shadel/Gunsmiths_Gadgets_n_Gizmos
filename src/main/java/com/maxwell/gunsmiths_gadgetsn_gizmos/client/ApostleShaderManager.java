package com.maxwell.gunsmiths_gadgetsn_gizmos.client;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.ApostleGunEntity;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.GunsmithConfig;
import com.mojang.blaze3d.shaders.Uniform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class ApostleShaderManager {
    public static final ResourceLocation SHADER_ID =
            ResourceLocation.fromNamespaceAndPath(GunsmithsGadgetsnGizmos.MODID, "shaders/post/ash_storm.json");

    public static boolean DEBUG_FORCE_ACTIVE = false;
    private static float progress = 0.0F;
    private static boolean active = false;

    public static void clientTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.isPaused() || mc.level == null || mc.player == null) return;

        if (!GunsmithConfig.CLIENT.enableAshStormShader.get()) {
            if (active) {
                if (mc.gameRenderer.currentEffect() != null) {
                    mc.gameRenderer.shutdownEffect();
                }
                active = false;
                progress = 0.0F;
            }
            return;
        }

        Player player = mc.player;
        List<ApostleGunEntity> bosses = mc.level.getEntitiesOfClass(
                ApostleGunEntity.class,
                player.getBoundingBox().inflate(36.0),
                b -> b.isAlive()
                        && (b.isPhase2() || b.isTransitioning())
                        && (b.getTarget() != null || b.isAggressive())
                        && player.distanceToSqr(b) <= 36.0 * 36.0
        );

        boolean shouldBeActive = DEBUG_FORCE_ACTIVE || !bosses.isEmpty();
        if (shouldBeActive) {
            progress = Math.min(1.0F, progress + 0.02F);
        } else {
            progress = Math.max(0.0F, progress - 0.02F);
        }

        if (progress > 0.001F) {
            if (mc.gameRenderer.currentEffect() == null) {
                mc.gameRenderer.loadEffect(SHADER_ID);
            }
            active = true;
        } else if (active) {
            if (mc.gameRenderer.currentEffect() != null) {
                mc.gameRenderer.shutdownEffect();
            }
            active = false;
        }

        if (active) {
            PostChain chain = mc.gameRenderer.currentEffect();
            if (chain != null) {
                float time = (float) player.tickCount;
                float wave = Mth.sin(time * 0.014F) * 0.32F + Mth.sin(time * 0.030F) * 0.15F + 0.45F;
                float sootAmount = Math.max(0.12F, wave) * progress;

                for (PostPass pass : chain.passes) {
                    EffectInstance effect = pass.getEffect();

                    // AshStormConfig が float3/vec3 などの場合
                    Uniform configUniform = effect.safeGetUniform("AshStormConfig");
                    if (configUniform != null) {
                        configUniform.set(progress, time, sootAmount);
                    }

                    // 個別 Uniform で定義されている場合へのフォールバック
                    Uniform uProgress = effect.safeGetUniform("Progress");
                    if (uProgress != null) uProgress.set(progress);

                    Uniform uTime = effect.safeGetUniform("Time");
                    if (uTime != null) uTime.set(time);

                    Uniform uSoot = effect.safeGetUniform("SootAmount");
                    if (uSoot != null) uSoot.set(sootAmount);
                }
            }
        }
    }
}