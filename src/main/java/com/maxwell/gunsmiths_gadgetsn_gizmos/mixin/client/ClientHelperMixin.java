package com.maxwell.gunsmiths_gadgetsn_gizmos.mixin.client;

import com.maxwell.gunsmiths_gadgetsn_gizmos.api.gun.MuzzleBoneAutoLoader;
import com.maxwell.gunsmiths_gadgetsn_gizmos.api.gun.MuzzleOffset;
import io.redspace.irons_artifice.client.ClientHelper;
import io.redspace.irons_artifice.network.packets.ClientboundMuzzleFlashPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientHelper.class, remap = false)
public class ClientHelperMixin {

    @Inject(method = "handleMuzzleFlash", at = @At("HEAD"), cancellable = true)
    private static void gunsmiths_gadgetsn_gizmos$customMuzzleFlashFromBone(ClientboundMuzzleFlashPacket msg, CallbackInfo ci) {
        ClientLevel level = Minecraft.getInstance().level;
        Minecraft mc = Minecraft.getInstance();
        if (level == null || mc.player == null) return;

        Entity entity = level.getEntity(msg.entityId());
        if (entity instanceof LivingEntity shooter) {
            ItemStack heldGun = shooter.getMainHandItem();
            if (heldGun.getItem() instanceof io.redspace.irons_artifice.item.GunItem) {
                MuzzleOffset offsetData = MuzzleBoneAutoLoader.getOffset(heldGun.getItem());
                Vec3 calculatedOffset;

                if (shooter == mc.player && mc.options.getCameraType().isFirstPerson()) {
                    calculatedOffset = offsetData.calculateFirstPersonOffset(shooter);
                } else {
                    calculatedOffset = offsetData.calculateThirdPersonOffset(shooter, shooter.getLookAngle());
                }

                Vec3 pos = shooter.getEyePosition().add(calculatedOffset);
                Vec3 motion = msg.entityMotion().scale(0.5);

                if (level.isFluidAtPosition(BlockPos.containing(pos), s -> s.is(FluidTags.WATER))) {
                    for (int i = 0; i < 40; i++) {
                        Vec3 r = new Vec3(level.getRandom().nextDouble() - 0.5, level.getRandom().nextDouble() - 0.5, level.getRandom().nextDouble() - 0.5).scale(3.5);
                        level.addAlwaysVisibleParticle(ParticleTypes.BUBBLE, false, pos.x, pos.y, pos.z, r.x, r.y, r.z);
                    }
                } else {
                    level.addAlwaysVisibleParticle(msg.particle(), true, pos.x, pos.y, pos.z, motion.x, motion.y, motion.z);
                }

                ci.cancel();
            }
        }
    }
}