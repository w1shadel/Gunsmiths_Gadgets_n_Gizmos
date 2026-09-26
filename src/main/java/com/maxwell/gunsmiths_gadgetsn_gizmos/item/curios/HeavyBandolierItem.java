package com.maxwell.gunsmiths_gadgetsn_gizmos.item.curios;

import com.maxwell.gunsmiths_gadgetsn_gizmos.item.AmmoPouchItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class HeavyBandolierItem extends AmmoPouchItem {
    public HeavyBandolierItem(Properties properties) {
        super(properties, 6);
    }

    @Override
    public void appendHoverText(@NonNull ItemStack itemStack, @NonNull TooltipContext context, @NonNull List<Component> tooltipComponents, @NonNull TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("gunsmiths_gadgetsn_gizmos.tooltip.heavy_bandolier").withStyle(ChatFormatting.GOLD));
        tooltipComponents.add(Component.translatable("gunsmiths_gadgetsn_gizmos.tooltip.heavy_bandolier.desc").withStyle(ChatFormatting.GRAY));
    }
}