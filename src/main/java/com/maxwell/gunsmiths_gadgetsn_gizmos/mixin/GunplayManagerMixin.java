package com.maxwell.gunsmiths_gadgetsn_gizmos.mixin;

import com.maxwell.gunsmiths_gadgetsn_gizmos.api.ammo.AmmoManager;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModItems;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.gun.ShotProfile;
import io.redspace.irons_artifice.item.GunplayManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GunplayManager.class)
public class GunplayManagerMixin {
    @Inject(method = "countBullets", at = @At("HEAD"), cancellable = true, remap = false)
    private static void gunsmiths_gadgetsn_gizmos$countAllBullets(Player player, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(AmmoManager.countPlayerAmmo(player));
    }

    @Inject(method = "consumeBullets", at = @At("HEAD"), cancellable = true, remap = false)
    private static void gunsmiths_gadgetsn_gizmos$consumeAllAmmoTypes(Player player, int amount, CallbackInfo ci) {
        ItemStack heldGun = player.getMainHandItem();
        AmmoManager.consumeAmmo(player, amount, heldGun);
        ci.cancel();
    }

    @Inject(method = "getSpreadForEntity", at = @At("RETURN"), cancellable = true, remap = false)
    private static void gunsmiths_gadgetsn_gizmos$cancelMovementSpreadWithSpurs(ShotProfile shotProfile, Entity entity, CallbackInfoReturnable<Float> cir) {
        if (entity instanceof LivingEntity living) {
            if (com.maxwell.gunsmiths_gadgetsn_gizmos.compat.curios.CuriosCompat.isEquipped(living, ModItems.GUNSLINGERS_SPURS.get())) {
                float baseSpread = (float) shotProfile.value(ShotComponents.SPREAD);
                if (living.isCrouching()) {
                    baseSpread *= 0.667F;
                }
                cir.setReturnValue(Math.max(0.0F, baseSpread));
            }
        }
    }

    @Inject(method = "attemptFinishReload", at = @At("HEAD"), remap = false)
    private static void gunsmiths_gadgetsn_gizmos$recordLoadedAmmoType(LivingEntity living, ItemStack gun, int roundsToLoad, CallbackInfoReturnable<?> cir) {
        if (living instanceof Player player && !player.level().isClientSide()) {
            AmmoManager.applyLoadedAmmoType(player, gun);
        }
    }

}