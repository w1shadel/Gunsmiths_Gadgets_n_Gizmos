package com.maxwell.gunsmiths_gadgetsn_gizmos.api.gun;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public record MuzzleOffset(double forward, double right, double up) {
    public static final MuzzleOffset DEFAULT = new MuzzleOffset(1.8, 0.30, -0.20);

    
    public Vec3 calculateFirstPersonOffset(LivingEntity shooter) {
        Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        Quaternionf camRot = new Quaternionf(camera.rotation());

        float handSign = shooter.getMainArm() == HumanoidArm.RIGHT ? 1.0F : -1.0F;


        Vector3f localPos = new Vector3f(
                (float) (this.right * handSign),
                (float) this.up,
                (float) -this.forward 
        );

        localPos.rotate(camRot);

        return new Vec3(localPos.x(), localPos.y(), localPos.z());
    }

    
    public Vec3 calculateThirdPersonOffset(LivingEntity shooter, Vec3 lookDirection) {
        Vec3 forwardVec = lookDirection.normalize();
        Vec3 rightVec = forwardVec.cross(new Vec3(0, 1, 0)).normalize();
        Vec3 upVec = rightVec.cross(forwardVec).normalize();

        float handSign = shooter.getMainArm() == HumanoidArm.RIGHT ? 1.0F : -1.0F;

        return forwardVec.scale(this.forward)
                .add(rightVec.scale(this.right * handSign))
                .add(upVec.scale(this.up));
    }
}