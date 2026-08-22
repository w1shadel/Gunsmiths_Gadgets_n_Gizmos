package com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.on_hit;

import io.redspace.irons_artifice.entity.Bullet;
import io.redspace.irons_artifice.gun.HitEntityAccumulator;
import io.redspace.irons_artifice.modifier.OnHitEffect;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class GrapplingAnchorOnHit implements OnHitEffect {
    @Override
    public void onHit(ServerLevel level, Bullet bullet, HitResult hitResult, HitEntityAccumulator accumulator) {
        if (!(hitResult instanceof BlockHitResult blockHit)) {
            return;
        }
        Entity owner = bullet.getOwner();
        if (!(owner instanceof LivingEntity livingOwner) || !livingOwner.isAlive()) {
            return;
        }
        Vec3 targetPos = hitResult.getLocation();
        Vec3 ownerPos = livingOwner.position();
        Vec3 pullVec = targetPos.subtract(ownerPos);
        double distance = pullVec.length();
        if (distance > 1.0) {
            Vec3 motion = pullVec.normalize().scale(Math.min(1.8, distance * 0.25));
            motion = motion.add(0, 0.2, 0);
            livingOwner.setDeltaMovement(motion);
            livingOwner.hurtMarked = true;
            livingOwner.resetFallDistance();
            level.playSound(null, ownerPos.x, ownerPos.y, ownerPos.z,
                    SoundEvents.CHAIN_FALL, SoundSource.PLAYERS, 1.0f, 1.2f);
        }
    }
}