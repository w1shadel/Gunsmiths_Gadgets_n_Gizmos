package com.maxwell.gunsmiths_gadgetsn_gizmos.client.renderer;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.client.model.ApostleGunModel;
import com.maxwell.gunsmiths_gadgetsn_gizmos.client.renderer.layer.CigarSmokeLayer;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.ApostleGunEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ApostleGunRenderer extends MobRenderer<ApostleGunEntity, ApostleGunModel> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(GunsmithsGadgetsnGizmos.MODID, "textures/entity/apostle_gun.png");

    public ApostleGunRenderer(EntityRendererProvider.Context context) {
        super(context, new ApostleGunModel(context.bakeLayer(ApostleGunModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        this.addLayer(new CigarSmokeLayer(this));
    }

    @Override
    public @NonNull ResourceLocation getTextureLocation(@NonNull ApostleGunEntity entity) {
        return TEXTURE;
    }
}