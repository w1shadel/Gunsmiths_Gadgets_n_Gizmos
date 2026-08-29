package com.maxwell.gunsmiths_gadgetsn_gizmos.entity;

import com.maxwell.gunsmiths_gadgetsn_gizmos.events.CultistAllianceEvents;
import io.redspace.irons_artifice.entity.ai.RangedGunAttackGoal;
import io.redspace.irons_artifice.item.GunItem;
import io.redspace.irons_artifice.item.ReloadState;
import io.redspace.irons_artifice.registry.ItemRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public class TownMarksmanEntity extends PathfinderMob {
    public static final byte EVENT_SHOOT_GUN = 100;
    public final AnimationState holdGunAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();
    public final AnimationState reloadPhaseInAnimationState = new AnimationState();
    public final AnimationState reloadLoopAnimationState = new AnimationState();
    public final AnimationState reloadEndAnimationState = new AnimationState();

    public TownMarksmanEntity(EntityType<? extends TownMarksmanEntity> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.MOVEMENT_SPEED, 0.30)
                .add(Attributes.FOLLOW_RANGE, 32.0)
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RangedGunAttackGoal<>(this, 24, 15, 35, 30, 60));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(
                this, Mob.class, 10, true, false,
                (entity, level) -> entity instanceof Enemy && !CultistAllianceEvents.isCultist(entity)
        ));
    }

    @Override
    protected @NonNull InteractionResult mobInteract(@NonNull Player player, @NonNull InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.is(ItemRegistry.BULLET.get()) || heldItem.is(ItemRegistry.BLACKPOWDER.get())) {
            if (!this.level().isClientSide()) {
                heldItem.consume(1, player);
                this.heal(6.0F);
                this.playSound(SoundEvents.VILLAGER_YES, 1.0F, this.getVoicePitch());
                ((ServerLevel) this.level()).sendParticles(ParticleTypes.HAPPY_VILLAGER,
                        this.getX(), this.getY() + 1.2, this.getZ(),
                        8, 0.3, 0.3, 0.3, 0.05);
            }
            return InteractionResult.SUCCESS;
        }
        if (this.isAlive() && !this.isAggressive()) {
            if (!this.level().isClientSide()) {
                this.playSound(SoundEvents.VILLAGER_AMBIENT, 1.0F, this.getVoicePitch());
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.VILLAGER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@NonNull DamageSource source) {
        return SoundEvents.VILLAGER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.VILLAGER_DEATH;
    }

    public SoundEvent getCelebrateSound() {
        return SoundEvents.VILLAGER_CELEBRATE;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            ItemStack gun = this.getMainHandItem();
            boolean isHoldingGun = gun.getItem() instanceof GunItem;
            if (isHoldingGun) {
                boolean reloading = GunItem.isReloading(gun);
                ReloadState reloadState = ReloadState.get(gun);
                if (reloading && reloadState != null) {
                    this.holdGunAnimationState.stop();
                    this.shootAnimationState.stop();
                    double progress = reloadState.progress();
                    double duration = reloadState.duration();
                    if (progress < 0.5) {
                        this.reloadPhaseInAnimationState.startIfStopped(this.tickCount);
                        this.reloadLoopAnimationState.stop();
                        this.reloadEndAnimationState.stop();
                    } else if (progress >= Math.max(0.5, duration - 0.79)) {
                        this.reloadPhaseInAnimationState.stop();
                        this.reloadLoopAnimationState.stop();
                        this.reloadEndAnimationState.startIfStopped(this.tickCount);
                    } else {
                        this.reloadPhaseInAnimationState.stop();
                        this.reloadLoopAnimationState.startIfStopped(this.tickCount);
                        this.reloadEndAnimationState.stop();
                    }
                } else {
                    this.reloadPhaseInAnimationState.stop();
                    this.reloadLoopAnimationState.stop();
                    this.reloadEndAnimationState.stop();
                    this.holdGunAnimationState.startIfStopped(this.tickCount);
                }
            } else {
                this.holdGunAnimationState.stop();
                this.shootAnimationState.stop();
                this.reloadPhaseInAnimationState.stop();
                this.reloadLoopAnimationState.stop();
                this.reloadEndAnimationState.stop();
            }
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == EVENT_SHOOT_GUN) {
            this.shootAnimationState.start(this.tickCount);
        } else {
            super.handleEntityEvent(id);
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(@NonNull ServerLevelAccessor level, @NonNull DifficultyInstance difficulty, @NonNull EntitySpawnReason spawnReason, @Nullable SpawnGroupData spawnGroupData) {
        this.populateDefaultEquipmentSlots(level.getRandom(), difficulty);
        return super.finalizeSpawn(level, difficulty, spawnReason, spawnGroupData);
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NonNull RandomSource random, @NonNull DifficultyInstance difficulty) {
        ItemStack weapon = random.nextFloat() < 0.7F
                ? new ItemStack(ItemRegistry.MUSKET.get())
                : new ItemStack(ItemRegistry.FLINTLOCK_PISTOL.get());
        this.setItemSlot(EquipmentSlot.MAINHAND, weapon);
        this.setDropChance(EquipmentSlot.MAINHAND, 0.05F);
        if (random.nextBoolean()) {
            this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemRegistry.TRICORNE_HAT.get()));
        }
    }
}