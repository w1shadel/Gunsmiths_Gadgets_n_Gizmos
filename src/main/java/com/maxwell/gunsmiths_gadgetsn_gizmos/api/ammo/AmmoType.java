package com.maxwell.gunsmiths_gadgetsn_gizmos.api.ammo;

import io.redspace.irons_artifice.gun.ShotProfile;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public abstract class AmmoType {
    private final Supplier<? extends Item> ammoItem;

    public AmmoType(Supplier<? extends Item> ammoItem) {
        this.ammoItem = ammoItem;
    }

    public Item getAmmoItem() {
        return ammoItem.get();
    }

    public abstract void applyToShot(ShotProfile profile, LivingEntity shooter);

    public abstract Component getDisplayName();
}