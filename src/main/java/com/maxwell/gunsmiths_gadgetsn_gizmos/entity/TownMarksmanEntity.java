package com.maxwell.gunsmiths_gadgetsn_gizmos.entity;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.events.CultistAllianceEvents;
import io.redspace.irons_artifice.entity.ai.RangedGunAttackGoal;
import io.redspace.irons_artifice.item.GunItem;
import io.redspace.irons_artifice.item.ReloadState;
import io.redspace.irons_artifice.registry.ItemRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;

public class TownMarksmanEntity extends PathfinderMob {
    public static final byte EVENT_SHOOT_GUN = 100;
    private static final Identifier SPEED_BOOST_MODIFIER_ID = Identifier.fromNamespaceAndPath(GunsmithsGadgetsnGizmos.MODID, "town_marksman_speed_boost");
    public final AnimationState holdGunAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();
    public final AnimationState reloadPhaseInAnimationState = new AnimationState();
    public final AnimationState reloadLoopAnimationState = new AnimationState();
    public final AnimationState reloadEndAnimationState = new AnimationState();
    private int repairCooldown = 0;
    private int postCombatAlertTicks = 0;
    private int currentSpeedMode = 0;

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

    public static boolean isFriendly(Entity entity) {
        return entity instanceof TownMarksmanEntity
                || entity instanceof AbstractVillager
                || entity instanceof IronGolem;
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (isFriendly(target)) return false;
        return super.canAttack(target);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RangedGunAttackGoal<>(this, 24, 15, 35, 30, 60));
        this.goalSelector.addGoal(2, new RepairIronGolemGoal(this));
        this.goalSelector.addGoal(3, new FollowVillagerGoal(this));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 0.5, 60));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, AbstractVillager.class, 8.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && mob.getLastHurtByMob() != null && !isFriendly(mob.getLastHurtByMob());
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(
                this, Mob.class, 10, true, false,
                (entity, level) -> entity instanceof Enemy && !CultistAllianceEvents.isCultist(entity) && !isFriendly(entity)
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
        if (!this.level().isClientSide()) {
            if (this.repairCooldown > 0) {
                this.repairCooldown--;
            }
            boolean isCombat = (this.getTarget() != null && this.getTarget().isAlive()) || this.isAggressive();
            if (isCombat) {
                this.postCombatAlertTicks = 1200;
            } else if (this.postCombatAlertTicks > 0) {
                this.postCombatAlertTicks--;
            }
            updateSpeedModifier(isCombat, this.postCombatAlertTicks > 0);
        }
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

    private void updateSpeedModifier(boolean isCombat, boolean isAlert) {
        int targetMode = isCombat ? 2 : (isAlert ? 1 : 0);
        if (this.currentSpeedMode != targetMode) {
            this.currentSpeedMode = targetMode;
            AttributeInstance speedAttr = this.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speedAttr != null) {
                speedAttr.removeModifier(SPEED_BOOST_MODIFIER_ID);
                if (targetMode == 2) {
                    speedAttr.addTransientModifier(new AttributeModifier(
                            SPEED_BOOST_MODIFIER_ID, 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ));
                } else if (targetMode == 1) {
                    speedAttr.addTransientModifier(new AttributeModifier(
                            SPEED_BOOST_MODIFIER_ID, 0.10, AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ));
                }
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
        this.setDropChance(EquipmentSlot.MAINHAND, 0.0F);
        if (random.nextBoolean()) {
            this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(ItemRegistry.TRICORNE_HAT.get()));
            this.setDropChance(EquipmentSlot.HEAD, 0.0F);
        }
    }

    static class RepairIronGolemGoal extends Goal {
        private final TownMarksmanEntity mob;
        private IronGolem targetGolem;
        private int repairAnimationTicks = 0;

        public RepairIronGolemGoal(TownMarksmanEntity mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.mob.repairCooldown > 0) return false;
            if (this.mob.getTarget() != null && this.mob.getTarget().isAlive()) return false;
            List<IronGolem> golems = this.mob.level().getEntitiesOfClass(
                    IronGolem.class,
                    this.mob.getBoundingBox().inflate(16.0),
                    g -> g.isAlive() && g.getHealth() < g.getMaxHealth()
            );
            if (golems.isEmpty()) return false;
            this.targetGolem = golems.stream()
                    .min(Comparator.comparingDouble(IronGolem::getHealth))
                    .orElse(null);
            return this.targetGolem != null;
        }

        @Override
        public boolean canContinueToUse() {
            if (this.mob.getTarget() != null && this.mob.getTarget().isAlive()) return false;
            if (this.targetGolem == null || !this.targetGolem.isAlive() || this.targetGolem.getHealth() >= this.targetGolem.getMaxHealth())
                return false;
            return this.repairAnimationTicks < 40;
        }

        @Override
        public void start() {
            this.repairAnimationTicks = 0;
            if (this.targetGolem != null) {
                this.mob.getNavigation().moveTo(this.targetGolem, 0.6);
            }
        }

        @Override
        public void tick() {
            if (this.targetGolem == null) return;
            this.mob.getLookControl().setLookAt(this.targetGolem, 30.0F, 30.0F);
            double distSq = this.mob.distanceToSqr(this.targetGolem);
            if (distSq > 9.0) {
                this.mob.getNavigation().moveTo(this.targetGolem, 0.6);
            } else {
                this.mob.getNavigation().stop();
                this.repairAnimationTicks++;
                if (this.repairAnimationTicks % 10 == 0 && this.mob.level() instanceof ServerLevel serverLevel) {
                    serverLevel.playSound(null, this.targetGolem.getX(), this.targetGolem.getY(), this.targetGolem.getZ(),
                            SoundEvents.IRON_GOLEM_REPAIR, SoundSource.NEUTRAL, 1.0F, 0.9F + serverLevel.getRandom().nextFloat() * 0.2F);
                    serverLevel.sendParticles(ParticleTypes.WAX_OFF,
                            this.targetGolem.getX(), this.targetGolem.getY() + 1.2, this.targetGolem.getZ(),
                            5, 0.3, 0.4, 0.3, 0.05);
                }
                if (this.repairAnimationTicks >= 40) {
                    this.targetGolem.heal(25.0F);
                    this.mob.repairCooldown = 800;
                    if (this.mob.level() instanceof ServerLevel serverLevel) {
                        serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
                                this.targetGolem.getX(), this.targetGolem.getY() + 1.5, this.targetGolem.getZ(),
                                10, 0.4, 0.5, 0.4, 0.05);
                    }
                }
            }
        }
    }

    static class FollowVillagerGoal extends Goal {
        private final TownMarksmanEntity mob;
        private AbstractVillager targetVillager;

        public FollowVillagerGoal(TownMarksmanEntity mob) {
            this.mob = mob;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.mob.getTarget() != null && this.mob.getTarget().isAlive()) return false;
            List<AbstractVillager> villagers = this.mob.level().getEntitiesOfClass(
                    AbstractVillager.class,
                    this.mob.getBoundingBox().inflate(32.0),
                    LivingEntity::isAlive
            );
            if (villagers.isEmpty()) return false;
            this.targetVillager = villagers.stream()
                    .min(Comparator.comparingDouble(this.mob::distanceToSqr))
                    .orElse(null);
            if (this.targetVillager == null) return false;
            return this.mob.distanceToSqr(this.targetVillager) > 64.0;
        }

        @Override
        public boolean canContinueToUse() {
            if (this.mob.getTarget() != null && this.mob.getTarget().isAlive()) return false;
            if (this.targetVillager == null || !this.targetVillager.isAlive()) return false;
            return !this.mob.getNavigation().isDone() && this.mob.distanceToSqr(this.targetVillager) > 16.0;
        }

        @Override
        public void start() {
            if (this.targetVillager != null) {
                this.mob.getNavigation().moveTo(this.targetVillager, 0.6);
            }
        }
    }
}