package com.maxwell.gunsmiths_gadgetsn_gizmos.util;

import io.redspace.irons_artifice.gun.ShotProfile;
import io.redspace.irons_artifice.menu.GunContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ModifierHelper {
    
    public static boolean hasModifier(ShotProfile profile, Item modifierItem) {
        return hasModifier(profile.itemStack(), modifierItem);
    }

    
    public static boolean hasModifier(ItemStack gunStack, Item modifierItem) {
        if (gunStack.isEmpty()) return false;
        GunContainer container = new GunContainer(gunStack);
        for (ItemStack slotStack : container.getItems()) {
            if (slotStack.is(modifierItem)) {
                return true;
            }
        }
        return false;
    }
}