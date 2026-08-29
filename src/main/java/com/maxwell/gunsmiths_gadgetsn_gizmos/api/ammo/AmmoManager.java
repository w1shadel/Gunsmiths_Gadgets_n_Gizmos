package com.maxwell.gunsmiths_gadgetsn_gizmos.api.ammo;

import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModAmmoTypes;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModDataComponents;
import com.maxwell.gunsmiths_gadgetsn_gizmos.item.InfiniteAmmoBagItem;
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
    public static java.util.List<ItemStack> getAllHeldAndCuriosStacks(Player player) {
        java.util.List<ItemStack> list = new java.util.ArrayList<>();

        // 1. オフハンド直持ち（最優先で1回だけ追加）
        ItemStack offhand = player.getOffhandItem();
        if (!offhand.isEmpty()) {
            list.add(offhand);
        }

        // 2. Curios 装備枠
        if (com.maxwell.gunsmiths_gadgetsn_gizmos.compat.curios.CuriosCompat.IS_CURIOS_LOADED) {
            top.theillusivec4.curios.api.CuriosApi.getCuriosInventory(player).ifPresent(inv -> {
                inv.findCurios(stack -> !stack.isEmpty()).forEach(slotResult -> {
                    list.add(slotResult.stack());
                });
            });
        }

        // 3. 通常インベントリ（★ 0〜35番スロットのメイン枠のみ走査してオフハンド重複を防止）
        for (int i = 0; i < 36; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty()) {
                list.add(stack);
            }
        }
        return list;
    }

    public static int countPlayerAmmo(Player player) {
        if (InfiniteAmmoBagItem.hasInfiniteBag(player)) {
            return 9999;
        }
        int total = 0;

        for (ItemStack stack : getAllHeldAndCuriosStacks(player)) {
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
        if (offhand != null && offhand != ModAmmoTypes.DEFAULT.get()) return offhand;

        for (ItemStack stack : getAllHeldAndCuriosStacks(player)) {
            if (stack.getItem() instanceof IAmmoContainer container) {
                for (ItemStack item : container.getStoredAmmo(stack)) {
                    AmmoType type = findAmmoType(item);

                    if (type != null && type != ModAmmoTypes.DEFAULT.get()) {
                        return type;
                    }
                }
            }
        }

        for (ItemStack stack : getAllHeldAndCuriosStacks(player)) {
            AmmoType type = findAmmoType(stack);
            if (type != null && type != ModAmmoTypes.DEFAULT.get()) {
                return type;
            }
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
        AmmoType targetType = getActiveAmmoType(player);

        for (ItemStack stack : getAllHeldAndCuriosStacks(player)) {
            if (remaining <= 0) break;
            if (stack.getItem() instanceof IAmmoContainer container) {
                NonNullList<ItemStack> items = container.getStoredAmmo(stack);
                boolean changed = false;
                for (ItemStack slotItem : items) {
                    if (findAmmoType(slotItem) == targetType) {
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
            for (int i = 0; i < 36 && remaining > 0; i++) {
                ItemStack stack = player.getInventory().getItem(i);
                if (findAmmoType(stack) == targetType) {
                    int take = Math.min(remaining, stack.getCount());
                    stack.shrink(take);
                    remaining -= take;
                }
            }
        }
    }
}