package com.maxwell.gunsmiths_gadgetsn_gizmos.api.ammo;

import com.maxwell.gunsmiths_gadgetsn_gizmos.item.InfiniteAmmoBagItem;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModAmmoTypes;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModDataComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class AmmoManager {
    public static @Nullable AmmoType findAmmoType(ItemStack stack) {
        if (stack.isEmpty()) return null;
        for (AmmoType type : ModAmmoTypes.REGISTRY) {
            if (stack.is(type.getAmmoItem())) {
                return type;
            }
        }
        return null;
    }

    public static boolean isAmmo(ItemStack stack) {
        return findAmmoType(stack) != null;
    }

    public static int countPlayerAmmo(Player player) {
        if (InfiniteAmmoBagItem.hasInfiniteBag(player)) {
            return 9999;
        }
        int total = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof IAmmoContainer container) {
                for (ItemStack item : container.getStoredAmmo(stack)) {
                    if (isAmmo(item)) {
                        total += item.getCount();
                    }
                }
            } else if (isAmmo(stack)) {
                total += stack.getCount();
            }
        }
        return total;
    }

    public static AmmoType getActiveAmmoType(Player player) {
        AmmoType offhand = findAmmoType(player.getOffhandItem());
        if (offhand != null) return offhand;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof IAmmoContainer container) {
                for (ItemStack item : container.getStoredAmmo(stack)) {
                    AmmoType type = findAmmoType(item);
                    if (type != null) return type;
                }
            }
        }
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            AmmoType type = findAmmoType(stack);
            if (type != null) return type;
        }
        return ModAmmoTypes.DEFAULT.get();
    }
    public static void applyLoadedAmmoType(Player player, ItemStack gun) {
        AmmoType activeType = getActiveAmmoType(player);
        Identifier typeId = ModAmmoTypes.REGISTRY.getKey(activeType);
        if (typeId != null) {
            gun.set(ModDataComponents.LOADED_AMMO_TYPE.get(), typeId.toString());
        }
    }
    public static void consumeAmmo(Player player, int amount, ItemStack gun) {
        if (InfiniteAmmoBagItem.hasInfiniteBag(player) || player.hasInfiniteMaterials()) {
            return; 
        }

        int remaining = amount;

        for (int i = 0; i < player.getInventory().getContainerSize() && remaining > 0; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.getItem() instanceof IAmmoContainer container) {
                NonNullList<ItemStack> items = container.getStoredAmmo(stack);
                boolean changed = false;

                for (ItemStack slotItem : items) {
                    if (isAmmo(slotItem)) {
                        int take = Math.min(remaining, slotItem.getCount());
                        slotItem.shrink(take);
                        remaining -= take;
                        changed = true;
                        if (remaining <= 0) break;
                    }
                }

                if (changed) {
                    container.setStoredAmmo(stack, items);
                }
            }
        }

        if (remaining > 0) {
            for (int i = 0; i < player.getInventory().getContainerSize() && remaining > 0; i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (isAmmo(stack)) {
                    int take = Math.min(remaining, stack.getCount());
                    stack.shrink(take);
                    remaining -= take;
                }
            }
        }
    }
}