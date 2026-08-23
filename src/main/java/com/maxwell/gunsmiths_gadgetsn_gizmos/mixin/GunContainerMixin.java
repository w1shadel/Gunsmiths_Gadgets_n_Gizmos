package com.maxwell.gunsmiths_gadgetsn_gizmos.mixin;

import com.maxwell.gunsmiths_gadgetsn_gizmos.registry.ModDataComponents;
import io.redspace.irons_artifice.menu.GunContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GunContainer.class)
public class GunContainerMixin {
    @Inject(method = "sizeFromStack", at = @At("RETURN"), cancellable = true, remap = false)
    private static void gunsmiths_gadgetsn_gizmos$expandModifierSlots(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        int baseSlots = cir.getReturnValue();
        if (baseSlots > 0 && stack.has(ModDataComponents.EXTRA_MODIFIER_SLOTS.get())) {
            int extraSlots = stack.getOrDefault(ModDataComponents.EXTRA_MODIFIER_SLOTS.get(), 0);
            cir.setReturnValue(baseSlots + extraSlots);
        }
    }
}