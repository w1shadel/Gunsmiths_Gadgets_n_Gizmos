package com.maxwell.gunsmiths_gadgetsn_gizmos.entity;

import com.maxwell.gunsmiths_gadgetsn_gizmos.client.ClientApostleHandler;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.boss.ApostleTitle;
import com.maxwell.gunsmiths_gadgetsn_gizmos.network.ClientboundAshStormPacket;
import io.redspace.irons_artifice.entity.ai.RangedGunAttackGoal;
import io.redspace.irons_artifice.item.GunItem;
import io.redspace.irons_artifice.item.ReloadState;
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

public class ApostleGunEntity extends SpellcasterIllager {
    public static final byte EVENT_SHOOT_GUN = 100;
    public static final int TITLE_SHIFT_INTERVAL = 400;
    public static final int PHASE_TRANSITION_DURATION = 100;
    private static final EntityDataAccessor<Integer> DATA_TITLE =
            SynchedEntityData.defineId(ApostleGunEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DATA_TRANSITIONING =
            SynchedEntityData.defineId(ApostleGunEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> DATA_PHASE_2 =
            SynchedEntityData.defineId(ApostleGunEntity.class, EntityDataSerializers.BOOLEAN);
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

    public boolean isPhase2() {
        return this.entityData.get(DATA_PHASE_2);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TITLE, 0);
        builder.define(DATA_TRANSITIONING, false);
        builder.define(DATA_PHASE_2, false);
    }

    @Override
    public void applyRaidBuffs(ServerLevel serverLevel, int i, boolean b) {
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
        this.goalSelector.addGoal(2, new ApostleFangsSpellGoal());
        this.goalSelector.addGoal(3, new RangedGunAttackGoal<>(this, 28, 10, 30, 20, 50));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 12.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Raider.class).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    private static final float MAX_DAMAGE_PER_HIT = 25.0F;

    @Override
    public boolean hurtServer(@NonNull ServerLevel level, @NonNull DamageSource source, float damage) {

        if (this.isTransitioning()) {
            this.playSound(SoundEvents.ANVIL_LAND, 0.5F, 1.8F);
            return false;
        }

        if (source.is(io.redspace.irons_artifice.damage.DamageSources.BULLET_DAMAGE_TYPE)) {
            damage *= 0.65F;
        }

        damage = Math.min(damage, MAX_DAMAGE_PER_HIT);

        boolean damaged = super.hurtServer(level, source, damage);

        if (damaged && !level.isClientSide() && damage > 10.0F && this.getRandom().nextFloat() < 0.35F) {
            teleportAway(level);
        }

        return damaged;
    }

    private void teleportAway(ServerLevel level) {
        for (int i = 0; i < 16; i++) {
            double tx = this.getX() + (this.getRandom().nextDouble() - 0.5) * 16.0;
            double tz = this.getZ() + (this.getRandom().nextDouble() - 0.5) * 16.0;
            double ty = this.getY() + (this.getRandom().nextInt(5) - 2);

            if (this.randomTeleport(tx, ty, tz, true)) {
                level.playSound(null, this.getX(), this.getY(), this.getZ(),
                        SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 1.5F, 0.8F);
                level.sendParticles(ParticleTypes.PORTAL, this.getX(), this.getY() + 1.0, this.getZ(),
                        25, 0.3, 0.5, 0.3, 0.1);
                break;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
            ServerLevel serverLevel = (ServerLevel) this.level();
            if (this.isPhase2() && this.isAlive()) {
                PacketDistributor.sendToPlayersTrackingEntity(this, ClientboundAshStormPacket.INSTANCE);
            }
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
                serverLevel.sendParticles(ParticleTypes.PORTAL,
                        this.getX(), this.getY() + 1.0, this.getZ(),
                        15, 0.5, 0.8, 0.5, 0.1);
                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                        this.getX(), this.getY() + 0.5, this.getZ(),
                        8, 0.4, 0.4, 0.4, 0.05);
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
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                            SoundEvents.GENERIC_EXPLODE.value(), SoundSource.HOSTILE, 3.0F, 0.8F);
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                            SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE, 2.5F, 1.0F);
                    serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER,
                            this.getX(), this.getY() + 1.0, this.getZ(), 1, 0, 0, 0, 0);
                    this.setTitle(ApostleTitle.ANNIHILATION_HARBINGER);
                }
                return;
            }
            if (this.getTarget() != null && this.isAlive()) {
                if (--this.titleShiftTimer <= 0) {
                    this.titleShiftTimer = TITLE_SHIFT_INTERVAL;
                    ApostleTitle nextTitle = ApostleTitle.randomExcept(this.getTitle(), this.getRandom());
                    this.setTitle(nextTitle);
                    this.level().playSound(null, this.getX(), this.getY(), this.getZ(),
                            SoundEvents.BELL_BLOCK, SoundSource.HOSTILE, 2.0F, 0.6F);
                }
            }
        } else {
            updateClientAnimations();
        }
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
        this.setTitle(ApostleTitle.random(level.getRandom()));
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
            if (target != null) {
                double targetX = target.getX();
                double targetZ = target.getZ();
                double targetY = target.getY();
                for (int i = 0; i < 8; ++i) {
                    float angle = (float) i * (float) Math.PI / 4.0F;
                    ApostleGunEntity.this.level().addFreshEntity(new net.minecraft.world.entity.projectile.EvokerFangs(
                            ApostleGunEntity.this.level(), targetX + (double) Mth.cos(angle) * 1.5, targetY, targetZ + (double) Mth.sin(angle) * 1.5,
                            0.0F, 0, ApostleGunEntity.this
                    ));
                }
            }
        }

        @Override
        protected int getCastingTime() {
            return 40;
        }

        @Override
        protected int getCastingInterval() {
            return 140;
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
}