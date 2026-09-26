package com.maxwell.gunsmiths_gadgetsn_gizmos.client;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.ApostleGunEntity;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.GunsmithConfig;
import com.maxwell.gunsmiths_gadgetsn_gizmos.mixin.client.PostChainAccessor;
import com.mojang.blaze3d.shaders.AbstractUniform;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.List;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID, value = Dist.CLIENT)
public class ApostleShaderManager {
    public static final ResourceLocation SHADER_ID =
            ResourceLocation.fromNamespaceAndPath(GunsmithsGadgetsnGizmos.MODID, "shaders/post/ash_storm.json");

    public static boolean DEBUG_FORCE_ACTIVE = false;
    private static float progress = 0.0F;
    private static boolean active = false;
    private static PostChain customPostChain = null;
    private static int lastWidth = 0;
    private static int lastHeight = 0;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.isPaused() || mc.level == null || mc.player == null) return;

        if (!GunsmithConfig.CLIENT.enableAshStormShader.get()) {
            active = false;
            progress = 0.0F;
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

        active = progress > 0.001F;
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Pre event) {
        if (!active) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        int width = mc.getWindow().getWidth();
        int height = mc.getWindow().getHeight();

        if (customPostChain == null) {
            try {
                customPostChain = new PostChain(
                        mc.getTextureManager(),
                        mc.getResourceManager(),
                        mc.getMainRenderTarget(),
                        SHADER_ID
                );
                customPostChain.resize(width, height);
                lastWidth = width;
                lastHeight = height;
            } catch (Exception e) {
                GunsmithsGadgetsnGizmos.LOGGER.error("[ApostleShader] Failed to load PostChain", e);
                return;
            }
        }

        if (width != lastWidth || height != lastHeight) {
            customPostChain.resize(width, height);
            lastWidth = width;
            lastHeight = height;
        }

        Player player = mc.player;
        float time = (float) player.tickCount;
        float wave = Mth.sin(time * 0.014F) * 0.32F + Mth.sin(time * 0.030F) * 0.15F + 0.45F;
        float sootAmount = Math.max(0.12F, wave) * progress;

        try {
            List<PostPass> passes = ((PostChainAccessor) customPostChain).gunsmiths_gadgetsn_gizmos$getPasses();
            for (PostPass pass : passes) {
                EffectInstance effect = pass.getEffect();

                AbstractUniform uProgress = effect.safeGetUniform("GSG_Progress");
                if (uProgress != null) uProgress.set(progress);

                AbstractUniform uTime = effect.safeGetUniform("GSG_Time");
                if (uTime != null) uTime.set(time);

                AbstractUniform uSoot = effect.safeGetUniform("GSG_SootAmount");
                if (uSoot != null) uSoot.set(sootAmount);
            }
        } catch (Exception ignored) {
        }

        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.resetTextureMatrix();

        customPostChain.process(event.getPartialTick().getGameTimeDeltaTicks());

        mc.getMainRenderTarget().bindWrite(true);
        RenderSystem.viewport(0, 0, width, height);
    }
}