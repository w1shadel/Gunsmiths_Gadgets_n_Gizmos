package com.maxwell.gunsmiths_gadgetsn_gizmos.container;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.block.alter.CursedAltarBlockEntity;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModBlocks;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModItems;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModMenuTypes;
import com.maxwell.gunsmiths_gadgetsn_gizmos.recipe.CursedAltarRecipe;
import com.maxwell.gunsmiths_gadgetsn_gizmos.recipe.CursedAltarRecipeManager;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

public class CursedAltarMenu extends AbstractContainerMenu {
    public static final int INPUT_SLOT_1 = 0;
    public static final int INPUT_SLOT_2 = 1;
    public static final int CATALYST_SLOT = 2;
    public static final int RESULT_SLOT = 3;
    public final CursedAltarBlockEntity blockEntity;
    private final Player player;
    private final ResultContainer resultContainer = new ResultContainer();
    private @Nullable CursedAltarRecipe activeRecipe = null;
    private boolean isCrafting = false;
    public CursedAltarMenu(int containerId, Inventory inv, RegistryFriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }    private final SimpleContainer inputContainer = new SimpleContainer(3) {
        @Override
        public void setChanged() {
            super.setChanged();
            if (!isCrafting) {
                CursedAltarMenu.this.slotsChanged(this);
            }
        }
    };

    public CursedAltarMenu(int containerId, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.CURSED_ALTAR_MENU.get(), containerId);
        this.blockEntity = (CursedAltarBlockEntity) entity;
        this.player = inv.player;
        if (this.blockEntity != null) {
            this.inputContainer.setItem(INPUT_SLOT_1, this.blockEntity.getItem(INPUT_SLOT_1));
            this.inputContainer.setItem(INPUT_SLOT_2, this.blockEntity.getItem(INPUT_SLOT_2));
            this.inputContainer.setItem(CATALYST_SLOT, this.blockEntity.getItem(CATALYST_SLOT));
        }
        this.addSlot(new Slot(inputContainer, INPUT_SLOT_1, 27, 47));
        this.addSlot(new Slot(inputContainer, INPUT_SLOT_2, 76, 47));
        this.addSlot(new Slot(inputContainer, CATALYST_SLOT, 51, 19));
        this.addSlot(new Slot(resultContainer, 0, 134, 47) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                consumeIngredients();
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.0F, 1.2F);
                player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                        SoundEvents.ANVIL_USE, SoundSource.BLOCKS, 0.8F, 0.5F);
                updateCraftingResult();
                super.onTake(player, stack);
            }
        });
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
        updateCraftingResult();
    }

    @Override
    public void slotsChanged(@NotNull Container container) {
        super.slotsChanged(container);
        if (this.blockEntity != null) {
            this.blockEntity.setItem(INPUT_SLOT_1, this.inputContainer.getItem(INPUT_SLOT_1));
            this.blockEntity.setItem(INPUT_SLOT_2, this.inputContainer.getItem(INPUT_SLOT_2));
            this.blockEntity.setItem(CATALYST_SLOT, this.inputContainer.getItem(CATALYST_SLOT));
            this.blockEntity.setChanged();
        }
        updateCraftingResult();
    }

    private void updateCraftingResult() {
        ItemStack input1 = inputContainer.getItem(INPUT_SLOT_1);
        ItemStack input2 = inputContainer.getItem(INPUT_SLOT_2);
        ItemStack catalyst = inputContainer.getItem(CATALYST_SLOT);
        Optional<CursedAltarRecipe> match = CursedAltarRecipeManager.findMatchingRecipe(input1, input2, catalyst);
        if (match.isPresent()) {
            this.activeRecipe = match.get();
            ItemStack result = this.activeRecipe.result().copy();
            resultContainer.setItem(0, result);
            GunsmithsGadgetsnGizmos.LOGGER.info("[CursedAltar] MATCHED: {} -> Result: {}", this.activeRecipe.id(), result);
            if (result.is(ModItems.APOSTLE_SUMMON_RITUAL.get())) {
                if (!this.player.level().isClientSide()) {
                    consumeIngredients();
                    resultContainer.setItem(0, ItemStack.EMPTY);
                    if (blockEntity != null) {
                        blockEntity.startRitual();
                    }
                    if (this.player instanceof ServerPlayer serverPlayer) {
                        serverPlayer.closeContainer();
                    }
                }
                return;
            }
        } else {
            this.activeRecipe = null;
            resultContainer.setItem(0, ItemStack.EMPTY);
        }
    }

    private void consumeIngredients() {
        this.isCrafting = true;
        try {
            if (this.activeRecipe != null) {
                ItemStack matStack = inputContainer.getItem(INPUT_SLOT_2);
                shrinkAndClean(INPUT_SLOT_1, 1);
                if (this.activeRecipe.materialItems().contains(matStack.getItem())) {
                    shrinkAndClean(INPUT_SLOT_2, this.activeRecipe.materialCount());
                    shrinkAndClean(CATALYST_SLOT, this.activeRecipe.catalystCount());
                } else {
                    shrinkAndClean(CATALYST_SLOT, this.activeRecipe.materialCount());
                    shrinkAndClean(INPUT_SLOT_2, this.activeRecipe.catalystCount());
                }
            } else {
                shrinkAndClean(INPUT_SLOT_1, 1);
                shrinkAndClean(INPUT_SLOT_2, 1);
                shrinkAndClean(CATALYST_SLOT, 1);
            }
            if (blockEntity != null) {
                blockEntity.setItem(INPUT_SLOT_1, inputContainer.getItem(INPUT_SLOT_1));
                blockEntity.setItem(INPUT_SLOT_2, inputContainer.getItem(INPUT_SLOT_2));
                blockEntity.setItem(CATALYST_SLOT, inputContainer.getItem(CATALYST_SLOT));
                blockEntity.setChanged();
            }
        } finally {
            this.isCrafting = false;
        }
    }

    private void shrinkAndClean(int slot, int amount) {
        ItemStack stack = inputContainer.getItem(slot);
        if (!stack.isEmpty()) {
            stack.shrink(amount);
            if (stack.isEmpty()) {
                inputContainer.setItem(slot, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();
        if (pIndex < 4) {
            if (!moveItemStackTo(sourceStack, 4, slots.size(), true)) return ItemStack.EMPTY;
            if (pIndex == RESULT_SLOT) {
                sourceSlot.onTake(playerIn, sourceStack);
            }
        } else {
            if (!moveItemStackTo(sourceStack, INPUT_SLOT_1, CATALYST_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }
        }
        if (sourceStack.isEmpty()) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        if (this.blockEntity != null) {
            return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), pPlayer, ModBlocks.CURSED_ALTAR.get());
        }
        return true;
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