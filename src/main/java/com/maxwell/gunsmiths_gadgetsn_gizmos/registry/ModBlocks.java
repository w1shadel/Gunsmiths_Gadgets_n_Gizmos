package com.maxwell.gunsmiths_gadgetsn_gizmos.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks("gunsmiths_gadgetsn_gizmos");
    public static final DeferredBlock<Block> GUNSMITH_BENCH = BLOCKS.registerBlock(
            "gunsmith_bench",
            Block::new,
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.ANVIL)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .strength(2.5F, 6.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredItem<BlockItem> GUNSMITH_BENCH_ITEM = ModItems.ITEMS.registerSimpleBlockItem(
            GUNSMITH_BENCH
    );

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}