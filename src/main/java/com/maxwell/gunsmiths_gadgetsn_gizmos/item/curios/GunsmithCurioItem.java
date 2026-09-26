package com.maxwell.gunsmiths_gadgetsn_gizmos.item.curios;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.function.Consumer;

public class GunsmithCurioItem extends Item {
    private final String tooltipKey;

    public GunsmithCurioItem(Properties properties, String tooltipKey) {
        super(properties.stacksTo(1));
        this.tooltipKey = tooltipKey;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("gunsmiths_gadgetsn_gizmos.tooltip." + tooltipKey).withStyle(ChatFormatting.GOLD));
        tooltipComponents.add(Component.translatable("gunsmiths_gadgetsn_gizmos.tooltip." + tooltipKey + ".desc").withStyle(ChatFormatting.GRAY));
    }
}