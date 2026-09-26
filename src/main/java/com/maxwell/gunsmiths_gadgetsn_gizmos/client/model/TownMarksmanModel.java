package com.maxwell.gunsmiths_gadgetsn_gizmos.client.model;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.client.animation.AnimationHelper;
import com.maxwell.gunsmiths_gadgetsn_gizmos.client.animation.TownMarksmanAnimation;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.TownMarksmanEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.checkerframework.checker.nullness.qual.NonNull;

public class TownMarksmanModel extends HierarchicalModel<TownMarksmanEntity> implements HeadedModel, ArmedModel {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(GunsmithsGadgetsnGizmos.MODID, "town_marksman"), "main");
    private final ModelPart root;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart helmet;
    private final ModelPart brim;
    private final ModelPart nose;
    private final ModelPart arms;
    private final ModelPart normal_arms;
    private final ModelPart right;
    private final ModelPart light;
    private final ModelPart leg0;
    private final ModelPart leg1;

    public TownMarksmanModel(ModelPart root) {
        this.root = root;
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.helmet = this.head.getChild("helmet");
        this.brim = this.head.getChild("brim");
        this.nose = this.head.getChild("nose");
        this.arms = this.body.getChild("arms");
        this.normal_arms = this.body.getChild("normal_arms");
        this.right = this.normal_arms.getChild("right");
        this.light = this.normal_arms.getChild("light");
        this.leg0 = this.body.getChild("leg0");
        this.leg1 = this.body.getChild("leg1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, -24.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 38).addBox(-4.0F, -24.0F, -3.0F, 8.0F, 18.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));
        head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(9, 13).addBox(-1.5F, -0.5F, 0.0F, 3.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.9F, -4.3F, -4.1F, 0.0241F, 0.0476F, 0.1668F));
        head.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        head.addOrReplaceChild("brim", CubeListBuilder.create().texOffs(30, 47).addBox(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
        head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));
        body.addOrReplaceChild("arms", CubeListBuilder.create().texOffs(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(44, 22).mirror().addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -21.0F, -1.0F, -0.75F, 0.0F, 0.0F));
        PartDefinition normal_arms = body.addOrReplaceChild("normal_arms", CubeListBuilder.create(), PartPose.offset(0.0F, -21.0F, 0.0F));
        PartDefinition right = normal_arms.addOrReplaceChild("right", CubeListBuilder.create().texOffs(44, 22).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -1.75F, 0.0F));
        right.addOrReplaceChild("arms_r1", CubeListBuilder.create().texOffs(44, 22).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.75F, 0.0F, 3.1416F, 0.0F, 0.0F));
        PartDefinition light = normal_arms.addOrReplaceChild("light", CubeListBuilder.create().texOffs(44, 22).mirror().addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.0F, -1.75F, 0.0F));
        light.addOrReplaceChild("arms_r2", CubeListBuilder.create().texOffs(44, 22).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 10.75F, 0.0F, 3.1416F, 0.0F, 0.0F));
        body.addOrReplaceChild("leg0", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -12.0F, 0.0F));
        body.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, -12.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NonNull TownMarksmanEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        this.head.xRot = headPitch * Mth.DEG_TO_RAD;
        this.leg0.xRot = Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount * 0.5F;
        this.leg1.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount * 0.5F;
        if (entity.isCrouching()) {
            this.body.xRot = 0.5F;
            this.head.y += 3.2F;
        } else {
            this.body.xRot = 0.0F;
        }
        boolean isCombatMode = entity.isAggressive() || entity.getTarget() != null;
        boolean isAiming = isCombatMode && !entity.getMainHandItem().isEmpty();
        this.arms.visible = !isAiming;
        this.normal_arms.visible = isAiming;
        if (isAiming) {
            AnimationHelper.animate(this.normal_arms, entity.reloadPhaseInAnimationState, TownMarksmanAnimation.reload_phasein, ageInTicks);
            AnimationHelper.animate(this.normal_arms, entity.reloadLoopAnimationState, TownMarksmanAnimation.reloading_loop, ageInTicks);
            AnimationHelper.animate(this.normal_arms, entity.reloadEndAnimationState, TownMarksmanAnimation.reload_end, ageInTicks);
            if (entity.shootAnimationState.isStarted()) {
                AnimationHelper.animate(this.normal_arms, entity.shootAnimationState, TownMarksmanAnimation.shoot_gun, ageInTicks);
            } else {
                AnimationHelper.animate(this.normal_arms, entity.holdGunAnimationState, TownMarksmanAnimation.hold_gun, ageInTicks);
            }
            this.normal_arms.xRot = this.head.xRot;
            this.normal_arms.yRot = this.head.yRot;
        }
    }

    @Override
    public void translateToHand(HumanoidArm humanoidArm, PoseStack poseStack) {
        this.root.translateAndRotate(poseStack);
        this.body.translateAndRotate(poseStack);
        this.normal_arms.translateAndRotate(poseStack);
        if (humanoidArm == HumanoidArm.RIGHT) {
            this.right.translateAndRotate(poseStack);
        } else {
            this.light.translateAndRotate(poseStack);
        }
    }

    @Override
    public @NonNull ModelPart getHead() {
        return this.head;
    }

    @Override
    public @NonNull ModelPart root() {
        return this.root;
    }
}