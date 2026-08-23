package com.maxwell.gunsmiths_gadgetsn_gizmos.registry;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.api.ammo.AmmoType;
import io.redspace.irons_artifice.client.particle.ColorTransitionParticleOption;
import io.redspace.irons_artifice.data.ShotComponents;
import io.redspace.irons_artifice.data.ValueModifier;
import io.redspace.irons_artifice.gun.ShotProfile;
import io.redspace.irons_artifice.registry.ItemRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModAmmoTypes {
    public static final ResourceKey<Registry<AmmoType>> REGISTRY_KEY =
            ResourceKey.createRegistryKey(Identifier.fromNamespaceAndPath(GunsmithsGadgetsnGizmos.MODID, "ammo_type"));
    public static final DeferredRegister<AmmoType> AMMO_TYPES =
            DeferredRegister.create(REGISTRY_KEY, GunsmithsGadgetsnGizmos.MODID);
    public static final Registry<AmmoType> REGISTRY = AMMO_TYPES.makeRegistry(builder -> {
    });
    public static final DeferredHolder<AmmoType, AmmoType> DEFAULT = AMMO_TYPES.register("default",
            () -> new AmmoType(ItemRegistry.BULLET) {
                @Override
                public void applyToShot(ShotProfile profile, LivingEntity shooter) {
                }

                @Override
                public Component getDisplayName() {
                    return Component.translatable("ammo.gunsmiths_gadgetsn_gizmos.default");
                }
            }
    );
    public static final DeferredHolder<AmmoType, AmmoType> SILVER = AMMO_TYPES.register("silver",
            () -> new AmmoType(ModItems.SILVER_BULLET) {
                @Override
                public void applyToShot(ShotProfile profile, LivingEntity shooter) {
                    profile.components().getOrCreate(ShotComponents.PARTICLE_TRAIL).add(
                            ColorTransitionParticleOption.bulletTrail(0xE8F8FF, 0x90B8CC)
                    );
                    profile.components().getOrCreate(ShotComponents.MUZZLE_FLASH).addTint(0xE8F8FF);
                }

                @Override
                public Component getDisplayName() {
                    return Component.translatable("ammo.gunsmiths_gadgetsn_gizmos.silver");
                }
            }
    );
    public static final DeferredHolder<AmmoType, AmmoType> ARMOR_PIERCING = AMMO_TYPES.register("armor_piercing",
            () -> new AmmoType(ModItems.AP_BULLET) {
                @Override
                public void applyToShot(ShotProfile profile, LivingEntity shooter) {
                    profile.components().getOrCreate(ShotComponents.PIERCING)
                            .addModifier(new ValueModifier(2, ValueModifier.Operation.ADD, ValueModifier.Type.BENEFICIAL));
                    profile.components().getOrCreate(ShotComponents.BULLET_SPEED)
                            .addModifier(new ValueModifier(0.30, ValueModifier.Operation.MULTIPLY_TOTAL, ValueModifier.Type.BENEFICIAL));
                    profile.components().getOrCreate(ShotComponents.PARTICLE_TRAIL).add(
                            ColorTransitionParticleOption.bulletTrail(0xFFA500, 0x8B4500)
                    );
                }

                @Override
                public Component getDisplayName() {
                    return Component.translatable("ammo.gunsmiths_gadgetsn_gizmos.armor_piercing");
                }
            }
    );

    public static void register(IEventBus bus) {
        AMMO_TYPES.register(bus);
    }
}