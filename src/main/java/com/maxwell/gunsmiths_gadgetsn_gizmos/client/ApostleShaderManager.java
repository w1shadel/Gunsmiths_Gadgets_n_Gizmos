package com.maxwell.gunsmiths_gadgetsn_gizmos.client;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.ApostleGunEntity;
import com.maxwell.gunsmiths_gadgetsn_gizmos.mixin.client.PostChainAccessor;
import com.maxwell.gunsmiths_gadgetsn_gizmos.mixin.client.PostPassAccessor;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.system.MemoryStack;

import java.util.List;
import java.util.Map;

public class ApostleShaderManager {
    public static final Identifier SHADER_ID =
            Identifier.fromNamespaceAndPath(GunsmithsGadgetsnGizmos.MODID, "ash_storm");
    public static boolean DEBUG_FORCE_ACTIVE = false;
    private static float progress = 0.0F;
    private static boolean active = false;

    public static void clientTick() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.isPaused() || mc.level == null || mc.player == null) return;
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
            if (!SHADER_ID.equals(mc.gameRenderer.currentPostEffect())) {
                mc.gameRenderer.setPostEffect(SHADER_ID);
            }
            active = true;
        } else if (active) {
            if (SHADER_ID.equals(mc.gameRenderer.currentPostEffect())) {
                mc.gameRenderer.clearPostEffect();
            }
            active = false;
        }
        if (active) {
            PostChain chain = mc.getShaderManager().getPostChain(SHADER_ID, LevelTargetBundle.MAIN_TARGETS);
            float time = (float) player.tickCount;
            float wave = Mth.sin(time * 0.014F) * 0.32F + Mth.sin(time * 0.030F) * 0.15F + 0.45F;
            float sootAmount = Math.max(0.12F, wave) * progress;
            List<PostPass> passes = ((PostChainAccessor) chain).gunsmiths_gadgetsn_gizmos$getPasses();
            for (PostPass pass : passes) {
                Map<String, GpuBuffer> uniforms = ((PostPassAccessor) pass).gunsmiths_gadgetsn_gizmos$getCustomUniforms();
                if (uniforms.containsKey("AshStormConfig")) {
                    int size = new Std140SizeCalculator().putFloat().putFloat().putFloat().get();
                    try (MemoryStack stack = MemoryStack.stackPush()) {
                        Std140Builder builder = Std140Builder.onStack(stack, size);
                        builder.putFloat(progress);
                        builder.putFloat(time);
                        builder.putFloat(sootAmount);
                        GpuBuffer newUbo = RenderSystem.getDevice().createBuffer(
                                () -> "AshStormConfig",
                                128,
                                builder.get()
                        );
                        GpuBuffer oldUbo = uniforms.put("AshStormConfig", newUbo);
                        if (oldUbo != null) {
                            oldUbo.close();
                        }
                    }
                }
            }
        }
    }
}