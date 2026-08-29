package com.maxwell.gunsmiths_gadgetsn_gizmos.api.gun;

import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public record MuzzleOffset(double px, double py, double pz) {
    public static final MuzzleOffset DEFAULT = new MuzzleOffset(0.0, 0.0, -20.0);

    
    public Vec3 calculateFirstPersonOffset(LivingEntity shooter) {
        float pitch = shooter.getXRot();
        float yaw = shooter.getYRot();
        Vec3 forwardVec = Vec3.directionFromRotation(pitch, yaw);
        Vec3 rightVec = Vec3.directionFromRotation(0, yaw + 90.0F);
        Vec3 upVec = Vec3.directionFromRotation(pitch - 90.0F, yaw);
        double armDirection = shooter.getMainArm() == HumanoidArm.LEFT ? -1.0 : 1.0;
        double forward = 0.40 + (-this.pz * 0.016);
        double right = 0.28 + (this.px * 0.012);
        double up = -0.22 + (this.py * 0.012);
        return forwardVec.scale(forward)
                .add(rightVec.scale(right * armDirection))
                .add(upVec.scale(up));
    }

    
    public Vec3 calculateThirdPersonOffset(LivingEntity shooter, Vec3 lookDirection) {
        float pitch = shooter.getXRot();
        float yaw = shooter.getYRot();
        Vec3 forwardVec = lookDirection.normalize();
        Vec3 rightVec = Vec3.directionFromRotation(0, yaw + 90.0F);
        Vec3 upVec = Vec3.directionFromRotation(pitch - 90.0F, yaw);
        double armDirection = shooter.getMainArm() == HumanoidArm.LEFT ? -1.0 : 1.0;
        double forward = 0.50 + (-this.pz / 16.0);
        double right = 0.35 + (this.px / 16.0);
        double up = -0.15 + (this.py / 16.0);
        return forwardVec.scale(forward)
                .add(rightVec.scale(right * armDirection))
                .add(upVec.scale(up));
    }
}