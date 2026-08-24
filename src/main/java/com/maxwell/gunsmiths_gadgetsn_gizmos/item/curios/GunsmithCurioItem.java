package com.maxwell.gunsmiths_gadgetsn_gizmos.item.curios;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class GunsmithCurioItem extends Item {
    private final String tooltipKey;

    public GunsmithCurioItem(Properties properties, String tooltipKey) {
        super(properties.stacksTo(1));
        this.tooltipKey = tooltipKey;
    }

    @Override
    public void appendHoverText(@NonNull ItemStack itemStack, @NonNull TooltipContext context, @NonNull TooltipDisplay display, @NonNull Consumer<Component> builder, @NonNull TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.tooltip." + tooltipKey).withStyle(ChatFormatting.GOLD));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.tooltip." + tooltipKey + ".desc").withStyle(ChatFormatting.GRAY));
    }
}