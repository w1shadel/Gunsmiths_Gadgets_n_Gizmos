package com.maxwell.gunsmiths_gadgetsn_gizmos.mixin.client;

import com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy.GunSetBonus;
import com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy.GunSetBonusManager;
import io.redspace.irons_artifice.menu.GunModifierMenu;
import io.redspace.irons_artifice.menu.GunModifierScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mixin(value = GunModifierScreen.class, remap = false)
public abstract class GunModifierScreenMixin extends AbstractContainerScreen<GunModifierMenu> {
    @Unique
    private int gunsmiths_gadgetsn_gizmos$lastActiveCount = -1;

    public GunModifierScreenMixin(GunModifierMenu menu, Inventory playerInventory, net.minecraft.network.chat.Component title) {
        super(menu, playerInventory, title, 176, 183);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void gunsmiths_gadgetsn_gizmos$initBonusCount(CallbackInfo ci) {
        GunModifierMenu gunMenu = (GunModifierMenu) this.menu;
        gunsmiths_gadgetsn_gizmos$lastActiveCount = gunsmiths_gadgetsn_gizmos$getActiveBonuses(gunMenu).size();
    }

    @Inject(method = "extractBackground", at = @At("TAIL"))
    private void gunsmiths_gadgetsn_gizmos$renderVisualSynergyEffects(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        GunModifierMenu gunMenu = (GunModifierMenu) this.menu;
        ItemStack gun = gunMenu.gunstack;
        if (gun.isEmpty()) return;
        List<Slot> modifierSlots = gunMenu.getModifierSlots();
        List<ItemStack> currentModifiers = new ArrayList<>();
        for (Slot slot : modifierSlots) {
            if (slot != null && slot.hasItem()) {
                currentModifiers.add(slot.getItem());
            }
        }
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        float tick = Minecraft.getInstance().player.tickCount + partialTick;
        List<GunSetBonus> activeBonuses = new ArrayList<>();
        for (GunSetBonus bonus : GunSetBonusManager.getAllBonuses()) {
            if (bonus.matches(currentModifiers)) {
                activeBonuses.add(bonus);
            }
        }
        if (gunsmiths_gadgetsn_gizmos$lastActiveCount != -1 && activeBonuses.size() > gunsmiths_gadgetsn_gizmos$lastActiveCount) {
            Minecraft.getInstance().getSoundManager().play(
                    SimpleSoundInstance.forUI(SoundEvents.AMETHYST_BLOCK_CHIME, 1.0F, 1.2F)
            );
            Minecraft.getInstance().getSoundManager().play(
                    SimpleSoundInstance.forUI(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.8F, 1.5F)
            );
        }
        gunsmiths_gadgetsn_gizmos$lastActiveCount = activeBonuses.size();
        if (!activeBonuses.isEmpty()) {
            GunSetBonus topBonus = activeBonuses.get(0);
            int auraColor = gunsmiths_gadgetsn_gizmos$parseColor(topBonus.bonuses().trailColor().orElse("#00FFFF"));
            float gunCenterX = xo + this.imageWidth / 2.0F;
            float gunCenterY = yo + 58;
            float pulse = (Mth.sin(tick * 0.08F) + 1.0F) * 0.5F;
            int ringRadius = (int) (30 + pulse * 4);
            for (int r = ringRadius; r > 10; r -= 5) {
                float progress = (float) r / ringRadius;
                int alpha = (int) ((1.0F - progress) * 60 * (0.7F + pulse * 0.3F));
                int layerColor = (alpha << 24) | (auraColor & 0x00FFFFFF);
                graphics.fill((int) (gunCenterX - r), (int) (gunCenterY - r * 0.6F),
                        (int) (gunCenterX + r), (int) (gunCenterY + r * 0.6F), layerColor);
            }
        }
        Set<Identifier> highlightedItemIds = new HashSet<>();
        boolean isFullActive = !activeBonuses.isEmpty();
        if (isFullActive) {
            for (GunSetBonus bonus : activeBonuses) {
                highlightedItemIds.addAll(bonus.requiredModifiers());
            }
        } else {
            GunSetBonus nearBonus = gunsmiths_gadgetsn_gizmos$findNearBonus(currentModifiers);
            if (nearBonus != null) {
                highlightedItemIds.addAll(nearBonus.requiredModifiers());
            }
        }
        if (!highlightedItemIds.isEmpty()) {
            float slotPulse = (Mth.sin(tick * 0.12F) + 1.0F) * 0.5F;
            int baseGlow = isFullActive ? 0x55FF55 : 0xBB66FF;
            int glowAlpha = (int) ((isFullActive ? 120 : 70) + slotPulse * 75);
            int glowColor = (glowAlpha << 24) | baseGlow;
            for (Slot slot : modifierSlots) {
                if (slot != null && slot.hasItem()) {
                    Identifier itemId = BuiltInRegistries.ITEM.getKey(slot.getItem().getItem());
                    if (highlightedItemIds.contains(itemId)) {
                        int sx = xo + slot.x - 4;
                        int sy = yo + slot.y - 4;
                        graphics.fill(sx - 1, sy - 1, sx + 25, sy, glowColor);
                        graphics.fill(sx - 1, sy + 24, sx + 25, sy + 25, glowColor);
                        graphics.fill(sx - 1, sy, sx, sy + 24, glowColor);
                        graphics.fill(sx + 24, sy, sx + 25, sy + 24, glowColor);
                        graphics.fill(sx, sy, sx + 24, sy + 24, (25 << 24) | baseGlow);
                    }
                }
            }
        }
    }

    @Unique
    private List<GunSetBonus> gunsmiths_gadgetsn_gizmos$getActiveBonuses(GunModifierMenu gunMenu) {
        List<ItemStack> currentModifiers = new ArrayList<>();
        for (Slot slot : gunMenu.getModifierSlots()) {
            if (slot != null && slot.hasItem()) {
                currentModifiers.add(slot.getItem());
            }
        }
        List<GunSetBonus> active = new ArrayList<>();
        for (GunSetBonus bonus : GunSetBonusManager.getAllBonuses()) {
            if (bonus.matches(currentModifiers)) {
                active.add(bonus);
            }
        }
        return active;
    }

    @Unique
    private GunSetBonus gunsmiths_gadgetsn_gizmos$findNearBonus(List<ItemStack> installed) {
        Set<Identifier> installedIds = installed.stream()
                .filter(s -> !s.isEmpty())
                .map(s -> BuiltInRegistries.ITEM.getKey(s.getItem()))
                .collect(Collectors.toSet());
        for (GunSetBonus bonus : GunSetBonusManager.getAllBonuses()) {
            int matchCount = 0;
            for (Identifier req : bonus.requiredModifiers()) {
                if (installedIds.contains(req)) {
                    matchCount++;
                }
            }
            if (matchCount == bonus.requiredModifiers().size() - 1 && bonus.requiredModifiers().size() >= 2) {
                return bonus;
            }
        }
        return null;
    }

    @Unique
    private int gunsmiths_gadgetsn_gizmos$parseColor(String hex) {
        try {
            return (int) Long.parseLong(hex.replace("#", ""), 16);
        } catch (Exception e) {
            return 0x00FFFF;
        }
    }
}