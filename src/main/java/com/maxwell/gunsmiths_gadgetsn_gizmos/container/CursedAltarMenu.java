package com.maxwell.gunsmiths_gadgetsn_gizmos.container;

import com.maxwell.gunsmiths_gadgetsn_gizmos.block.alter.CursedAltarBlockEntity;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModBlocks;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModItems;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModMenuTypes;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModRecipes;
import com.maxwell.gunsmiths_gadgetsn_gizmos.recipe.CursedAltarRecipe;
import com.maxwell.gunsmiths_gadgetsn_gizmos.recipe.CursedAltarRecipeInput;
import io.redspace.irons_artifice.modifier.ModifierItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
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

    public CursedAltarMenu(int containerId, Inventory inv, RegistryFriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public CursedAltarMenu(int containerId, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.CURSED_ALTAR_MENU.get(), containerId);
        this.blockEntity = (CursedAltarBlockEntity) entity;
        this.player = inv.player;
        this.addSlot(new Slot(blockEntity, INPUT_SLOT_1, 27, 47) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() instanceof ModifierItem || stack.is(ModItems.FORBIDDEN_BLUEPRINT.get());
            }

            @Override
            public void setChanged() {
                super.setChanged();
                CursedAltarMenu.this.slotsChanged(this.container);
            }
        });
        this.addSlot(new Slot(blockEntity, INPUT_SLOT_2, 76, 47) {
            @Override
            public void setChanged() {
                super.setChanged();
                CursedAltarMenu.this.slotsChanged(this.container);
            }
        });
        this.addSlot(new Slot(blockEntity, CATALYST_SLOT, 51, 19) {
            @Override
            public void setChanged() {
                super.setChanged();
                CursedAltarMenu.this.slotsChanged(this.container);
            }
        });
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
        updateCraftingResult();
    }

    private void updateCraftingResult() {
        if (!(blockEntity.getLevel() instanceof ServerLevel serverLevel)) return;
        ItemStack input1 = blockEntity.getItem(INPUT_SLOT_1);
        ItemStack input2 = blockEntity.getItem(INPUT_SLOT_2);
        ItemStack catalyst = blockEntity.getItem(CATALYST_SLOT);
        CursedAltarRecipeInput recipeInput = new CursedAltarRecipeInput(input1, input2, catalyst);
        Optional<RecipeHolder<CursedAltarRecipe>> match = serverLevel.recipeAccess()
                .getRecipeFor(ModRecipes.CURSED_ALTAR_TYPE.get(), recipeInput, serverLevel);
        com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos.LOGGER.info(
                "[CursedAltar] Slots -> Base(左下): {}, Material(右下): {}, Catalyst(上): {} | Matched: {}",
                input1, input2, catalyst, match.isPresent() ? match.get().id() : "NONE"
        );
        if (match.isPresent()) {
            this.activeRecipe = match.get().value();
            ItemStack result = this.activeRecipe.assemble(recipeInput);
            resultContainer.setItem(0, result);
            if (result.is(ModItems.APOSTLE_SUMMON_RITUAL.get())) {
                consumeIngredients();
                resultContainer.setItem(0, ItemStack.EMPTY);
                blockEntity.startRitual();
                if (this.player instanceof ServerPlayer serverPlayer) {
                    serverPlayer.closeContainer();
                }
                return;
            }
        } else {
            this.activeRecipe = null;
            resultContainer.setItem(0, ItemStack.EMPTY);
        }
        this.broadcastChanges();
    }

    private void consumeIngredients() {
        if (this.activeRecipe != null) {
            shrinkAndClean(INPUT_SLOT_1, 1);
            shrinkAndClean(INPUT_SLOT_2, this.activeRecipe.material().count());
            shrinkAndClean(CATALYST_SLOT, this.activeRecipe.catalyst().count());
        } else {
            shrinkAndClean(INPUT_SLOT_1, 1);
            shrinkAndClean(INPUT_SLOT_2, 1);
            shrinkAndClean(CATALYST_SLOT, 1);
        }
        blockEntity.setChanged();
    }

    private void shrinkAndClean(int slot, int amount) {
        ItemStack stack = blockEntity.getItem(slot);
        if (!stack.isEmpty()) {
            stack.shrink(amount);
            if (stack.isEmpty()) {
                blockEntity.setItem(slot, ItemStack.EMPTY);
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
            if (pIndex == RESULT_SLOT) sourceSlot.onTake(playerIn, sourceStack);
        } else {
            if (sourceStack.getItem() instanceof ModifierItem || sourceStack.is(ModItems.FORBIDDEN_BLUEPRINT.get())) {
                if (!moveItemStackTo(sourceStack, INPUT_SLOT_1, INPUT_SLOT_2 + 1, false)) return ItemStack.EMPTY;
            } else {
                if (!moveItemStackTo(sourceStack, INPUT_SLOT_2, CATALYST_SLOT + 1, false)) return ItemStack.EMPTY;
            }
        }
        if (sourceStack.isEmpty()) sourceSlot.set(ItemStack.EMPTY);
        else sourceSlot.setChanged();
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()), pPlayer, ModBlocks.CURSED_ALTAR.get());
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