package com.maxwell.gunsmiths_gadgetsn_gizmos.events;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.api.gun.MuzzleBoneAutoLoader;
import com.maxwell.gunsmiths_gadgetsn_gizmos.api.gun.MuzzleOffset;
import io.redspace.irons_artifice.api.GunShootEvent;
import io.redspace.irons_artifice.utils.Utils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.Optional;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID)
public class MuzzleOriginAlignEvents {
    private static final double MAX_AIM_DISTANCE = 64.0;

    @SubscribeEvent
    public static void onGunShootPre(GunShootEvent.Pre event) {
        LivingEntity shooter = event.getEntity();
        if (shooter == null || shooter.level() == null) return;
        ItemStack gunStack = event.getShotProfile().itemStack();
        if (gunStack.isEmpty()) return;
        Vec3 eyePos = shooter.getEyePosition();
        Vec3 lookAngle = event.getDirection().normalize();
        Vec3 traceEnd = eyePos.add(lookAngle.scale(MAX_AIM_DISTANCE));
        HitResult blockHit = shooter.level().clip(new ClipContext(
                eyePos, traceEnd, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, shooter
        ));
        Vec3 targetPoint = blockHit.getLocation();
        double closestDist = eyePos.distanceTo(targetPoint);
        AABB searchBox = new AABB(eyePos, targetPoint).inflate(1.0);
        for (Entity entity : shooter.level().getEntities(shooter, searchBox, e -> !e.isSpectator() && e.isPickable() && Utils.canHarm(shooter, e))) {
            AABB bb = entity.getBoundingBox().inflate(0.25);
            Optional<Vec3> clip = bb.clip(eyePos, targetPoint);
            if (clip.isPresent()) {
                double dist = eyePos.distanceTo(clip.get());
                if (dist < closestDist) {
                    closestDist = dist;
                    targetPoint = clip.get();
                }
            }
        }
        MuzzleOffset offsetData = MuzzleBoneAutoLoader.getOffset(gunStack.getItem());
        Vec3 naturalMuzzleOffset = offsetData.calculateFirstPersonOffset(shooter);
        Vec3 naturalMuzzlePos = eyePos.add(naturalMuzzleOffset);
        double muzzleForwardDistance = Math.max(0.3, naturalMuzzleOffset.dot(lookAngle));
        if (closestDist <= muzzleForwardDistance + 0.3) {
            event.setOrigin(eyePos.add(lookAngle.scale(0.1)));
            event.setDirection(lookAngle);
            return;
        }
        Vec3 toTarget = targetPoint.subtract(naturalMuzzlePos);
        Vec3 convergentDirection = toTarget.normalize();
        double dot = lookAngle.dot(convergentDirection);
        if (dot < 0.965) {
            double blend = (dot - 0.70) / (0.965 - 0.70);
            blend = Mth.clamp(blend, 0.0, 1.0);
            Vec3 blendedOrigin = eyePos.add(lookAngle.scale(0.1)).lerp(naturalMuzzlePos, blend);
            Vec3 blendedDirection = targetPoint.subtract(blendedOrigin).normalize();
            event.setOrigin(blendedOrigin);
            event.setDirection(blendedDirection);
        } else {
            event.setOrigin(naturalMuzzlePos);
            event.setDirection(convergentDirection);
        }
    }
}