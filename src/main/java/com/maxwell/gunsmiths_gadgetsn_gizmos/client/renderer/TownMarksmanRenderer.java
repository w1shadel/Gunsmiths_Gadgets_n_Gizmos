package com.maxwell.gunsmiths_gadgetsn_gizmos.client.renderer;

import com.maxwell.gunsmiths_gadgetsn_gizmos.client.model.TownMarksmanModel;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.TownMarksmanEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class TownMarksmanRenderer extends MobRenderer<TownMarksmanEntity, TownMarksmanRenderState, TownMarksmanModel> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("gunsmiths_gadgetsn_gizmos", "textures/entity/town_marksman.png");

    public TownMarksmanRenderer(EntityRendererProvider.Context context) {
        super(context, new TownMarksmanModel(context.bakeLayer(TownMarksmanModel.LAYER_LOCATION)), 0.5F);
        this.addLayer(new ItemInHandLayer<>(this));
    }

    @Override
    public @NonNull Identifier getTextureLocation(@NonNull TownMarksmanRenderState state) {
        return TEXTURE;
    }

    @Override
    public @NonNull TownMarksmanRenderState createRenderState() {
        return new TownMarksmanRenderState();
    }

    @Override
    public void extractRenderState(@NonNull TownMarksmanEntity entity, @NonNull TownMarksmanRenderState state, float partialTicks) {
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

        state.isCombatMode = entity.isAggressive()
                || entity.getTarget() != null
                || entity.reloadPhaseInAnimationState.isStarted()
                || entity.reloadLoopAnimationState.isStarted()
                || entity.reloadEndAnimationState.isStarted()
                || entity.shootAnimationState.isStarted();

        if (!state.isCombatMode) {
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