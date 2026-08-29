package com.maxwell.gunsmiths_gadgetsn_gizmos.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.maxwell.gunsmiths_gadgetsn_gizmos.entity.TownMarksmanEntity;
import com.maxwell.gunsmiths_gadgetsn_gizmos.events.CultistAllianceEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;

@Mixin(Raid.class)
public class RaidMixin {
    
    @WrapOperation(
            method = "tick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerLevel;isVillage(Lnet/minecraft/core/BlockPos;)Z"
            )
    )
    private boolean gunsmiths_gadgetsn_gizmos$checkVillageIncludingMarksman(
            ServerLevel level,
            BlockPos centerPos,
            Operation<Boolean> original
    ) {
        boolean isVillage = original.call(level, centerPos);
        if (!isVillage) {
            return false;
        }
        AABB raidArea = AABB.ofSize(Vec3.atCenterOf(centerPos), 96 * 2, 96 * 2, 96 * 2);
        List<Villager> villagers = level.getEntitiesOfClass(Villager.class, raidArea, LivingEntity::isAlive);
        boolean hasNormalVillager = villagers.stream()
                .anyMatch(v -> !CultistAllianceEvents.isCultist(v));
        List<TownMarksmanEntity> marksmen = level.getEntitiesOfClass(TownMarksmanEntity.class, raidArea, LivingEntity::isAlive);
        boolean hasMarksman = !marksmen.isEmpty();
        if (hasNormalVillager || hasMarksman) {
            return true;
        }
        return false;
    }
}