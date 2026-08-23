package com.maxwell.gunsmiths_gadgetsn_gizmos.client.gui;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.container.CursedAltarMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class CursedAltarScreen extends AbstractContainerScreen<CursedAltarMenu> {
    private static final Identifier BG_TEXTURE = Identifier.fromNamespaceAndPath(GunsmithsGadgetsnGizmos.MODID, "textures/gui/cursed_altar.png");

    public CursedAltarScreen(CursedAltarMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, 176, 166);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        int xo = (this.width - this.imageWidth) / 2;
        int yo = (this.height - this.imageHeight) / 2;
        graphics.blit(RenderPipelines.GUI_TEXTURED, BG_TEXTURE, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }
}