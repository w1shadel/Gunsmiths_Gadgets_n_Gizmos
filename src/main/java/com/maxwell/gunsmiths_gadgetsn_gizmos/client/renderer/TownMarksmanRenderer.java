package com.maxwell.gunsmiths_gadgetsn_gizmos.client.renderer;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.client.model.TownMarksmanModel;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.TownMarksmanEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.checkerframework.checker.nullness.qual.NonNull;

public class TownMarksmanRenderer extends MobRenderer<TownMarksmanEntity, TownMarksmanModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(GunsmithsGadgetsnGizmos.MODID, "textures/entity/town_marksman.png");

    public TownMarksmanRenderer(EntityRendererProvider.Context context) {
        super(context, new TownMarksmanModel(context.bakeLayer(TownMarksmanModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public @NonNull ResourceLocation getTextureLocation(@NonNull TownMarksmanEntity entity) {
        return TEXTURE;
    }
}