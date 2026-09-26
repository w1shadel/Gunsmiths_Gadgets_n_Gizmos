package com.maxwell.gunsmiths_gadgetsn_gizmos.mixin.client;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.api.ammo.AmmoType;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModAmmoTypes;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModDataComponents;
import io.redspace.irons_artifice.client.gui.AmmoCountHudOverlay;
import io.redspace.irons_artifice.config.ClientConfig;
import io.redspace.irons_artifice.item.GunItem;
import io.redspace.irons_artifice.item.GunplayManager;
import io.redspace.irons_artifice.item.MagazineContents;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = AmmoCountHudOverlay.class, remap = false)
public class AmmoCountHudOverlayMixin {
    private static final ResourceLocation EMPTY_ICON = ResourceLocation.fromNamespaceAndPath(GunsmithsGadgetsnGizmos.MODID, "textures/gui/empty.png");
    @Unique
    private static final int[][] OUTLINE_OFFSETS = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {1, -1}, {-1, 1}, {1, 1}
    };

    @Redirect(
            method = "render",
            at = @At(
                    value = "FIELD",
                    target = "Lio/redspace/irons_artifice/client/gui/AmmoCountHudOverlay;BULLET_ICON:Lnet/minecraft/resources/ResourceLocation;",
                    opcode = org.objectweb.asm.Opcodes.GETSTATIC
            )
    )
    private static ResourceLocation gunsmiths_gadgetsn_gizmos$suppressDefaultBulletIcon() {
        Player player = Minecraft.getInstance().player;
        if (player != null) {
            ItemStack heldGun = player.getMainHandItem();
            if (heldGun.has(ModDataComponents.LOADED_AMMO_TYPE.get())) {
                return EMPTY_ICON;
            }
        }
        return io.redspace.irons_artifice.IronsArtifice.id("textures/gui/bullet_icon.png");
    }

    @Inject(method = "render", at = @At("TAIL"))
    private static void gunsmiths_gadgetsn_gizmos$renderLoadedAmmoItemOverlay(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!ClientConfig.ENABLED.get() || !ClientConfig.SHOW_ICON.get()) return;
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options.hideGui) return;
        Player player = minecraft.player;
        if (player == null || player.isSpectator()) return;
        ItemStack heldGun = player.getMainHandItem();
        if (!(heldGun.getItem() instanceof GunItem gunItem)) return;
        String ammoId = heldGun.get(ModDataComponents.LOADED_AMMO_TYPE.get());
        if (ammoId == null) return;
        ResourceLocation id = ResourceLocation.tryParse(ammoId);
        if (id == null) return;
        AmmoType type = ModAmmoTypes.REGISTRY.get(id);
        if (type == null) return;
        Item ammoItem = type.getAmmoItem();
        if (ammoItem == null) return;
        Font font = minecraft.font;
        MagazineContents magazine = GunItem.getMagazine(heldGun);
        int loaded = magazine.count();
        int capacity = gunItem.magazineCapacity();
        int reserve = GunplayManager.countBullets(player);
        float loadedScale = ClientConfig.LOADED_SCALE.get().floatValue();
        float capacityScale = loadedScale * 0.75f;
        float reserveScale = 1.0f;
        boolean showMagazine = ClientConfig.SHOW_MAGAZINE.get();
        boolean showReserve = ClientConfig.SHOW_RESERVE.get();
        int iconSize = ClientConfig.ICON_SIZE.get();
        String loadedText = Integer.toString(loaded);
        String capacityText = "/" + capacity;
        String reserveText = Integer.toString(reserve);
        float loadedWidth = font.width(loadedText) * loadedScale;
        float capacityWidth = showMagazine ? font.width(capacityText) * capacityScale : 0;
        float magazineWidth = loadedWidth + capacityWidth;
        float reserveWidth = showReserve ? font.width(reserveText) * reserveScale : 0;
        float textColumnWidth = Math.max(magazineWidth, reserveWidth);
        float iconBlock = iconSize + 4;
        float totalWidth = iconBlock + textColumnWidth;
        float magazineHeight = font.lineHeight * loadedScale;
        float reserveHeight = showReserve ? font.lineHeight * reserveScale : 0;
        float gapBetweenRows = showReserve ? 2.0F : 0.0F;
        float totalHeight = Math.max(iconSize, magazineHeight + gapBetweenRows + reserveHeight);
        int ox = ClientConfig.OFFSET_X.get();
        int oy = ClientConfig.OFFSET_Y.get();
        int guiWidth = guiGraphics.guiWidth();
        int guiHeight = guiGraphics.guiHeight();
        float left = switch (ClientConfig.ANCHOR.get()) {
            case BOTTOM_RIGHT -> guiWidth - ox - totalWidth;
            case BOTTOM_LEFT -> ox;
            case TOP_RIGHT -> guiWidth - ox - totalWidth;
            case TOP_LEFT -> ox;
            case HOTBAR -> ox + (guiWidth) / 2.0F - totalWidth;
        };
        float top = switch (ClientConfig.ANCHOR.get()) {
            case BOTTOM_RIGHT, BOTTOM_LEFT -> guiHeight - oy - totalHeight;
            case TOP_RIGHT, TOP_LEFT -> oy;
            case HOTBAR -> guiHeight - oy - totalHeight - Math.max(minecraft.gui.leftHeight, minecraft.gui.rightHeight);
        };
        int iconX = Math.round(left);
        int iconY = Math.round(top + (totalHeight - iconSize) * 0.5F);
        ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(ammoItem);
        ResourceLocation textureLocation = ResourceLocation.fromNamespaceAndPath(
                itemId.getNamespace(),
                "textures/item/" + itemId.getPath() + ".png"
        );

        guiGraphics.setColor(0.0F, 0.0F, 0.0F, 1.0F);
        for (int[] offset : OUTLINE_OFFSETS) {
            guiGraphics.blit(
                    textureLocation,
                    iconX + offset[0],
                    iconY + offset[1],
                    0.0F, 0.0F,
                    16, 16,
                    16, 16
            );
        }
        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        guiGraphics.renderItem(new ItemStack(ammoItem), iconX, iconY);
    }
}