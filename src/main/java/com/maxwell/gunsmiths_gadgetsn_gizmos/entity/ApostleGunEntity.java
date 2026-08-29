package com.maxwell.gunsmiths_gadgetsn_gizmos.entity;

import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.boss.ApostleTitle;
import com.maxwell.gunsmiths_gadgetsn_gizmos.network.ClientboundAshStormPacket;
import io.redspace.irons_artifice.entity.IGunslingerMob;
import io.redspace.irons_artifice.entity.Illificer;
import io.redspace.irons_artifice.entity.ai.RangedGunAttackGoal;
import io.redspace.irons_artifice.item.GunItem;
import io.redspace.irons_artifice.item.ReloadState;
import io.redspace.irons_artifice.registry.EntityRegistry;
import io.redspace.irons_artifice.registry.ItemRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.illager.Pillager;
import net.minecraft.world.entity.monster.illager.SpellcasterIllager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.UUID;

public class ApostleGunEntity extends SpellcasterIllager implements IGunslingerMob {
    public static final byte EVENT_SHOOT_GUN = 100;
    public static final int TITLE_SHIFT_INTERVAL = 400;
    public static final int PHASE_TRANSITION_DURATION = 100;
    public static final int TELEPORT_TELEGRAPH_TICKS = 30;
    private static final EntityDataAccessor<Integer> DATA_TITLE =
            SynchedEntityData.defineId(ApostleGunEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_TRANSITIONING =
            SynchedEntityData.defineId(ApostleGunEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_PHASE_2 =
            SynchedEntityData.defineId(ApostleGunEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_TELEPORT_TIMER =
            SynchedEntityData.defineId(ApostleGunEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<org.joml.Vector3fc> DATA_TELEPORT_TARGET =
            SynchedEntityData.defineId(ApostleGunEntity.class, EntityDataSerializers.VECTOR3);
    private static final float MAX_DAMAGE_PER_HIT = 25.0F;
    public final AnimationState holdGunAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();
    public final AnimationState reloadPhaseInAnimationState = new AnimationState();
    public final AnimationState reloadLoopAnimationState = new AnimationState();
    public final AnimationState reloadEndAnimationState = new AnimationState();
    private final ServerBossEvent bossEvent = (ServerBossEvent) new ServerBossEvent(
            UUID.randomUUID(),
            this.getDisplayName(),
            BossEvent.BossBarColor.PURPLE,
            BossEvent.BossBarOverlay.NOTCHED_10
    ).setDarkenScreen(true);
    private int teleportCooldown = 260;
    private int titleShiftTimer = TITLE_SHIFT_INTERVAL;
    private boolean hasTriggeredPhase2 = false;
    private int transitionTicksRemaining = 0;

    public ApostleGunEntity(EntityType<? extends ApostleGunEntity> type, Level level) {
        super(type, level);
        this.xpReward = 100;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 350.0)
                .add(Attributes.MOVEMENT_SPEED, 0.33)
                .add(Attributes.FOLLOW_RANGE, 48.0)
                .add(Attributes.ARMOR, 16.0)
                .add(Attributes.ARMOR_TOUGHNESS, 8.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    public void customizeMobShot(Mob mob, io.redspace.irons_artifice.gun.ShotProfile profile) {
        profile.get(io.redspace.irons_artifice.data.ShotComponents.DAMAGE)
                .addModifier(new io.redspace.irons_artifice.data.ValueModifier(
                        this.isPhase2() ? 0.10 : 0.0,
                        io.redspace.irons_artifice.data.ValueModifier.Operation.MULTIPLY_TOTAL,
                        io.redspace.irons_artifice.data.ValueModifier.Type.BENEFICIAL
                ));

        profile.get(io.redspace.irons_artifice.data.ShotComponents.SPREAD)
                .addModifier(new io.redspace.irons_artifice.data.ValueModifier(
                        this.isPhase2() ? 5.5 : 3.5, 
                        io.redspace.irons_artifice.data.ValueModifier.Operation.ADD,
                        io.redspace.irons_artifice.data.ValueModifier.Type.NEUTRAL
                ));
    }

    public boolean isPhase2() {
        return this.entityData.get(DATA_PHASE_2);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TITLE, 0);
        builder.define(DATA_TRANSITIONING, false);
        builder.define(DATA_PHASE_2, false);
        builder.define(DATA_TELEPORT_TIMER, 0);
        builder.define(DATA_TELEPORT_TARGET, new org.joml.Vector3f());
    }

    @Override
    public void applyRaidBuffs(ServerLevel serverLevel, int i, boolean b) {
    }

    public int getTeleportTimer() {
        return this.entityData.get(DATA_TELEPORT_TIMER);
    }

    public Vec3 getTeleportTarget() {
        org.joml.Vector3fc v = this.entityData.get(DATA_TELEPORT_TARGET);
        return new Vec3(v.x(), v.y(), v.z());
    }

    public ApostleTitle getTitle() {
        int ordinal = this.entityData.get(DATA_TITLE);
        ApostleTitle[] values = ApostleTitle.values();
        return values[Mth.clamp(ordinal, 0, values.length - 1)];
    }

    public void setTitle(ApostleTitle title) {
        this.entityData.set(DATA_TITLE, title.ordinal());
        this.bossEvent.setName(this.getFullBossDisplayName());
        if (!this.level().isClientSide() && this.level() instanceof ServerLevel serverLevel) {
            title.onShift(this, serverLevel);
        }
    }

    public boolean isTransitioning() {
        return this.entityData.get(DATA_TRANSITIONING);
    }

    public Component getFullBossDisplayName() {
        return getTitle().getTitleComponent()
                .copy()
                .append(" ")
                .append(Component.translatable("entity.gunsmiths_gadgetsn_gizmos.apostle_gun"));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SpellcasterCastingSpellGoal());

        this.goalSelector.addGoal(2, new ApostleSummonMinionsGoal());
        this.goalSelector.addGoal(3, new ApostleVortexSpellGoal());
        this.goalSelector.addGoal(3, new ApostleFangsSpellGoal());

        this.goalSelector.addGoal(4, new ApostleCombatGoal());

        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }
    @Override
    public boolean hurtServer(@NonNull ServerLevel level, @NonNull DamageSource source, float damage) {
        if (this.isTransitioning()) {
            this.playSound(SoundEvents.ANVIL_LAND, 0.5F, 1.8F);
            return false;
        }
        if (source.getEntity() != null && source.getEntity().getPersistentData().getBooleanOr("apostle_minion", false)) {
            return false; 
        }
        if (source.is(io.redspace.irons_artifice.damage.DamageSources.BULLET_DAMAGE_TYPE)) {
            damage *= 0.65F;
        }
        damage = Math.min(damage, MAX_DAMAGE_PER_HIT);
        boolean damaged = super.hurtServer(level, source, damage);
        if (damaged && !level.isClientSide() && damage > 10.0F && this.getRandom().nextFloat() < 0.35F) {
            teleportToTacticalPosition(this.getTarget(), this.getRandom().nextBoolean());
        }
        return damaged;
    }

    public void teleportToTacticalPosition(@Nullable LivingEntity target, boolean behind) {
        if (target == null || !(this.level() instanceof ServerLevel level)) return;
        Vec3 targetPos = target.position();
        Vec3 look = target.getLookAngle();
        Vec3 dest;
        if (behind) {
            dest = targetPos.subtract(look.x * 4.5, 0, look.z * 4.5);
        } else {
            Vec3 away = this.position().subtract(targetPos).normalize().scale(12.0);
            dest = targetPos.add(away.x, 0, away.z);
        }
        if (this.randomTeleport(dest.x, target.getY(), dest.z, true)) {
            level.playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.5F, 0.8F);
            level.sendParticles(ParticleTypes.PORTAL, this.getX(), this.getY() + 1.0, this.getZ(),
                    30, 0.3, 0.5, 0.3, 0.1);
            this.lookAt(target, 180.0F, 180.0F);
        }
    }




    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            ServerLevel serverLevel = (ServerLevel) this.level();

            this.tickBossBar();
            this.tickAshStormPacket();

            if (this.tickPhaseTransition(serverLevel)) {
                return;
            }

            this.tickTitleShift();
            this.tickTeleportation(serverLevel);
        } else {
            this.updateClientAnimations();
        }
    }

    
    private void tickBossBar() {
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    
    private void tickAshStormPacket() {
        if (this.isPhase2() && this.isAlive()) {
            PacketDistributor.sendToPlayersTrackingEntity(this, ClientboundAshStormPacket.INSTANCE);
        }
    }

    
    private boolean tickPhaseTransition(ServerLevel level) {

        if (!this.hasTriggeredPhase2 && this.getHealth() <= 150.0F && this.isAlive()) {
            this.hasTriggeredPhase2 = true;
            this.transitionTicksRemaining = PHASE_TRANSITION_DURATION;
            this.entityData.set(DATA_TRANSITIONING, true);
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 2.0F, 0.6F);
        }

        if (this.isTransitioning()) {
            this.getNavigation().stop();
            this.setDeltaMovement(0, this.getDeltaMovement().y * 0.5, 0);
            this.heal(150.0F / (float) PHASE_TRANSITION_DURATION);

            level.sendParticles(ParticleTypes.PORTAL, this.getX(), this.getY() + 1.0, this.getZ(), 15, 0.5, 0.8, 0.5, 0.1);
            level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, this.getX(), this.getY() + 0.5, this.getZ(), 8, 0.4, 0.4, 0.4, 0.05);

            if (--this.transitionTicksRemaining <= 0) {
                this.entityData.set(DATA_TRANSITIONING, false);
                this.entityData.set(DATA_PHASE_2, true);
                this.setHealth(300.0F);

                AABB blastArea = this.getBoundingBox().inflate(6.0);
                for (LivingEntity target : this.level().getEntitiesOfClass(LivingEntity.class, blastArea, e -> e != this)) {
                    Vec3 push = target.position().subtract(this.position()).normalize().scale(1.5);
                    target.setDeltaMovement(push.x, 0.5, push.z);
                    target.hurtMarked = true;
                }

                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXPLODE.value(), SoundSource.HOSTILE, 3.0F, 0.8F);
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE, 2.5F, 1.0F);
                level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY() + 1.0, this.getZ(), 1, 0, 0, 0, 0);

                this.setTitle(ApostleTitle.selectRandom(this.getTitle(), true, false, this.level().dimension() == Level.END, this.getRandom()));
            }
            return true;
        }
        return false;
    }

    
    private void tickTitleShift() {
        if (this.getTarget() == null || !this.isAlive()) return;

        boolean isTheEnd = this.level().dimension() == Level.END;
        float hpRatio = this.getHealth() / this.getMaxHealth();

        if (isTheEnd && this.isPhase2()) {

            if (hpRatio <= 0.25F) {
                if (this.getTitle() != ApostleTitle.ABYSSAL_RULER_OF_ALL_CREATION) {
                    this.setTitle(ApostleTitle.ABYSSAL_RULER_OF_ALL_CREATION);
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITHER_DEATH, SoundSource.HOSTILE, 3.0F, 0.4F);
                }
            }

            else if (hpRatio <= 0.50F) {
                if (--this.titleShiftTimer <= 0) {
                    ApostleTitle nextTitle = ApostleTitle.selectRandom(this.getTitle(), true, true, true, this.getRandom());
                    this.setTitle(nextTitle);
                    this.titleShiftTimer = (nextTitle == ApostleTitle.RULER_OF_ALL_CREATION) ? 800 : 200;
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BELL_BLOCK, SoundSource.HOSTILE, 2.5F, 0.5F);
                }
            }

            else {
                if (--this.titleShiftTimer <= 0) {
                    this.titleShiftTimer = 240;
                    ApostleTitle nextTitle = ApostleTitle.selectRandom(this.getTitle(), true, false, true, this.getRandom());
                    this.setTitle(nextTitle);
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BELL_BLOCK, SoundSource.HOSTILE, 2.0F, 0.6F);
                }
            }
        } else {

            if (--this.titleShiftTimer <= 0) {
                this.titleShiftTimer = 240;
                boolean isLowHp = hpRatio <= 0.40F;
                ApostleTitle nextTitle = ApostleTitle.selectRandom(this.getTitle(), this.isPhase2(), isLowHp, false, this.getRandom());
                this.setTitle(nextTitle);
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BELL_BLOCK, SoundSource.HOSTILE, 2.0F, 0.6F);
            }
        }
    }

    
    private void tickTeleportation(ServerLevel level) {
        int currentTimer = this.getTeleportTimer();

        if (currentTimer <= 0 && this.getTarget() != null) {
            if (--this.teleportCooldown <= 0) {
                this.teleportCooldown = this.getRandom().nextIntBetweenInclusive(240, 320);
                LivingEntity target = this.getTarget();
                Vec3 look = target.getLookAngle();
                Vec3 dest = target.position().subtract(look.x * 5.0, 0, look.z * 5.0);

                this.entityData.set(DATA_TELEPORT_TARGET, new org.joml.Vector3f((float) dest.x, (float) dest.y, (float) dest.z));
                this.entityData.set(DATA_TELEPORT_TIMER, TELEPORT_TELEGRAPH_TICKS);
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PORTAL_TRIGGER, SoundSource.HOSTILE, 1.2F, 1.8F);
            }
        }

        if (currentTimer > 0) {
            this.entityData.set(DATA_TELEPORT_TIMER, currentTimer - 1);

            LivingEntity target = this.getTarget();
            Vec3 targetDest;
            if (target != null && target.isAlive()) {
                Vec3 look = target.getLookAngle();
                targetDest = target.position().subtract(look.x * 5.0, 0, look.z * 5.0);
                this.entityData.set(DATA_TELEPORT_TARGET, new org.joml.Vector3f((float) targetDest.x, (float) targetDest.y, (float) targetDest.z));
            } else {
                targetDest = this.getTeleportTarget();
            }

            level.sendParticles(ParticleTypes.SQUID_INK, targetDest.x, targetDest.y + 0.2, targetDest.z, 12, 0.4, 0.1, 0.4, 0.04);
            level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, targetDest.x, targetDest.y + 0.3, targetDest.z, 8, 0.3, 0.6, 0.3, 0.08);
            level.sendParticles(ParticleTypes.REVERSE_PORTAL, targetDest.x, targetDest.y + 1.2, targetDest.z, 15, 0.5, 1.0, 0.5, 0.15);
            level.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY() + 1.2, this.getZ(), 6, 0.3, 0.3, 0.3, 0.1);

            if (currentTimer - 1 == 0) {
                level.sendParticles(ParticleTypes.SQUID_INK, this.getX(), this.getY() + 1.0, this.getZ(), 60, 0.6, 1.0, 0.6, 0.12);
                level.sendParticles(ParticleTypes.LARGE_SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 30, 0.5, 0.8, 0.5, 0.08);
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.BEACON_DEACTIVATE, SoundSource.HOSTILE, 2.0F, 0.6F);

                this.setPos(targetDest.x, targetDest.y, targetDest.z);

                level.sendParticles(ParticleTypes.SONIC_BOOM, targetDest.x, targetDest.y + 1.0, targetDest.z, 1, 0, 0, 0, 0);
                level.sendParticles(ParticleTypes.SQUID_INK, targetDest.x, targetDest.y + 1.0, targetDest.z, 80, 0.8, 1.2, 0.8, 0.15);
                level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, targetDest.x, targetDest.y + 0.5, targetDest.z, 40, 0.6, 0.8, 0.6, 0.1);
                level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, targetDest.x, targetDest.y + 1.0, targetDest.z, 1, 0, 0, 0, 0);

                this.level().playSound(null, targetDest.x, targetDest.y, targetDest.z, SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE, 2.5F, 1.4F);
                this.level().playSound(null, targetDest.x, targetDest.y, targetDest.z, SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 2.0F, 0.7F);

                if (target != null && target.isAlive()) {
                    this.lookAt(target, 180.0F, 180.0F);
                    Vec3 aim = target.getEyePosition().subtract(this.getEyePosition()).normalize();
                    io.redspace.irons_artifice.item.GunplayManager.tryFire(this, aim);
                    this.level().broadcastEntityEvent(this, EVENT_SHOOT_GUN);
                }
            }
        }
    }

    @Override
    public boolean canBeAffected(net.minecraft.world.effect.MobEffectInstance effectInstance) {
        if (effectInstance.getEffect().value().getCategory() == net.minecraft.world.effect.MobEffectCategory.HARMFUL) {
            return false;
        }
        return super.canBeAffected(effectInstance);
    }

    @Override
    public boolean canFreeze() {
        return false;
    }

    private void updateClientAnimations() {
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
        }
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(@NonNull ServerLevelAccessor level, @NonNull DifficultyInstance difficulty, @NonNull EntitySpawnReason spawnReason, @Nullable SpawnGroupData spawnGroupData) {
        boolean isTheEnd = this.level().dimension() == Level.END;
        this.setTitle(ApostleTitle.selectRandom(null, false, false, isTheEnd, level.getRandom()));
        this.populateDefaultEquipmentSlots(level.getRandom(), difficulty);
        return super.finalizeSpawn(level, difficulty, spawnReason, spawnGroupData);
    }

    @Override
    public SoundEvent getCelebrateSound() {
        return null;
    }

    @Override
    protected void populateDefaultEquipmentSlots(@NonNull RandomSource random, @NonNull DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.CLOCKWORK_RIFLE.get()));
        this.setDropChance(EquipmentSlot.MAINHAND, 0.1F);
    }

    @Override
    public void startSeenByPlayer(@NonNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NonNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
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
    protected void addAdditionalSaveData(@NonNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("ApostleTitle", this.entityData.get(DATA_TITLE));
        output.putBoolean("HasTriggeredPhase2", this.hasTriggeredPhase2);
        output.putBoolean("IsPhase2", this.isPhase2());
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.entityData.set(DATA_TITLE, input.getIntOr("ApostleTitle", 0));
        this.hasTriggeredPhase2 = input.getBooleanOr("HasTriggeredPhase2", false);
        this.entityData.set(DATA_PHASE_2, input.getBooleanOr("IsPhase2", false));
        this.setTitle(this.getTitle());
    }

    @Override
    protected SoundEvent getCastingSoundEvent() {
        return SoundEvents.EVOKER_CAST_SPELL;
    }

    class ApostleFangsSpellGoal extends SpellcasterUseSpellGoal {
        @Override
        protected void performSpellCasting() {
            LivingEntity target = ApostleGunEntity.this.getTarget();
            if (target == null) return;
            Level level = ApostleGunEntity.this.level();
            double startX = ApostleGunEntity.this.getX();
            double startZ = ApostleGunEntity.this.getZ();
            double targetX = target.getX();
            double targetZ = target.getZ();
            double dx = targetX - startX;
            double dz = targetZ - startZ;
            float angle = (float) Mth.atan2(dz, dx);
            double dist = Math.min(ApostleGunEntity.this.distanceTo(target), 16.0);
            for (int i = 0; i < (int) (dist * 1.5); ++i) {
                double step = 1.0 + (double) i * 1.0;
                double fx = startX + (double) Mth.cos(angle) * step;
                double fz = startZ + (double) Mth.sin(angle) * step;
                level.addFreshEntity(new net.minecraft.world.entity.projectile.EvokerFangs(
                        level, fx, target.getY(), fz, angle, i * 2, ApostleGunEntity.this
                ));
            }
        }

        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 100;
        }

        @Override
        protected @Nullable SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_ATTACK;
        }

        @Override
        protected IllagerSpell getSpell() {
            return IllagerSpell.FANGS;
        }
    }

    class ApostleSummonMinionsGoal extends SpellcasterUseSpellGoal {
        @Override
        public boolean canUse() {
            if (!super.canUse()) return false;

            int aliveMinions = ApostleGunEntity.this.level().getEntitiesOfClass(
                    Monster.class,
                    ApostleGunEntity.this.getBoundingBox().inflate(32.0),
                    e -> e.isAlive() && e.getPersistentData().getBooleanOr("apostle_minion", false)
            ).size();

            return aliveMinions == 0;
        }

        @Override
        protected void performSpellCasting() {
            ServerLevel level = (ServerLevel) ApostleGunEntity.this.level();
            boolean isTheEnd = level.dimension() == net.minecraft.world.level.Level.END;

            if (isTheEnd) {

                if (ApostleGunEntity.this.isPhase2()) {

                    for (int i = 0; i < 2; i++) {
                        double spawnX = ApostleGunEntity.this.getX() + (ApostleGunEntity.this.getRandom().nextDouble() - 0.5) * 6.0;
                        double spawnZ = ApostleGunEntity.this.getZ() + (ApostleGunEntity.this.getRandom().nextDouble() - 0.5) * 6.0;
                        double spawnY = ApostleGunEntity.this.getY();

                        Illificer illificer = EntityRegistry.ILLIFICER.get().create(level, EntitySpawnReason.MOB_SUMMONED);
                        if (illificer != null) {
                            illificer.setPos(spawnX, spawnY, spawnZ);
                            illificer.getAttribute(Attributes.MAX_HEALTH).setBaseValue(35.0);
                            illificer.setHealth(35.0F);
                            illificer.setItemSlot(EquipmentSlot.MAINHAND, Illificer.createLoadout(level));
                            illificer.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
                            illificer.getPersistentData().putBoolean("apostle_minion", true);
                            level.addFreshEntity(illificer);
                        }
                        level.sendParticles(ParticleTypes.ASH, spawnX, spawnY + 0.5, spawnZ, 25, 0.3, 0.5, 0.3, 0.05);
                    }
                } else {

                    for (int i = 0; i < 2; i++) {
                        double spawnX = ApostleGunEntity.this.getX() + (ApostleGunEntity.this.getRandom().nextDouble() - 0.5) * 6.0;
                        double spawnZ = ApostleGunEntity.this.getZ() + (ApostleGunEntity.this.getRandom().nextDouble() - 0.5) * 6.0;
                        double spawnY = ApostleGunEntity.this.getY();

                        Illificer illificer = EntityRegistry.ILLIFICER.get().create(level, EntitySpawnReason.MOB_SUMMONED);
                        if (illificer != null) {
                            illificer.setPos(spawnX, spawnY, spawnZ);
                            illificer.getAttribute(Attributes.MAX_HEALTH).setBaseValue(18.0);
                            illificer.setHealth(18.0F);
                            illificer.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.ARQUEBUS.get()));
                            illificer.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
                            illificer.getPersistentData().putBoolean("apostle_minion", true);
                            level.addFreshEntity(illificer);
                        }
                        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, spawnX, spawnY + 0.5, spawnZ, 15, 0.2, 0.4, 0.2, 0.05);
                    }
                }
            } else {

                if (ApostleGunEntity.this.isPhase2()) {
                    for (int i = 0; i < 2; i++) {
                        double spawnX = ApostleGunEntity.this.getX() + (ApostleGunEntity.this.getRandom().nextDouble() - 0.5) * 6.0;
                        double spawnZ = ApostleGunEntity.this.getZ() + (ApostleGunEntity.this.getRandom().nextDouble() - 0.5) * 6.0;
                        double spawnY = ApostleGunEntity.this.getY();
                        Illificer illificer = EntityRegistry.ILLIFICER.get().create(level, EntitySpawnReason.MOB_SUMMONED);
                        if (illificer != null) {
                            illificer.setPos(spawnX, spawnY, spawnZ);
                            illificer.getAttribute(Attributes.MAX_HEALTH).setBaseValue(15.0);
                            illificer.setHealth(15.0F);
                            illificer.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(ItemRegistry.FLINTLOCK_PISTOL.get()));
                            illificer.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
                            illificer.getPersistentData().putBoolean("apostle_minion", true);
                            level.addFreshEntity(illificer);
                        }
                        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, spawnX, spawnY + 0.5, spawnZ, 20, 0.3, 0.5, 0.3, 0.05);
                    }
                } else {
                    for (int i = 0; i < 2; i++) {
                        double spawnX = ApostleGunEntity.this.getX() + (ApostleGunEntity.this.getRandom().nextDouble() - 0.5) * 6.0;
                        double spawnZ = ApostleGunEntity.this.getZ() + (ApostleGunEntity.this.getRandom().nextDouble() - 0.5) * 6.0;
                        double spawnY = ApostleGunEntity.this.getY();
                        Pillager minion = EntityType.PILLAGER.create(level, EntitySpawnReason.MOB_SUMMONED);
                        if (minion != null) {
                            minion.setPos(spawnX, spawnY, spawnZ);
                            minion.getAttribute(Attributes.MAX_HEALTH).setBaseValue(12.0);
                            minion.setHealth(12.0F);
                            minion.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(net.minecraft.world.item.Items.CROSSBOW));
                            minion.getPersistentData().putBoolean("apostle_minion", true);
                            minion.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
                            level.addFreshEntity(minion);
                        }
                        level.sendParticles(ParticleTypes.POOF, spawnX, spawnY + 0.5, spawnZ, 10, 0.2, 0.3, 0.2, 0.02);
                    }
                    for (int i = 0; i < 2; i++) {
                        double spawnX = ApostleGunEntity.this.getX() + (ApostleGunEntity.this.getRandom().nextDouble() - 0.5) * 4.0;
                        double spawnZ = ApostleGunEntity.this.getZ() + (ApostleGunEntity.this.getRandom().nextDouble() - 0.5) * 4.0;
                        double spawnY = ApostleGunEntity.this.getY() + 1.0;
                        net.minecraft.world.entity.monster.Vex vex = EntityType.VEX.create(level, EntitySpawnReason.MOB_SUMMONED);
                        if (vex != null) {
                            vex.setPos(spawnX, spawnY, spawnZ);
                            vex.setBoundOrigin(ApostleGunEntity.this.blockPosition());
                            vex.getPersistentData().putBoolean("apostle_minion", true);
                            vex.setLimitedLife(20 * 20);
                            level.addFreshEntity(vex);
                        }
                        level.sendParticles(ParticleTypes.SOUL, spawnX, spawnY, spawnZ, 8, 0.1, 0.1, 0.1, 0.02);
                    }
                }
            }

            level.playSound(null, ApostleGunEntity.this.getX(), ApostleGunEntity.this.getY(), ApostleGunEntity.this.getZ(),
                    SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.HOSTILE, 1.5F, 1.0F);
        }

        @Override
        protected int getCastingTime() {
            return 25;
        }

        @Override
        protected int getCastingInterval() {
            return 240;
        }

        @Override
        protected @Nullable SoundEvent getSpellPrepareSound() {
            return SoundEvents.EVOKER_PREPARE_SUMMON;
        }

        @Override
        protected IllagerSpell getSpell() {
            return IllagerSpell.SUMMON_VEX;
        }
    }

    class ApostleVortexSpellGoal extends SpellcasterUseSpellGoal {
        @Override
        public boolean canUse() {
            if (!super.canUse()) return false;
            LivingEntity target = ApostleGunEntity.this.getTarget();
            return target != null && ApostleGunEntity.this.distanceToSqr(target) > 64.0;
        }

        @Override
        protected void performSpellCasting() {
            LivingEntity target = ApostleGunEntity.this.getTarget();
            if (target == null) return;
            ServerLevel level = (ServerLevel) ApostleGunEntity.this.level();
            Vec3 pullDirection = ApostleGunEntity.this.position().subtract(target.position()).normalize().scale(1.4);
            target.setDeltaMovement(pullDirection.x, 0.4, pullDirection.z);
            target.hurtMarked = true;
            level.sendParticles(ParticleTypes.REVERSE_PORTAL, target.getX(), target.getY() + 1.0, target.getZ(), 30, 0.5, 0.5, 0.5, 0.1);
            level.playSound(null, target.getX(), target.getY(), target.getZ(),
                    SoundEvents.WIND_CHARGE_BURST.value(), SoundSource.HOSTILE, 2.0F, 0.6F);
            Vec3 aimVec = target.getEyePosition().subtract(ApostleGunEntity.this.getEyePosition()).normalize();
            io.redspace.irons_artifice.item.GunplayManager.tryFire(ApostleGunEntity.this, aimVec);
            ApostleGunEntity.this.level().broadcastEntityEvent(ApostleGunEntity.this, ApostleGunEntity.EVENT_SHOOT_GUN);
        }

        @Override
        protected int getCastingTime() {
            return 20;
        }

        @Override
        protected int getCastingInterval() {
            return 180;
        }

        @Override
        protected @Nullable SoundEvent getSpellPrepareSound() {
            return SoundEvents.PORTAL_TRIGGER;
        }

        @Override
        protected IllagerSpell getSpell() {
            return IllagerSpell.BLINDNESS;
        }
    }
    class ApostleCombatGoal extends net.minecraft.world.entity.ai.goal.Goal {
        private final io.redspace.irons_artifice.entity.ai.GunCombatMoveControl mover = new io.redspace.irons_artifice.entity.ai.GunCombatMoveControl();
        private final io.redspace.irons_artifice.entity.ai.AiGunRange bands = new io.redspace.irons_artifice.entity.ai.AiGunRange(28.0F);

        private int burstShotsRemaining = 0;
        private int burstCooldown = 0;

        public ApostleCombatGoal() {
            this.setFlags(java.util.EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = ApostleGunEntity.this.getTarget();
            return target != null && target.isAlive()
                    && ApostleGunEntity.this.getMainHandItem().getItem() instanceof GunItem
                    && !ApostleGunEntity.this.isTransitioning();
        }

        @Override
        public void start() {
            ApostleGunEntity.this.setAggressive(true);
            this.burstShotsRemaining = ApostleGunEntity.this.isPhase2() ? 10 : 3;
            this.burstCooldown = 0;
        }

        @Override
        public void stop() {
            ApostleGunEntity.this.setAggressive(false);
            this.mover.reset();
            ApostleGunEntity.this.getNavigation().stop();
            ApostleGunEntity.this.getMoveControl().strafe(0.0F, 0.0F);
        }

        @Override
        public void tick() {
            LivingEntity target = ApostleGunEntity.this.getTarget();
            if (target == null || !target.isAlive()) return;

            ItemStack gun = ApostleGunEntity.this.getMainHandItem();
            ApostleGunEntity.this.getLookControl().setLookAt(target, 30.0F, 30.0F);

            double distSqr = ApostleGunEntity.this.distanceToSqr(target);
            boolean hasLos = ApostleGunEntity.this.getSensing().hasLineOfSight(target);

            boolean isAnnihilation = ApostleGunEntity.this.getTitle() == ApostleTitle.ANNIHILATION_HARBINGER
                    || ApostleGunEntity.this.getTitle() == ApostleTitle.ABYSSAL_RULER_OF_ALL_CREATION;

            if (!ApostleGunEntity.this.isCastingSpell() || isAnnihilation) {
                var mode = this.mover.selectKiting(distSqr, hasLos, this.bands);
                double moveSpeed = ApostleGunEntity.this.isPhase2() ? 1.25 : 1.05;
                this.mover.tick(ApostleGunEntity.this, target, this.bands, mode, moveSpeed);
            }

            if (this.burstCooldown > 0) {
                this.burstCooldown--;
            }

            if (hasLos && distSqr <= 32.0 * 32.0 && (!ApostleGunEntity.this.isCastingSpell() || isAnnihilation) && this.burstCooldown <= 0) {
                if (GunItem.getMagazine(gun).isEmpty() && !GunItem.isReloading(gun)) {
                    io.redspace.irons_artifice.item.GunplayManager.attemptStartReload(ApostleGunEntity.this, gun);
                    this.burstShotsRemaining = isAnnihilation ? 30 : (ApostleGunEntity.this.isPhase2() ? 12 : 3);
                } else if (!io.redspace.irons_artifice.item.FireDelayState.isActive(gun) && !GunItem.isReloading(gun)) {
                    Vec3 targetCenter = target.getBoundingBox().getCenter();
                    Vec3 inaccuracy = new Vec3(
                            (ApostleGunEntity.this.getRandom().nextDouble() - 0.5) * 2.4,
                            (ApostleGunEntity.this.getRandom().nextDouble() - 0.5) * 1.6,
                            (ApostleGunEntity.this.getRandom().nextDouble() - 0.5) * 2.4
                    );
                    Vec3 aim = targetCenter.add(inaccuracy).subtract(ApostleGunEntity.this.getEyePosition()).normalize();

                    if (io.redspace.irons_artifice.item.GunplayManager.tryFire(ApostleGunEntity.this, aim)) {
                        ApostleGunEntity.this.level().broadcastEntityEvent(ApostleGunEntity.this, EVENT_SHOOT_GUN);
                        this.burstShotsRemaining--;

                        if (this.burstShotsRemaining <= 0) {

                            this.burstCooldown = isAnnihilation ? 0 : (ApostleGunEntity.this.isPhase2() ? 12 : 8);
                            this.burstShotsRemaining = isAnnihilation ? 30 : (ApostleGunEntity.this.isPhase2() ? 10 : 3);
                        }
                    }
                }
            }
        }
    }

}