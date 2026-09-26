package com.maxwell.gunsmiths_gadgetsn_gizmos.mixin.client;

import com.maxwell.gunsmiths_gadgetsn_gizmos.client.ApostleShaderManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
//    @Shadow
//    @Nullable
//    private PostChain postEffect;
//
//    @Shadow
//    private boolean effectActive;
//
//    @Inject(method = "togglePostEffect", at = @At("HEAD"), cancellable = true)
//    private void gunsmiths_gadgetsn_gizmos$preventDisablingApostleShader(CallbackInfo ci) {
//        if (this.postEffect != null && ApostleShaderManager.SHADER_ID.toString().equals(this.postEffect.getName())) {
//            this.effectActive = true;
//            ci.cancel();
//        }
//    }
}