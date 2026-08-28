package com.maxwell.gunsmiths_gadgetsn_gizmos.events;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.TownMarksmanEntity;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModBlocks;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

import java.util.List;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID)
public class MarksmanVillageSpawnerEvents {

    private static final int CHECK_INTERVAL = 400;

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

        for (var player : level.players()) {
            BlockPos playerPos = player.blockPosition();
            AABB villageArea = new AABB(playerPos).inflate(48);

            List<AbstractVillager> villagers = level.getEntitiesOfClass(AbstractVillager.class, villageArea);

            if (villagers.size() >= 3) {

                List<TownMarksmanEntity> marksmen = level.getEntitiesOfClass(TownMarksmanEntity.class, villageArea);
                if (marksmen.size() < 2) {

                    BlockPos.findClosestMatch(playerPos, 32, 16, pos -> level.getBlockState(pos).is(ModBlocks.GUNSMITH_BENCH.get()))
                            .ifPresent(benchPos -> {

                                int spawnX = benchPos.getX() + level.getRandom().nextIntBetweenInclusive(-3, 3);
                                int spawnZ = benchPos.getZ() + level.getRandom().nextIntBetweenInclusive(-3, 3);
                                int spawnY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, spawnX, spawnZ);
                                BlockPos spawnPos = new BlockPos(spawnX, spawnY, spawnZ);

                                TownMarksmanEntity marksman = ModEntities.TOWN_MARKSMAN.get().create(level, EntitySpawnReason.EVENT);
                                if (marksman != null) {
                                    marksman.setPos(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
                                    marksman.finalizeSpawn(level, level.getCurrentDifficultyAt(spawnPos), EntitySpawnReason.EVENT, null);
                                    level.addFreshEntity(marksman);
                                }
                            });
                }
            }
        }
    }
}