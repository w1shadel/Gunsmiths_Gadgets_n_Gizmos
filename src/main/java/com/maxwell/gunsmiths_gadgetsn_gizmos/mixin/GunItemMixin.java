package com.maxwell.gunsmiths_gadgetsn_gizmos.mixin;

import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModDataComponents;
import io.redspace.irons_artifice.item.GunItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GunItem.class)
public class GunItemMixin {
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void gunsmiths_gadgetsn_gizmos$showExtraSlots(@NonNull ItemStack itemStack, Item.@NonNull TooltipContext context, @NonNull List<Component> tooltipComponents, @NonNull TooltipFlag tooltipFlag, CallbackInfo ci) {
        if (itemStack.has(ModDataComponents.EXTRA_MODIFIER_SLOTS.get())) {
            int extra = itemStack.getOrDefault(ModDataComponents.EXTRA_MODIFIER_SLOTS.get(), 0);
            if (extra > 0) {
                tooltipComponents.add(Component.translatable("gunsmiths_gadgetsn_gizmos.tooltip.extra_slots", extra)
                        .withStyle(ChatFormatting.AQUA));
            }
        }

        var bonuses = com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy.GunSetBonusManager.getMatchingBonuses(itemStack);
        if (!bonuses.isEmpty()) {
            tooltipComponents.add(Component.empty());
            for (var bonus : bonuses) {
                tooltipComponents.add(Component.literal("★ ")
                        .append(Component.translatable(bonus.nameKey()))
                        .withStyle(ChatFormatting.GREEN, ChatFormatting.BOLD));
                tooltipComponents.add(Component.literal("  ")
                        .append(Component.translatable(bonus.descKey()))
                        .withStyle(ChatFormatting.DARK_GREEN));
            }
        }
    }
}