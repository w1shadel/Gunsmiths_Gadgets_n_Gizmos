package com.maxwell.gunsmiths_gadgetsn_gizmos.api.ammo;

import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public interface IAmmoContainer {
    int getPouchSlotCount(ItemStack containerStack);

    default NonNullList<ItemStack> getStoredAmmo(ItemStack containerStack) {
        int slots = getPouchSlotCount(containerStack);
        NonNullList<ItemStack> items = NonNullList.withSize(slots, ItemStack.EMPTY);
        ItemContainerContents contents = containerStack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        contents.copyInto(items);
        return items;
    }

    default void setStoredAmmo(ItemStack containerStack, NonNullList<ItemStack> items) {
        containerStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(items));
    }

    default boolean canHoldAmmo(ItemStack stack) {
        return AmmoManager.isAmmo(stack);
    }
}