package com.maxwell.gunsmiths_gadgetsn_gizmos.client.animation;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;


public class TownMarksmanAnimation {
    public static final AnimationDefinition hold_gun = AnimationDefinition.Builder.withLength(0.0F)
            .addAnimation("right", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-90.0F, -12.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("light", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-86.1138F, 49.9349F, 2.9759F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();
    public static final AnimationDefinition shoot_gun = AnimationDefinition.Builder.withLength(0.5417F)
            .addAnimation("right", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-87.5F, -12.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.0833F, KeyframeAnimations.degreeVec(-112.5F, -12.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-87.5F, -12.5F, 0.0F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("light", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-86.1138F, 49.9349F, 2.9759F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();
    public static final AnimationDefinition reload_phasein = AnimationDefinition.Builder.withLength(0.5F)
            .addAnimation("right", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-87.5F, -12.5F, 0.0F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-50.4239F, -28.4841F, -12.8967F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("light", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-86.1138F, 49.9349F, 2.9759F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.4167F, KeyframeAnimations.degreeVec(-65.6772F, 40.9821F, -3.4038F), AnimationChannel.Interpolations.LINEAR)
            ))
            .build();
    public static final AnimationDefinition reloading_loop = AnimationDefinition.Builder.withLength(1.0833F).looping()
            .addAnimation("right", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-50.4239F, -28.4841F, -12.8967F), AnimationChannel.Interpolations.LINEAR)
            ))
            .addAnimation("light", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-65.6772F, 40.9821F, -3.4038F), AnimationChannel.Interpolations.LINEAR),
                    new Keyframe(0.25F, KeyframeAnimations.degreeVec(-70.9532F, 17.6773F, -13.9306F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-63.6641F, 45.5026F, -0.4681F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(-70.6725F, 20.0385F, -13.0625F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-61.1138F, 49.9349F, 2.9758F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();
    public static final AnimationDefinition reload_end = AnimationDefinition.Builder.withLength(0.7917F)
            .addAnimation("right", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-50.4239F, -28.4841F, -12.8967F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(-87.5F, -12.5F, 0.0F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .addAnimation("light", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-65.6772F, 40.9821F, -3.4038F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.75F, KeyframeAnimations.degreeVec(-86.1138F, 49.9349F, 2.9759F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();
}