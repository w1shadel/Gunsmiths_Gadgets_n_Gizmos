package com.maxwell.gunsmiths_gadgetsn_gizmos.item;

import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class InfiniteAmmoBagItem extends Item {
    public InfiniteAmmoBagItem(Properties properties) {
        super(properties
                .stacksTo(1)
                .component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
        );
    }

    public static boolean hasInfiniteBag(Player player) {
        if (com.maxwell.gunsmiths_gadgetsn_gizmos.compat.curios.CuriosCompat.isEquipped(player, ModItems.INFINITE_AMMO_BAG.get())) {
            return true;
        }
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof InfiniteAmmoBagItem) {
                return true;
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public void appendHoverText(@NonNull ItemStack itemStack, @NonNull TooltipContext context, @NonNull TooltipDisplay display, @NonNull Consumer<Component> builder, @NonNull TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.tooltip.infinite_ammo_bag")
                .withStyle(ChatFormatting.GOLD));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.tooltip.infinite_ammo_bag.desc")
                .withStyle(ChatFormatting.GRAY));
    }
}