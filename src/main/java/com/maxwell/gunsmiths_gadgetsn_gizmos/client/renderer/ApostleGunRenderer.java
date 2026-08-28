package com.maxwell.gunsmiths_gadgetsn_gizmos.client.renderer;

import com.maxwell.gunsmiths_gadgetsn_gizmos.client.model.ApostleGunModel;
import com.maxwell.gunsmiths_gadgetsn_gizmos.client.renderer.layer.CigarSmokeLayer;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.ApostleGunEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class ApostleGunRenderer extends MobRenderer<ApostleGunEntity, ApostleGunRenderState, ApostleGunModel> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("gunsmiths_gadgetsn_gizmos", "textures/entity/apostle_gun.png");

    public ApostleGunRenderer(EntityRendererProvider.Context context) {
        super(context, new ApostleGunModel(context.bakeLayer(ApostleGunModel.LAYER_LOCATION)), 0.5F);

        this.addLayer(new ItemInHandLayer<>(this));

        this.addLayer(new CigarSmokeLayer(this));
    }

    @Override
    public @NonNull Identifier getTextureLocation(@NonNull ApostleGunRenderState state) {
        return TEXTURE;
    }

    @Override
    public @NonNull ApostleGunRenderState createRenderState() {
        return new ApostleGunRenderState();
    }

    @Override
    public void extractRenderState(@NonNull ApostleGunEntity entity, @NonNull ApostleGunRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);

        ArmedEntityRenderState.extractArmedEntityRenderState(entity, state, this.itemModelResolver, partialTicks);

        state.headEquipment = entity.getItemBySlot(EquipmentSlot.HEAD);
        state.chestEquipment = entity.getItemBySlot(EquipmentSlot.CHEST);
        state.legsEquipment = entity.getItemBySlot(EquipmentSlot.LEGS);
        state.feetEquipment = entity.getItemBySlot(EquipmentSlot.FEET);

        state.isCrouching = entity.isCrouching();
        state.isUsingItem = entity.isUsingItem();
        state.ticksUsingItem = (float) entity.getTicksUsingItem();
        state.useItemHand = entity.getUsedItemHand();

        state.isCombatMode = entity.isAggressive() || entity.getTarget() != null;
        state.isCastingSpell = entity.isCastingSpell();

        if (!state.isCombatMode || state.isCastingSpell) {
            state.rightHandItemState.clear();
            state.leftHandItemState.clear();
            state.rightHandItemStack = ItemStack.EMPTY;
            state.leftHandItemStack = ItemStack.EMPTY;
        }

        state.holdGunAnimationState.copyFrom(entity.holdGunAnimationState);
        state.shootAnimationState.copyFrom(entity.shootAnimationState);
        state.reloadPhaseInAnimationState.copyFrom(entity.reloadPhaseInAnimationState);
        state.reloadLoopAnimationState.copyFrom(entity.reloadLoopAnimationState);
        state.reloadEndAnimationState.copyFrom(entity.reloadEndAnimationState);
    }
}