package com.maxwell.gunsmiths_gadgetsn_gizmos.compat.curios;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Predicate;

public class CuriosCompat {
    public static final boolean IS_CURIOS_LOADED = ModList.get().isLoaded("curios");

    
    public static boolean isEquipped(LivingEntity entity, Item item) {
        return isEquipped(entity, stack -> stack.is(item));
    }

    public static boolean isEquipped(LivingEntity entity, Predicate<ItemStack> predicate) {
        if (IS_CURIOS_LOADED) {
            var inventoryOpt = CuriosApi.getCuriosInventory(entity);
            if (inventoryOpt.isPresent() && inventoryOpt.get().findFirstCurio(predicate).isPresent()) {
                return true;
            }
        }
        if (entity instanceof Player player) {
            if (predicate.test(player.getOffhandItem())) return true;
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                if (predicate.test(player.getInventory().getItem(i))) return true;
            }
        }
        return false;
    }
}