package com.maxwell.gunsmiths_gadgetsn_gizmos.mixin.client;

import com.maxwell.gunsmiths_gadgetsn_gizmos.client.ApostleShaderManager;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Shadow
    private Identifier postEffectId;
    @Shadow
    private boolean effectActive;

    /** F4キー等によるポストエフェクト解除を使徒戦中のみブロック */
    @Inject(method = "togglePostEffect", at = @At("HEAD"), cancellable = true)
    private void gunsmiths_gadgetsn_gizmos$preventDisablingApostleShader(CallbackInfo ci) {
        if (ApostleShaderManager.SHADER_ID.equals(this.postEffectId)) {
            this.effectActive = true; // 常に有効を強制
            ci.cancel(); // F4トグル処理をキャンセル
        }
    }
}