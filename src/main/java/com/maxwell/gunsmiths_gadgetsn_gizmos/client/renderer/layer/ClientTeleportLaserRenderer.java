package com.maxwell.gunsmiths_gadgetsn_gizmos.client.renderer.layer;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.ApostleGunEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID, value = Dist.CLIENT)
public class ClientTeleportLaserRenderer {
    private static final ResourceLocation BEAM_TEXTURE = ResourceLocation.withDefaultNamespace("textures/entity/beacon_beam.png");

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null) return;

        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 camPos = camera.getPosition();
        float partialTick = event.getPartialTick().getGameTimeDeltaPartialTick(false);

        List<ApostleGunEntity> bosses = mc.level.getEntitiesOfClass(
                ApostleGunEntity.class,
                mc.player.getBoundingBox().inflate(64.0),
                b -> b.isAlive() && b.getTeleportTimer() > 0
        );
        if (bosses.isEmpty()) return;

        PoseStack poseStack = event.getPoseStack();
        for (ApostleGunEntity boss : bosses) {
            double bx = Mth.lerp(partialTick, boss.xo, boss.getX());
            double by = Mth.lerp(partialTick, boss.yo, boss.getY()) + 1.2;
            double bz = Mth.lerp(partialTick, boss.zo, boss.getZ());
            Vec3 startWorld = new Vec3(bx, by, bz);
            Vec3 endWorld = boss.getTeleportTarget().add(0, 0.2, 0);
            Vec3 delta = endWorld.subtract(startWorld);
            float distance = (float) delta.length();
            if (distance < 0.2F) continue;

            poseStack.pushPose();
            poseStack.translate(startWorld.x - camPos.x, startWorld.y - camPos.y, startWorld.z - camPos.z);

            Vec3 dir = delta.normalize();
            Quaternionf rotation = new Quaternionf().rotationTo(
                    new Vector3f(0, 0, 1),
                    new Vector3f((float) dir.x, (float) dir.y, (float) dir.z)
            );
            poseStack.mulPose(rotation);

            float pulse = (float) Math.sin((boss.tickCount + partialTick) * 0.5F) * 0.25F + 0.75F;
            int outerColor = FastColor.ARGB32.colorFromFloat(0.85F * pulse, 0.65F, 0.05F, 0.20F);
            int coreColor = FastColor.ARGB32.colorFromFloat(0.95F, 1.0F, 0.85F, 0.90F);

            var bufferSource = mc.renderBuffers().bufferSource();
            RenderType renderType = RenderType.entityCutoutNoCull(BEAM_TEXTURE);
            VertexConsumer consumer = bufferSource.getBuffer(renderType);
            drawCrossQuad(consumer, poseStack.last(), distance, 0.45F, outerColor, 15728880);
            drawCrossQuad(consumer, poseStack.last(), distance, 0.15F, coreColor, 15728880);
            bufferSource.endBatch(renderType);

            poseStack.popPose();
        }
    }

    private static void drawCrossQuad(VertexConsumer consumer, PoseStack.Pose pose, float length, float width, int color, int light) {
        float halfW = width * 0.5F;
        consumer.addVertex(pose, -halfW, 0, 0).setColor(color).setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0, 1, 0);
        consumer.addVertex(pose, halfW, 0, 0).setColor(color).setUv(1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0, 1, 0);
        consumer.addVertex(pose, halfW, 0, length).setColor(color).setUv(1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0, 1, 0);
        consumer.addVertex(pose, -halfW, 0, length).setColor(color).setUv(0, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 0, 1, 0);

        consumer.addVertex(pose, 0, -halfW, 0).setColor(color).setUv(0, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 1, 0, 0);
        consumer.addVertex(pose, 0, halfW, 0).setColor(color).setUv(1, 0).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 1, 0, 0);
        consumer.addVertex(pose, 0, halfW, length).setColor(color).setUv(1, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 1, 0, 0);
        consumer.addVertex(pose, 0, -halfW, length).setColor(color).setUv(0, 1).setOverlay(OverlayTexture.NO_OVERLAY).setLight(light).setNormal(pose, 1, 0, 0);
    }
}