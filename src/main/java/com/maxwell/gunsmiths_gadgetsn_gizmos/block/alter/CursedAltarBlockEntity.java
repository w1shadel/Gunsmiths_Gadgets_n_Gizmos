package com.maxwell.gunsmiths_gadgetsn_gizmos.block.alter;

import com.maxwell.gunsmiths_gadgetsn_gizmos.container.CursedAltarMenu;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.ApostleGunEntity;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModBlockEntities;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CursedAltarBlockEntity extends BlockEntity implements MenuProvider, Container {
    public static final int SLOT_COUNT = 4;
    private final NonNullList<ItemStack> items = NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
    private int ritualTimer = 0;

    public CursedAltarBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CURSED_ALTAR_BE.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, CursedAltarBlockEntity altar) {
        if (altar.ritualTimer <= 0) return;
        altar.ritualTimer--;
        if (level instanceof ServerLevel serverLevel) {
            double cx = pos.getX() + 0.5;
            double cy = pos.getY() + 1.1;
            double cz = pos.getZ() + 0.5;
            float time = 80 - altar.ritualTimer;
            float radius = 1.6F;
            int points = 16;
            for (int i = 0; i < points; i++) {
                double angle = (i * (Math.PI * 2 / points)) + (time * 0.12);
                double px = cx + Math.cos(angle) * radius;
                double pz = cz + Math.sin(angle) * radius;
                serverLevel.sendParticles(ParticleTypes.REVERSE_PORTAL, px, cy, pz, 1, 0, 0.05, 0, 0.01);
                if (i % 2 == 0) {
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, px, cy, pz, 1, 0, 0.02, 0, 0.01);
                }
            }
            serverLevel.sendParticles(ParticleTypes.SQUID_INK, cx, cy + 0.2, cz, 2, 0.1, 0.2, 0.1, 0.05);
            if (time % 20 == 0) {
                serverLevel.playSound(null, pos, SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.5F, 0.8F + (time * 0.01F));
                serverLevel.playSound(null, pos, SoundEvents.SOUL_ESCAPE.value(), SoundSource.BLOCKS, 1.2F, 1.2F);
            }
            if (altar.ritualTimer == 0) {
                ApostleGunEntity apostle = ModEntities.APOSTLE_GUN.get().create(serverLevel, EntitySpawnReason.TRIGGERED);
                if (apostle != null) {
                    apostle.setPos(cx, cy + 0.5, cz);
                    apostle.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(pos), EntitySpawnReason.TRIGGERED, null);
                    serverLevel.addFreshEntity(apostle);
                }
                serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER, cx, cy + 1.0, cz, 3, 0, 0, 0, 0);
                serverLevel.sendParticles(ParticleTypes.SONIC_BOOM, cx, cy + 0.5, cz, 1, 0, 0, 0, 0);
                serverLevel.sendParticles(ParticleTypes.SQUID_INK, cx, cy + 1.0, cz, 80, 0.8, 1.2, 0.8, 0.15);
                serverLevel.playSound(null, pos, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.HOSTILE, 3.0F, 0.6F);
                serverLevel.playSound(null, pos, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE, 3.0F, 0.8F);
                serverLevel.playSound(null, pos, SoundEvents.BELL_BLOCK, SoundSource.HOSTILE, 3.0F, 0.3F);
            }
        }
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        this.drops();
    }

    public void drops() {
        if (this.level == null) return;
        for (ItemStack stack : this.items) {
            if (!stack.isEmpty()) {
                Containers.dropItemStack(this.level, this.worldPosition.getX(), this.worldPosition.getY(), this.worldPosition.getZ(), stack);
            }
        }
    }

    public void startRitual() {
        this.ritualTimer = 80;
        this.setChanged();
    }

    @Override
    public int getContainerSize() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        return this.items.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.items.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack res = ContainerHelper.removeItem(this.items, slot, amount);
        if (!res.isEmpty()) setChanged();
        return res;
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.items, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.items.set(slot, stack);
        setChanged();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.items.clear();
        setChanged();
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("block.gunsmiths_gadgetsn_gizmos.cursed_altar");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, @NotNull Inventory playerInventory, @NotNull Player player) {
        return new CursedAltarMenu(containerId, playerInventory, this);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.items.clear();
        ContainerHelper.loadAllItems(input, this.items);
        this.ritualTimer = input.getIntOr("RitualTimer", 0);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, this.items);
        output.putInt("RitualTimer", this.ritualTimer);
    }
}