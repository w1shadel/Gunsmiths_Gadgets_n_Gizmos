package com.maxwell.gunsmiths_gadgetsn_gizmos.client.gui;

import com.maxwell.gunsmiths_gadgetsn_gizmos.container.AmmoPouchMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class AmmoPouchScreen extends AbstractContainerScreen<AmmoPouchMenu> {
    private static final Identifier BG_TEXTURE = Identifier.fromNamespaceAndPath("gunsmiths_gadgetsn_gizmos", "textures/gui/ammo_pouch.png");
    private static final Identifier SLOT_TEXTURE = Identifier.fromNamespaceAndPath("gunsmiths_gadgetsn_gizmos", "textures/gui/slot.png");

    public AmmoPouchScreen(AmmoPouchMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 133);
        this.inventoryLabelY = this.imageHeight - 94;
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, BG_TEXTURE, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        for (int i = 0; i < this.menu.containerSize; i++) {
            Slot slot = this.menu.slots.get(i);
            graphics.blit(
                    RenderPipelines.GUI_TEXTURED,
                    SLOT_TEXTURE,
                    xo + slot.x - 1,
                    yo + slot.y - 1,
                    0.0F, 0.0F,
                    18, 18,
                    18, 18
            );
        }
    }
}