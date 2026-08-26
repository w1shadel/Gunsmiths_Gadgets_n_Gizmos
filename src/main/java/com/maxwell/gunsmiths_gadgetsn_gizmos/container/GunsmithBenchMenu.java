package com.maxwell.gunsmiths_gadgetsn_gizmos.container;

import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModBlocks;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModDataComponents;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModItems;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModMenuTypes;
import io.redspace.irons_artifice.item.GunItem;
import io.redspace.irons_artifice.registry.ItemRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class GunsmithBenchMenu extends AbstractContainerMenu {
    public static final int GUN_SLOT = 0;
    public static final int KIT_SLOT = 1;
    public static final int PARTS_SLOT = 2;
    public static final int RESULT_SLOT = 3;
    public static final int MAX_EXTRA_SLOTS = 3;
    public static final int REQUIRED_PARTS_COUNT = 8;
    private final ContainerLevelAccess access;
    private final ResultContainer resultContainer = new ResultContainer();

    public GunsmithBenchMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, ContainerLevelAccess.NULL);
    }

    public GunsmithBenchMenu(int containerId, Inventory playerInv, ContainerLevelAccess access) {
        super(ModMenuTypes.GUNSMITH_BENCH_MENU.get(), containerId);
        this.access = access;
        this.addSlot(new Slot(inputContainer, GUN_SLOT, 27, 47) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() instanceof GunItem;
            }
        });
        this.addSlot(new Slot(inputContainer, KIT_SLOT, 51, 19) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ModItems.GUNSMITH_CHASSIS_KIT.get());
            }
        });
        this.addSlot(new Slot(inputContainer, PARTS_SLOT, 76, 47) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ItemRegistry.MECHANICAL_COMPONENTS.get())
                        || stack.is(ItemRegistry.CLOCKWORK_COMPONENTS.get());
            }
        });
        this.addSlot(new Slot(resultContainer, 0, 134, 47) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                inputContainer.getItem(GUN_SLOT).shrink(1);
                inputContainer.getItem(KIT_SLOT).shrink(1);
                inputContainer.getItem(PARTS_SLOT).shrink(REQUIRED_PARTS_COUNT);
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
                updateResult();
                super.onTake(player, stack);
            }
        });
        addPlayerInventory(playerInv);
        addPlayerHotbar(playerInv);
    }    private final SimpleContainer inputContainer = new SimpleContainer(3) {
        @Override
        public void setChanged() {
            super.setChanged();
            GunsmithBenchMenu.this.slotsChanged(this);
        }
    };

    @Override
    public void slotsChanged(@NotNull Container container) {
        super.slotsChanged(container);
        updateResult();
    }

    private void updateResult() {
        ItemStack gun = inputContainer.getItem(GUN_SLOT);
        ItemStack kit = inputContainer.getItem(KIT_SLOT);
        ItemStack parts = inputContainer.getItem(PARTS_SLOT);
        if (gun.getItem() instanceof GunItem && kit.is(ModItems.GUNSMITH_CHASSIS_KIT.get()) && parts.getCount() >= REQUIRED_PARTS_COUNT) {
            int currentExtra = gun.getOrDefault(ModDataComponents.EXTRA_MODIFIER_SLOTS.get(), 0);
            if (currentExtra < MAX_EXTRA_SLOTS) {
                ItemStack upgradedGun = gun.copy();
                upgradedGun.set(ModDataComponents.EXTRA_MODIFIER_SLOTS.get(), currentExtra + 1);
                resultContainer.setItem(0, upgradedGun);
                return;
            }
        }
        resultContainer.setItem(0, ItemStack.EMPTY);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        if (pIndex < 4) {
            if (!moveItemStackTo(sourceStack, 4, slots.size(), true)) {
                return ItemStack.EMPTY;
            }
            if (pIndex == RESULT_SLOT) {
                sourceSlot.onTake(playerIn, sourceStack);
            }
        } else {
            if (sourceStack.getItem() instanceof GunItem) {
                if (!moveItemStackTo(sourceStack, GUN_SLOT, GUN_SLOT + 1, false)) return ItemStack.EMPTY;
            } else if (sourceStack.is(ModItems.GUNSMITH_CHASSIS_KIT.get())) {
                if (!moveItemStackTo(sourceStack, KIT_SLOT, KIT_SLOT + 1, false)) return ItemStack.EMPTY;
            } else if (sourceStack.is(ItemRegistry.MECHANICAL_COMPONENTS.get()) || sourceStack.is(ItemRegistry.CLOCKWORK_COMPONENTS.get())) {
                if (!moveItemStackTo(sourceStack, PARTS_SLOT, PARTS_SLOT + 1, false)) return ItemStack.EMPTY;
            } else {
                return ItemStack.EMPTY;
            }
        }
        if (sourceStack.isEmpty()) sourceSlot.set(ItemStack.EMPTY);
        else sourceSlot.setChanged();
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(this.access, player, ModBlocks.GUNSMITH_BENCH.get());
    }

    @Override
    public void removed(@NotNull Player player) {
        super.removed(player);
        this.clearContainer(player, inputContainer);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 84 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }



}