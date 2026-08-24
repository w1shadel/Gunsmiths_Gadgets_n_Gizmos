package com.maxwell.gunsmiths_gadgetsn_gizmos.mixin.client;

import com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy.GunSetBonus;
import com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy.GunSetBonusManager;
import io.redspace.irons_artifice.menu.GunModifierMenu;
import io.redspace.irons_artifice.menu.GunModifierScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GunModifierScreen.class)
public abstract class GunModifierScreenMixin extends AbstractContainerScreen<GunModifierMenu> {

    @Unique
    private int gunsmiths_gadgetsn_gizmos$lastActiveBonusCount = 0;

    public GunModifierScreenMixin(GunModifierMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 183);
    }

    @Inject(method = "extractBackground", at = @At("TAIL"), remap = false)
    private void gunsmiths_gadgetsn_gizmos$renderSetBonusEffects(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, CallbackInfo ci) {
        ItemStack gun = this.menu.gunstack;
        if (gun.isEmpty()) return;

        List<GunSetBonus> activeBonuses = GunSetBonusManager.getMatchingBonuses(gun);
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;

        // =========================================================================
        // 1. セット効果が発動した瞬間のファンファーレ効果音
        // =========================================================================
        if (activeBonuses.size() > gunsmiths_gadgetsn_gizmos$lastActiveBonusCount) {
            Minecraft.getInstance().getSoundManager().play(
                    SimpleSoundInstance.forUI(SoundEvents.PLAYER_LEVELUP, 1.8F, 0.7F)
            );
        }
        gunsmiths_gadgetsn_gizmos$lastActiveBonusCount = activeBonuses.size();

        // =========================================================================
        // 2. 発動中のセット効果のGUI表示 (緑色のカットイン表示)
        // =========================================================================
        if (!activeBonuses.isEmpty()) {
            int textY = yo + 14;
            for (GunSetBonus bonus : activeBonuses) {
                Component titleComp = Component.literal("★ ").append(Component.translatable(bonus.nameKey()));
                int textWidth = this.font.width(titleComp);
                int textX = xo + (this.imageWidth - textWidth) / 2;

                // ほんのり光る背景バー描画
                graphics.fill(textX - 4, textY - 2, textX + textWidth + 4, textY + 10, 0x90003311);
                // テキスト描画 (鮮やかな黄緑色)
                graphics.text(this.font, titleComp, textX, textY, 0x55FF55, true);
                textY += 12;
            }
        }
        // =========================================================================
        // 3. リーチ（あと1パーツ）の予兆演出 (紫色の点滅パルス表示)
        // =========================================================================
        else {
            GunSetBonus nearBonus = gunsmiths_gadgetsn_gizmos$findNearBonus(gun);
            if (nearBonus != null) {
                float pulse = (Mth.sin(Minecraft.getInstance().player.tickCount * 0.15F) + 1.0F) * 0.5F;
                int alpha = (int) (100 + pulse * 155);
                int color = (alpha << 24) | 0xAA00FF; // 紫色の点滅

                Component hintComp = Component.literal("⚠️ ").append(Component.translatable("gunsmiths_gadgetsn_gizmos.gui.synergy_near"));
                int textWidth = this.font.width(hintComp);
                int textX = xo + (this.imageWidth - textWidth) / 2;

                graphics.text(this.font, hintComp, textX, yo + 14, color, true);
            }
        }
    }

    /**
     * あと1つのパーツで揃うセット効果を探索するヘルパー
     */
    @Unique
    private GunSetBonus gunsmiths_gadgetsn_gizmos$findNearBonus(ItemStack gun) {
        var container = new io.redspace.irons_artifice.menu.GunContainer(gun);
        var installed = container.getItems();
        for (GunSetBonus bonus : GunSetBonusManager.getMatchingBonuses(ItemStack.EMPTY)) { // 全ボーナスを走査
            int matchCount = 0;
            for (net.minecraft.resources.Identifier req : bonus.requiredModifiers()) {
                for (ItemStack slotStack : installed) {
                    if (!slotStack.isEmpty() && net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(slotStack.getItem()).equals(req)) {
                        matchCount++;
                        break;
                    }
                }
            }
            // 必要な数より「ちょうど1個だけ足りない」状態
            if (matchCount == bonus.requiredModifiers().size() - 1 && bonus.requiredModifiers().size() >= 3) {
                return bonus;
            }
        }
        return null;
    }
}