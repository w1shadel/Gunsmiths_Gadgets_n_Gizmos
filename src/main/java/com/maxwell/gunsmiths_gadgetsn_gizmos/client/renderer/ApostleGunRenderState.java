package com.maxwell.gunsmiths_gadgetsn_gizmos.client.renderer;

import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.phys.Vec3;

public class ApostleGunRenderState extends HumanoidRenderState {
    public final AnimationState holdGunAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();
    public final AnimationState reloadPhaseInAnimationState = new AnimationState();
    public final AnimationState reloadLoopAnimationState = new AnimationState();
    public final AnimationState reloadEndAnimationState = new AnimationState();
    public boolean isCombatMode;
    public boolean isCastingSpell;
    public boolean isTelegraphingTeleport;
    public Vec3 teleportDest = Vec3.ZERO;
    public float telegraphProgress;
}