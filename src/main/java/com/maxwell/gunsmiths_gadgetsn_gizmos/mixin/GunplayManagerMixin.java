package com.maxwell.gunsmiths_gadgetsn_gizmos.mixin;

import com.maxwell.gunsmiths_gadgetsn_gizmos.api.ammo.AmmoManager;
import io.redspace.irons_artifice.item.GunplayManager;
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
}