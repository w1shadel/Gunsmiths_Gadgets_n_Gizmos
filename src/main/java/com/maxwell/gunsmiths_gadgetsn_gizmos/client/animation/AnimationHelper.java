package com.maxwell.gunsmiths_gadgetsn_gizmos.client.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import org.joml.Vector3fc;

import java.util.List;
import java.util.Map;

public class AnimationHelper {
    
    public static void animate(ModelPart root, AnimationState state, AnimationDefinition animation, float ageInTicks) {
        state.ifStarted(animState -> {
            float elapsedSeconds = (float) animState.getTimeInMillis(ageInTicks) / 1000.0F;
            float animLength = animation.lengthInSeconds();
            float time = animation.looping() && animLength > 0.0F
                    ? elapsedSeconds % animLength
                    : Math.min(elapsedSeconds, animLength);
            for (Map.Entry<String, List<AnimationChannel>> entry : animation.boneAnimations().entrySet()) {
                String boneName = entry.getKey();
                ModelPart targetBone = findBone(root, boneName);
                if (targetBone != null) {
                    for (AnimationChannel channel : entry.getValue()) {
                        applyChannel(targetBone, channel, time);
                    }
                }
            }
        });
    }

    private static void applyChannel(ModelPart bone, AnimationChannel channel, float time) {
        Keyframe[] keyframes = channel.keyframes();
        if (keyframes.length == 0) return;
        if (time <= keyframes[0].timestamp()) {
            applyTarget(bone, channel.target(), keyframes[0].postTarget());
            return;
        }
        if (time >= keyframes[keyframes.length - 1].timestamp()) {
            applyTarget(bone, channel.target(), keyframes[keyframes.length - 1].postTarget());
            return;
        }
        for (int i = 0; i < keyframes.length - 1; i++) {
            Keyframe k0 = keyframes[i];
            Keyframe k1 = keyframes[i + 1];
            if (time >= k0.timestamp() && time <= k1.timestamp()) {
                float duration = k1.timestamp() - k0.timestamp();
                float progress = duration > 0.0F ? (time - k0.timestamp()) / duration : 0.0F;
                Vector3fc v0 = k0.postTarget();
                Vector3fc v1 = k1.preTarget();
                float x = Mth.lerp(progress, v0.x(), v1.x());
                float y = Mth.lerp(progress, v0.y(), v1.y());
                float z = Mth.lerp(progress, v0.z(), v1.z());
                applyTarget(bone, channel.target(), x, y, z);
                break;
            }
        }
    }

    private static void applyTarget(ModelPart bone, AnimationChannel.Target target, Vector3fc val) {
        applyTarget(bone, target, val.x(), val.y(), val.z());
    }

    private static void applyTarget(ModelPart bone, AnimationChannel.Target target, float x, float y, float z) {
        if (target == AnimationChannel.Targets.ROTATION) {
            bone.xRot += x;
            bone.yRot += y;
            bone.zRot += z;
        } else if (target == AnimationChannel.Targets.POSITION) {
            bone.x += x;
            bone.y += y;
            bone.z += z;
        }
    }

    private static ModelPart findBone(ModelPart part, String name) {
        if (part.hasChild(name)) {
            return part.getChild(name);
        }
        for (ModelPart child : part.getAllParts()) {
            if (child.hasChild(name)) {
                return child.getChild(name);
            }
        }
        return null;
    }
}