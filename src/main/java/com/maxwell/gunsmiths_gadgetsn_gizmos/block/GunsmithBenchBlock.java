package com.maxwell.gunsmiths_gadgetsn_gizmos.block;

import com.maxwell.gunsmiths_gadgetsn_gizmos.container.GunsmithBenchMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.NonNull;

public class GunsmithBenchBlock extends Block {
    public GunsmithBenchBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NonNull InteractionResult useWithoutItem(@NonNull BlockState state, @NonNull Level level, @NonNull BlockPos pos, @NonNull Player player, @NonNull BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            player.openMenu(new SimpleMenuProvider(
                    (id, inv, p) -> new GunsmithBenchMenu(id, inv, ContainerLevelAccess.create(level, pos)),
                    Component.translatable("container.gunsmiths_gadgetsn_gizmos.gunsmith_bench")
            ));
        }
        return InteractionResult.SUCCESS;
    }
}