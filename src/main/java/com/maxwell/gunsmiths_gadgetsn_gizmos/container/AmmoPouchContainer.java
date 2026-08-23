package com.maxwell.gunsmiths_gadgetsn_gizmos.container;

import com.maxwell.gunsmiths_gadgetsn_gizmos.api.ammo.AmmoManager;
import com.maxwell.gunsmiths_gadgetsn_gizmos.api.ammo.IAmmoContainer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public class AmmoPouchContainer extends SimpleContainer {
    private final ItemStack pouchStack;

    public AmmoPouchContainer(ItemStack pouchStack) {
        super(sizeFromStack(pouchStack));
        this.pouchStack = pouchStack;
        ItemContainerContents contents = pouchStack.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY);
        contents.copyInto(this.getItems());
    }

    private static int sizeFromStack(ItemStack stack) {
        if (stack.getItem() instanceof IAmmoContainer container) {
            return container.getPouchSlotCount(stack);
        }
        return 4;
    }

    public ItemStack getPouchStack() {
        return pouchStack;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        this.pouchStack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.getItems()));
    }

    @Override
    public boolean stillValid(Player player) {
        return !player.isRemoved() && (player.getMainHandItem() == pouchStack || player.getOffhandItem() == pouchStack);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        if (pouchStack.getItem() instanceof IAmmoContainer container) {
            return container.canHoldAmmo(stack);
        }
        return AmmoManager.isAmmo(stack);
    }
}