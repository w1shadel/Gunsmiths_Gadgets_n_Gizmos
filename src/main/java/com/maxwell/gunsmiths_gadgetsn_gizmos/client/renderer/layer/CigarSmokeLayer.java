package com.maxwell.gunsmiths_gadgetsn_gizmos.client.renderer.layer;

import com.maxwell.gunsmiths_gadgetsn_gizmos.client.model.ApostleGunModel;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.ApostleGunEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class CigarSmokeLayer extends RenderLayer<ApostleGunEntity, ApostleGunModel> {
    private final RandomSource random = RandomSource.create();

    public CigarSmokeLayer(RenderLayerParent<ApostleGunEntity, ApostleGunModel> renderer) {
        super(renderer);
    }

    @Override
    public void render(@NonNull PoseStack poseStack, @NonNull MultiBufferSource buffer, int packedLight, @NonNull ApostleGunEntity entity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        Minecraft mc = Minecraft.getInstance();
        ClientLevel level = mc.level;
        if (level == null || mc.isPaused()) return;
        if (ageInTicks % 2.0F > 0.5F) return;

        poseStack.pushPose();
        ApostleGunModel model = this.getParentModel();
        model.root().translateAndRotate(poseStack);
        if (model.root().hasChild("body")) {
            var body = model.root().getChild("body");
            body.translateAndRotate(poseStack);
            if (body.hasChild("head")) {
                var head = body.getChild("head");
                head.translateAndRotate(poseStack);
                if (head.hasChild("bone")) {
                    head.getChild("bone").translateAndRotate(poseStack);
                }
            }
        }

        Matrix4f poseMatrix = poseStack.last().pose();
        Vector4f localPos = new Vector4f(0.0F, 0.0F, 0.0F, 1.0F);
        localPos.mul(poseMatrix);

        Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
        double worldX = cameraPos.x + localPos.x();
        double worldY = cameraPos.y + localPos.y();
        double worldZ = cameraPos.z + localPos.z();
        poseStack.popPose();

        level.addParticle(ParticleTypes.SOUL,
                worldX, worldY, worldZ,
                (this.random.nextFloat() - 0.5F) * 0.01F,
                0.03F + this.random.nextFloat() * 0.02F,
                (this.random.nextFloat() - 0.5F) * 0.01F);

        if (this.random.nextFloat() < 0.25F) {
            level.addParticle(ParticleTypes.SOUL_FIRE_FLAME,
                    worldX, worldY, worldZ,
                    0.0F, 0.01F, 0.0F);
        }

        if (entity.isCastingSpell()) {
            level.addParticle(ParticleTypes.SCULK_SOUL,
                    worldX, worldY, worldZ,
                    0.0F, 0.05F, 0.0F);
        }
    }
}