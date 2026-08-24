package com.maxwell.gunsmiths_gadgetsn_gizmos.item.curios;

import com.maxwell.gunsmiths_gadgetsn_gizmos.item.AmmoPouchItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class HeavyBandolierItem extends AmmoPouchItem {
    public HeavyBandolierItem(Properties properties) {
        super(properties, 6);
    }

    @Override
    public void appendHoverText(@NonNull ItemStack itemStack, @NonNull TooltipContext context, @NonNull TooltipDisplay display, @NonNull Consumer<Component> builder, @NonNull TooltipFlag tooltipFlag) {
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.tooltip.heavy_bandolier").withStyle(ChatFormatting.GOLD));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.tooltip.heavy_bandolier.desc").withStyle(ChatFormatting.GRAY));
    }
}