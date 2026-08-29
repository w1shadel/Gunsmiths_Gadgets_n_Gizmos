package com.maxwell.gunsmiths_gadgetsn_gizmos.client.model;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.client.animation.AnimationHelper;
import com.maxwell.gunsmiths_gadgetsn_gizmos.client.animation.TownMarksmanAnimation;
import com.maxwell.gunsmiths_gadgetsn_gizmos.client.renderer.ApostleGunRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import org.jspecify.annotations.NonNull;

public class ApostleGunModel extends EntityModel<ApostleGunRenderState> implements HeadedModel, ArmedModel {
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(Identifier.fromNamespaceAndPath(GunsmithsGadgetsnGizmos.MODID, "apostle_gun"), "main");
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart bone;
    private final ModelPart helmet;
    private final ModelPart brim;
    private final ModelPart nose;
    private final ModelPart arms;
    private final ModelPart held_item;
    private final ModelPart normal_arms;
    private final ModelPart right;
    private final ModelPart light;
    private final ModelPart held_item2;
    private final ModelPart leg0;
    private final ModelPart leg1;

    public ApostleGunModel(ModelPart root) {
        super(root);
        this.body = root.getChild("body");
        this.head = this.body.getChild("head");
        this.bone = this.head.getChild("bone");
        this.helmet = this.head.getChild("helmet");
        this.brim = this.head.getChild("brim");
        this.nose = this.head.getChild("nose");
        this.arms = this.body.getChild("arms");
        this.held_item = this.arms.getChild("held_item");
        this.normal_arms = this.body.getChild("normal_arms");
        this.right = this.normal_arms.getChild("right");
        this.light = this.normal_arms.getChild("light");
        this.held_item2 = this.normal_arms.getChild("held_item2");
        this.leg0 = this.body.getChild("leg0");
        this.leg1 = this.body.getChild("leg1");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 20).addBox(-4.0F, -24.0F, -3.0F, 8.0F, 12.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(0, 38).addBox(-4.0F, -24.0F, -3.0F, 8.0F, 18.0F, 6.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 10.0F, 8.0F, new CubeDeformation(0.0F))
                .texOffs(41, 11).addBox(-4.0F, -5.0F, -4.1F, 4.0F, 5.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -24.0F, 0.0F));
        PartDefinition head_r1 = head.addOrReplaceChild("head_r1", CubeListBuilder.create().texOffs(33, 14).addBox(-0.5F, -0.5F, -2.0F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.5F, -0.5F, -5.0F, 0.2618F, -0.48F, 0.0F));
        PartDefinition bone = head.addOrReplaceChild("bone", CubeListBuilder.create(), PartPose.offset(3.3F, 0.0F, -6.3F));
        PartDefinition helmet = head.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition brim = head.addOrReplaceChild("brim", CubeListBuilder.create().texOffs(30, 47).addBox(-8.0F, -8.0F, -6.0F, 16.0F, 16.0F, 1.0F, new CubeDeformation(0.1F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.0F, 0.0F));
        PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));
        PartDefinition arms = body.addOrReplaceChild("arms", CubeListBuilder.create().texOffs(28, 38).addBox(-4.0F, 2.0F, -2.0F, 8.0F, 4.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F))
                .texOffs(44, 22).mirror().addBox(4.0F, -2.0F, -2.0F, 4.0F, 8.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, -21.0F, -1.0F, -0.75F, 0.0F, 0.0F));
        PartDefinition held_item = arms.addOrReplaceChild("held_item", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, 0.0F));
        PartDefinition normal_arms = body.addOrReplaceChild("normal_arms", CubeListBuilder.create(), PartPose.offset(0.0F, -21.0F, 0.0F));
        PartDefinition right = normal_arms.addOrReplaceChild("right", CubeListBuilder.create().texOffs(44, 22).addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-6.0F, -1.75F, 0.0F));
        PartDefinition arms_r1 = right.addOrReplaceChild("arms_r1", CubeListBuilder.create().texOffs(48, 35).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 10.75F, 0.0F, 3.1416F, 0.0F, 0.0F));
        PartDefinition light = normal_arms.addOrReplaceChild("light", CubeListBuilder.create().texOffs(44, 22).mirror().addBox(-2.0F, -0.25F, -2.0F, 4.0F, 6.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(6.0F, -1.75F, 0.0F));
        PartDefinition arms_r2 = light.addOrReplaceChild("arms_r2", CubeListBuilder.create().texOffs(48, 35).mirror().addBox(-2.0F, -2.0F, -2.0F, 4.0F, 7.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(0.0F, 10.75F, 0.0F, 3.1416F, 0.0F, 0.0F));
        PartDefinition held_item2 = normal_arms.addOrReplaceChild("held_item2", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, 0.0F));
        PartDefinition leg0 = body.addOrReplaceChild("leg0", CubeListBuilder.create().texOffs(0, 22).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -12.0F, 0.0F));
        PartDefinition leg1 = body.addOrReplaceChild("leg1", CubeListBuilder.create().texOffs(0, 22).mirror().addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(2.0F, -12.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(@NonNull ApostleGunRenderState state) {
        super.setupAnim(state);
        this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
        this.head.xRot = state.xRot * Mth.DEG_TO_RAD;
        this.leg0.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed * 0.5F;
        this.leg1.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + (float) Math.PI) * 1.4F * state.walkAnimationSpeed * 0.5F;
        if (state.isCrouching) {
            this.body.xRot = 0.5F;
            this.head.y += 3.2F;
        } else {
            this.body.xRot = 0.0F;
        }
        boolean isAiming = state.isCombatMode && !state.getMainHandItemStack().isEmpty();
        this.arms.visible = !isAiming;
        this.normal_arms.visible = isAiming;
        if (state.isCastingSpell) {
            float spellCycle = (state.ageInTicks * 0.15F) % (float) Math.PI;
            float castProgress = Mth.sin(spellCycle);
            if (castProgress > 0.7F) {
                this.right.xRot = (float) Math.toRadians(-30);
                this.right.yRot = -0.3F;
                this.light.xRot = (float) Math.toRadians(-30);
                this.light.yRot = 0.3F;
            } else {
                float sway = Mth.cos(state.ageInTicks * 0.5F) * 0.2F;
                this.right.xRot = (float) Math.toRadians(-145) + sway;
                this.right.yRot = -0.25F;
                this.light.xRot = (float) Math.toRadians(-145) - sway;
                this.light.yRot = 0.25F;
            }
            return;
        }
        if (isAiming) {
            if (state.isCastingSpell) {
                this.right.xRot = (float) Math.toRadians(-135) + Mth.cos(state.ageInTicks * 0.4F) * 0.15F;
                this.right.yRot = -0.2F;
                this.light.xRot = (float) Math.toRadians(-135) + Mth.cos(state.ageInTicks * 0.4F + (float) Math.PI) * 0.15F;
                this.light.yRot = 0.2F;
                return;
            }
            AnimationHelper.animate(this.normal_arms, state.reloadPhaseInAnimationState, TownMarksmanAnimation.reload_phasein, state.ageInTicks);
            AnimationHelper.animate(this.normal_arms, state.reloadLoopAnimationState, TownMarksmanAnimation.reloading_loop, state.ageInTicks);
            AnimationHelper.animate(this.normal_arms, state.reloadEndAnimationState, TownMarksmanAnimation.reload_end, state.ageInTicks);
            if (state.shootAnimationState.isStarted()) {
                AnimationHelper.animate(this.normal_arms, state.shootAnimationState, TownMarksmanAnimation.shoot_gun, state.ageInTicks);
            } else {
                AnimationHelper.animate(this.normal_arms, state.holdGunAnimationState, TownMarksmanAnimation.hold_gun, state.ageInTicks);
            }
            this.normal_arms.xRot = this.head.xRot;
            this.normal_arms.yRot = this.head.yRot;
        }
    }

    @Override
    public void translateToHand(EntityRenderState entityRenderState, HumanoidArm humanoidArm, PoseStack poseStack) {
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
}