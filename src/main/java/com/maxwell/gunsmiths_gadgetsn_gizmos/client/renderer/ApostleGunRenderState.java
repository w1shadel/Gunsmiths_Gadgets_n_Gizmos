package com.maxwell.gunsmiths_gadgetsn_gizmos.client.renderer;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.AnimationState;

public class ApostleGunRenderState extends HumanoidRenderState {
    public final AnimationState holdGunAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();
    public final AnimationState reloadPhaseInAnimationState = new AnimationState();
    public final AnimationState reloadLoopAnimationState = new AnimationState();
    public final AnimationState reloadEndAnimationState = new AnimationState();

    public boolean isCombatMode;
    public boolean isCastingSpell; 
}