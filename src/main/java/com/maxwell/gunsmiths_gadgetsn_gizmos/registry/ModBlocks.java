package com.maxwell.gunsmiths_gadgetsn_gizmos.registry;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.block.GunsmithBenchBlock;
import com.maxwell.gunsmiths_gadgetsn_gizmos.block.alter.CursedAltarBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(GunsmithsGadgetsnGizmos.MODID);
    public static final DeferredBlock<GunsmithBenchBlock> GUNSMITH_BENCH = BLOCKS.registerBlock(
            "gunsmith_bench",
            GunsmithBenchBlock::new,
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.ANVIL)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .strength(2.5F, 6.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredItem<BlockItem> GUNSMITH_BENCH_ITEM = ModItems.ITEMS.registerSimpleBlockItem(
            GUNSMITH_BENCH
    );
    public static final DeferredBlock<CursedAltarBlock> CURSED_ALTAR = BLOCKS.registerBlock(
            "cursed_altar",
            CursedAltarBlock::new,
            () -> BlockBehaviour.Properties.ofFullCopy(Blocks.CRYING_OBSIDIAN)
                    .sound(SoundType.NETHERITE_BLOCK)
                    .lightLevel(state -> 7)
                    .strength(3.0F, 9.0F)
                    .requiresCorrectToolForDrops()
    );
    public static final DeferredItem<BlockItem> CURSED_ALTAR_ITEM = ModItems.ITEMS.registerSimpleBlockItem(
            CURSED_ALTAR
    );

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}