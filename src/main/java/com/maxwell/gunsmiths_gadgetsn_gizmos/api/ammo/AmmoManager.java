package com.maxwell.gunsmiths_gadgetsn_gizmos.api.ammo;

import com.maxwell.gunsmiths_gadgetsn_gizmos.item.InfiniteAmmoBagItem;
import com.maxwell.gunsmiths_gadgetsn_gizmos.registry.ModAmmoTypes;
import com.maxwell.gunsmiths_gadgetsn_gizmos.registry.ModDataComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class AmmoManager {
    /**
     * 該当ItemStackのAmmoTypeを取得（弾薬でなければnull）
     */
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

    /**
     * プレイヤーの総残弾数を計算（インベントリ直持ち ＋ 全IAmmoContainer内）
     */
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

    /**
     * 装填優先度に従って弾薬タイプを決定（①オフハンド ➔ ②IAmmoContainer内 ➔ ③インベントリ直持ち ➔ ④通常弾）
     */
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

    /**
     * 弾薬を優先度順（IAmmoContainer内 ➔ インベントリ直持ち）に消費し、銃に弾種を記録
     */
    public static void consumeAmmo(Player player, int amount, ItemStack gun) {
        if (InfiniteAmmoBagItem.hasInfiniteBag(player)) {
            return;
        }
        AmmoType activeType = getActiveAmmoType(player);
        Identifier typeId = ModAmmoTypes.REGISTRY.getKey(activeType);
        if (typeId != null) {
            gun.set(ModDataComponents.LOADED_AMMO_TYPE.get(), typeId.toString());
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