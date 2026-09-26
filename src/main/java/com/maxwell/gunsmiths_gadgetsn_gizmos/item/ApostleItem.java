package com.maxwell.gunsmiths_gadgetsn_gizmos.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class ApostleItem extends Item {
    public ApostleItem(Properties properties) {
        super(properties
                .stacksTo(1)
                .component(net.minecraft.core.component.DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("gunsmiths_gadgetsn_gizmos.tooltip.apostleitem").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
    }
}