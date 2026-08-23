package com.maxwell.gunsmiths_gadgetsn_gizmos.block.alter;

import com.maxwell.gunsmiths_gadgetsn_gizmos.container.CursedAltarMenu;
import com.maxwell.gunsmiths_gadgetsn_gizmos.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class CursedAltarBlockEntity extends BlockEntity implements MenuProvider {
    public static final int SLOT_COUNT = 4;
    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

    public CursedAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CURSED_ALTAR_BE.get(), pos, state);
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    public void setItem(int slot, ItemStack stack) {
        items.set(slot, stack);
        setChanged();
    }

    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    @Override
    public @NonNull Component getDisplayName() {
        return Component.translatable("block.gunsmiths_gadgetsn_gizmos.cursed_altar");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int containerId, @NonNull Inventory playerInventory, @NonNull Player player) {
        return new CursedAltarMenu(containerId, playerInventory, this);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.items.clear();
        ContainerHelper.loadAllItems(input, this.items);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items);
    }
}