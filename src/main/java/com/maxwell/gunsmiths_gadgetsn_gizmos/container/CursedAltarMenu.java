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
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;

public class CursedAltarMenu extends AbstractContainerMenu {
    public static final int INPUT_SLOT_1 = 0;
    public static final int INPUT_SLOT_2 = 1;
    public static final int CATALYST_SLOT = 2;
    public static final int RESULT_SLOT = 3;
    private static final int INPUT_SLOTS_COUNT = 3;
    public final CursedAltarBlockEntity blockEntity;
    private final SimpleContainer inputContainer;
    private final ResultContainer resultContainer = new ResultContainer();
    private @Nullable CursedAltarRecipe activeRecipe = null;

    public CursedAltarMenu(int containerId, Inventory inv, RegistryFriendlyByteBuf extraData) {
        this(containerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public CursedAltarMenu(int containerId, Inventory inv, BlockEntity entity) {
        super(ModMenuTypes.CURSED_ALTAR_MENU.get(), containerId);
        this.blockEntity = (CursedAltarBlockEntity) entity;
        this.inputContainer = new SimpleContainer(INPUT_SLOTS_COUNT) {
            @Override
            public void setChanged() {
                super.setChanged();
                CursedAltarMenu.this.slotsChanged(this);
            }
        };
        for (int i = 0; i < INPUT_SLOTS_COUNT; i++) {
            this.inputContainer.setItem(i, blockEntity.getItem(i));
        }
        this.addSlot(new Slot(inputContainer, INPUT_SLOT_1, 27, 47) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.getItem() instanceof ModifierItem
                        || stack.is(ModItems.FORBIDDEN_BLUEPRINT.get());
            }
        });
        this.addSlot(new Slot(inputContainer, INPUT_SLOT_2, 76, 47));
        this.addSlot(new Slot(inputContainer, CATALYST_SLOT, 51, 19) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ModItems.OMINOUS_CLOCKWORK_CORE.get())
                        || stack.is(ModItems.COAGULATED_OMEN_BLOOD.get())
                        || stack.is(ModItems.VOID_CASING.get())
                        || stack.is(ModItems.SOUL_CINDER.get());
            }
        });
        this.addSlot(new Slot(resultContainer, 0, 134, 47) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return false;
            }

            @Override
            public void onTake(@NotNull Player player, @NotNull ItemStack stack) {
                consumeIngredientsOnTake();
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

    private static java.util.List<CursedAltarRecipe> loadAllAltarRecipes(ServerLevel level) {
        java.util.List<CursedAltarRecipe> list = new java.util.ArrayList<>();
        try {
            var resourceManager = level.getServer().getResourceManager();
            var resources = resourceManager.listResources("recipe", id -> true);
            var ops = level.registryAccess().createSerializationContext(com.mojang.serialization.JsonOps.INSTANCE);
            for (var entry : resources.entrySet()) {
                try (var reader = entry.getValue().openAsReader()) {
                    var json = com.google.gson.JsonParser.parseReader(reader);
                    var res = net.minecraft.world.item.crafting.Recipe.CONDITIONAL_CODEC.parse(ops, json);
                    res.ifSuccess(opt -> opt.ifPresent(withCond -> {
                        if (withCond.carrier() instanceof CursedAltarRecipe altarRecipe) {
                            list.add(altarRecipe);
                        }
                    }));
                } catch (Exception ignored) {
                }
            }
        } catch (Exception ignored) {
        }
        return list;
    }

    @Override
    public void slotsChanged(@NotNull Container container) {
        super.slotsChanged(container);
        if (!blockEntity.getLevel().isClientSide()) {
            updateCraftingResult();
        }
    }

    private void updateCraftingResult() {
        Level level = blockEntity.getLevel();
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        ItemStack input1 = inputContainer.getItem(INPUT_SLOT_1);
        ItemStack input2 = inputContainer.getItem(INPUT_SLOT_2);
        ItemStack catalyst = inputContainer.getItem(CATALYST_SLOT);
        CursedAltarRecipeInput recipeInput = new CursedAltarRecipeInput(input1, input2, catalyst);
        Optional<RecipeHolder<CursedAltarRecipe>> match = serverLevel.recipeAccess()
                .getRecipeFor(ModRecipes.CURSED_ALTAR_TYPE.get(), recipeInput, serverLevel);
        CursedAltarRecipe matchedRecipe = match.map(RecipeHolder::value).orElse(null);
        if (matchedRecipe == null) {
            for (CursedAltarRecipe recipe : loadAllAltarRecipes(serverLevel)) {
                if (recipe.matches(recipeInput, serverLevel)) {
                    matchedRecipe = recipe;
                    break;
                }
            }
        }
        if (matchedRecipe != null) {
            this.activeRecipe = matchedRecipe;
            ItemStack result = matchedRecipe.assemble(recipeInput);
            resultContainer.setItem(0, result);
            blockEntity.setItem(RESULT_SLOT, result);
        } else {
            this.activeRecipe = null;
            resultContainer.setItem(0, ItemStack.EMPTY);
            blockEntity.setItem(RESULT_SLOT, ItemStack.EMPTY);
        }
    }

    private void consumeIngredientsOnTake() {
        if (this.activeRecipe != null) {
            shrinkSlot(INPUT_SLOT_1, 1);
            shrinkSlot(INPUT_SLOT_2, this.activeRecipe.material().count());
            shrinkSlot(CATALYST_SLOT, this.activeRecipe.catalyst().count());
        } else {
            shrinkSlot(INPUT_SLOT_1, 1);
            shrinkSlot(INPUT_SLOT_2, 1);
            shrinkSlot(CATALYST_SLOT, 1);
        }
    }

    private void shrinkSlot(int slotIndex, int count) {
        ItemStack stack = inputContainer.getItem(slotIndex);
        if (!stack.isEmpty()) {
            stack.shrink(count);
            inputContainer.setItem(slotIndex, stack);
            blockEntity.setItem(slotIndex, stack);
        }
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
            if (sourceStack.getItem() instanceof ModifierItem || sourceStack.is(ModItems.FORBIDDEN_BLUEPRINT.get())) {
                if (!moveItemStackTo(sourceStack, INPUT_SLOT_1, INPUT_SLOT_2 + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (sourceStack.is(ModItems.OMINOUS_CLOCKWORK_CORE.get())
                    || sourceStack.is(ModItems.COAGULATED_OMEN_BLOOD.get())
                    || sourceStack.is(ModItems.VOID_CASING.get())
                    || sourceStack.is(ModItems.SOUL_CINDER.get())) {
                if (!moveItemStackTo(sourceStack, CATALYST_SLOT, CATALYST_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!moveItemStackTo(sourceStack, INPUT_SLOT_2, INPUT_SLOT_2 + 1, false)) {
                    return ItemStack.EMPTY;
                }
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
    public void removed(@NotNull Player player) {
        super.removed(player);
        for (int i = 0; i < INPUT_SLOTS_COUNT; i++) {
            blockEntity.setItem(i, inputContainer.getItem(i));
        }
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()),
                pPlayer, ModBlocks.CURSED_ALTAR.get());
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