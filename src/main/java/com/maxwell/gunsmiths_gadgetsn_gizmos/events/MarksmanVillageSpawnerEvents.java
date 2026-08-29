package com.maxwell.gunsmiths_gadgetsn_gizmos.events;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.TownMarksmanEntity;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.GunsmithConfig;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModBlocks;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModEntities;
import io.redspace.irons_artifice.entity.Bullet;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID)
public class MarksmanVillageSpawnerEvents {
    private static final int CHECK_INTERVAL = 200;

    @SubscribeEvent
    public static void onMarksmanFriendlyFire(LivingDamageEvent.Pre event) {
        if (event.getSource().getDirectEntity() instanceof Bullet bullet) {
            if (bullet.getOwner() instanceof TownMarksmanEntity) {
                LivingEntity victim = event.getEntity();
                if (TownMarksmanEntity.isFriendly(victim)) {
                    event.setNewDamage(0.0F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onFriendlyTargetMarksman(LivingChangeTargetEvent event) {
        LivingEntity attacker = event.getEntity();
        LivingEntity target = event.getNewAboutToBeSetTarget();
        if (TownMarksmanEntity.isFriendly(attacker) && target != null && TownMarksmanEntity.isFriendly(target)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onMarksmanShoot(io.redspace.irons_artifice.api.GunShootEvent.Post event) {
        if (event.getEntity() instanceof TownMarksmanEntity marksman) {
            if (!marksman.level().isClientSide()) {
                marksman.level().broadcastEntityEvent(marksman, TownMarksmanEntity.EVENT_SHOOT_GUN);
            }
        }
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (level.getGameTime() % CHECK_INTERVAL != 0) return;
        int configMax = GunsmithConfig.COMMON.maxMarksmanPerVillage.get();
        if (configMax <= 0) return;
        for (var player : level.players()) {
            BlockPos playerPos = player.blockPosition();
            AABB searchArea = new AABB(playerPos).inflate(48);
            List<AbstractVillager> villagers = level.getEntitiesOfClass(AbstractVillager.class, searchArea, LivingEntity::isAlive);
            if (villagers.size() >= 3) {
                Optional<BlockPos> benchPosOpt = BlockPos.findClosestMatch(playerPos, 32, 16, pos -> level.getBlockState(pos).is(ModBlocks.GUNSMITH_BENCH.get()));
                BlockPos villageCenter = benchPosOpt.orElseGet(() -> villagers.get(0).blockPosition());
                AABB villageBounds = new AABB(villageCenter).inflate(64);
                List<TownMarksmanEntity> currentMarksmen = level.getEntitiesOfClass(TownMarksmanEntity.class, villageBounds, LivingEntity::isAlive);
                int allowedMarksmen = Math.min(configMax, Math.max(1, villagers.size() / 3));
                if (currentMarksmen.size() < allowedMarksmen) {
                    boolean shouldSpawn = currentMarksmen.isEmpty() || level.getRandom().nextFloat() < 0.30F;
                    if (shouldSpawn) {
                        int spawnX = villageCenter.getX() + level.getRandom().nextIntBetweenInclusive(-4, 4);
                        int spawnZ = villageCenter.getZ() + level.getRandom().nextIntBetweenInclusive(-4, 4);
                        int spawnY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, spawnX, spawnZ);
                        BlockPos spawnPos = new BlockPos(spawnX, spawnY, spawnZ);
                        if (level.getBlockState(spawnPos.below()).isSolid()) {
                            TownMarksmanEntity marksman = ModEntities.TOWN_MARKSMAN.get().create(level, EntitySpawnReason.EVENT);
                            if (marksman != null) {
                                marksman.setPos(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
                                marksman.finalizeSpawn(level, level.getCurrentDifficultyAt(spawnPos), EntitySpawnReason.EVENT, null);
                                level.addFreshEntity(marksman);
                            }
                        }
                    }
                }
            }
        }
    }
}