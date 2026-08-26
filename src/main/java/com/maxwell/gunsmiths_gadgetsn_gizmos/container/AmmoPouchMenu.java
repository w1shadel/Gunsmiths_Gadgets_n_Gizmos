package com.maxwell.gunsmiths_gadgetsn_gizmos.container;

import com.maxwell.gunsmiths_gadgetsn_gizmos.api.ammo.IAmmoContainer;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModMenuTypes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class AmmoPouchMenu extends AbstractContainerMenu {
    public static final int SLOT_SIZE = 18;
    public final AmmoPouchContainer pouchContainer;
    public final ItemStack pouchStack;
    public final int containerSize;

    public AmmoPouchMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, getPouchFromPlayer(playerInv.player));
    }

    public AmmoPouchMenu(int containerId, Inventory playerInv, ItemStack pouchStack) {
        super(ModMenuTypes.AMMO_POUCH_MENU.get(), containerId);
        this.pouchStack = pouchStack;
        this.pouchContainer = new AmmoPouchContainer(pouchStack);
        this.containerSize = this.pouchContainer.getContainerSize();
        this.pouchContainer.startOpen(playerInv.player);
        int maxPerRow = 9;
        int rows = (this.containerSize - 1) / maxPerRow + 1;
        int centerX = 176 / 2;
        int startY = 20;
        for (int i = 0; i < this.containerSize; i++) {
            int row = i / maxPerRow;
            int col = i % maxPerRow;
            int slotsInRow = Math.min(maxPerRow, this.containerSize - row * maxPerRow);
            int rowLeft = centerX - (slotsInRow * SLOT_SIZE) / 2;
            this.addSlot(new Slot(pouchContainer, i, rowLeft + col * SLOT_SIZE, startY + row * SLOT_SIZE) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return pouchContainer.canPlaceItem(this.index, stack);
                }
            });
        }
        int playerInvStartY = Math.max(51, startY + rows * SLOT_SIZE + 10);
        addPlayerInventory(playerInv, playerInvStartY);
        addPlayerHotbar(playerInv, playerInvStartY + 58);
    }

    private static ItemStack getPouchFromPlayer(Player player) {
        if (player.getMainHandItem().getItem() instanceof IAmmoContainer) {
            return player.getMainHandItem();
        }
        if (player.getOffhandItem().getItem() instanceof IAmmoContainer) {
            return player.getOffhandItem();
        }
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int pIndex) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();
            if (pIndex < this.containerSize) {
                if (!this.moveItemStackTo(stack, this.containerSize, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.moveItemStackTo(stack, 0, this.containerSize, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return result;
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.pouchContainer.stopOpen(player);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return this.pouchContainer.stillValid(player);
    }

    private void addPlayerInventory(Inventory playerInventory, int startY) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, startY + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory, int startY) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, startY));
        }
    }
}