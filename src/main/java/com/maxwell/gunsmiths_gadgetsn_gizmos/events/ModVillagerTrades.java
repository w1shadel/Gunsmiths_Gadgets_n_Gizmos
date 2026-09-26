package com.maxwell.gunsmiths_gadgetsn_gizmos.events;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModBlocks;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModItems;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModVillagers;
import io.redspace.irons_artifice.registry.ItemRegistry;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModVillagerTrades {

    @SubscribeEvent
    public static void registerVillagerTrades(VillagerTradesEvent event) {

        if (event.getType() == ModVillagers.GUNSMITH.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(1).add(trade(Items.IRON_INGOT, 4, Items.EMERALD, 1, 16, 2, 0.05F));
            trades.get(1).add(trade(Items.COPPER_INGOT, 6, Items.EMERALD, 1, 16, 2, 0.05F));
            trades.get(1).add(trade(Items.CHARCOAL, 16, Items.EMERALD, 1, 16, 2, 0.05F));
            trades.get(1).add(trade(Items.EMERALD, 1, ItemRegistry.BULLET.get(), 16, 12, 1, 0.05F));
            trades.get(1).add(trade(Items.EMERALD, 1, ItemRegistry.BLACKPOWDER.get(), 4, 12, 1, 0.05F));

            trades.get(2).add(trade(Items.GUNPOWDER, 6, Items.EMERALD, 1, 16, 5, 0.05F));
            trades.get(2).add(trade(Items.REDSTONE, 12, Items.EMERALD, 1, 16, 5, 0.05F));
            trades.get(2).add(trade(Items.EMERALD, 3, ItemRegistry.SIMPLE_MECHANICAL_COMPONENTS.get(), 1, 8, 5, 0.05F));
            trades.get(2).add(trade(Items.EMERALD, 4, ItemRegistry.GUN_OIL.get(), 1, 6, 5, 0.05F));

            trades.get(3).add(trade(Items.GOLD_INGOT, 3, Items.EMERALD, 1, 16, 10, 0.05F));
            trades.get(3).add(trade(Items.BLAZE_POWDER, 4, Items.EMERALD, 1, 12, 10, 0.05F));
            trades.get(3).add(trade(Items.EMERALD, 5, ModItems.TOWN_BELL_FLARE_MODIFIER.get(), 1, 4, 10, 0.05F));
            trades.get(3).add(trade(Items.EMERALD, 6, ItemRegistry.STEEL_CORE.get(), 1, 4, 10, 0.05F));
            trades.get(3).add(trade(Items.EMERALD, 4, ModItems.UNIDENTIFIED_CRATE.get(), 1, 6, 10, 0.05F));

            trades.get(4).add(trade(Items.AMETHYST_SHARD, 5, Items.EMERALD, 1, 12, 15, 0.05F));
            trades.get(4).add(trade(Items.BREEZE_ROD, 2, Items.EMERALD, 3, 8, 15, 0.05F));
            trades.get(4).add(trade(Items.EMERALD, 7, ModItems.PISTON_RAMROD_MODIFIER.get(), 1, 3, 15, 0.05F));
            trades.get(4).add(trade(Items.EMERALD, 8, ModItems.BREEZE_CYCLONE_MODIFIER.get(), 1, 3, 15, 0.05F));
            trades.get(4).add(trade(Items.EMERALD, 8, ModItems.MERCHANT_BOUNTY_MODIFIER.get(), 1, 3, 15, 0.05F));

            trades.get(5).add(trade(Items.NETHERITE_SCRAP, 1, Items.EMERALD, 10, 4, 30, 0.05F));
            trades.get(5).add(trade(Items.EMERALD, 18, ItemRegistry.SIX_SHOOTER.get(), 1, 2, 30, 0.05F));
            trades.get(5).add(trade(Items.EMERALD, 10, ItemRegistry.CLOCKWORK_COMPONENTS.get(), 1, 4, 30, 0.05F));
            trades.get(5).add(trade(Items.EMERALD, 14, Items.IRON_BLOCK, 1, ModItems.HEAVY_CORE_IMPACT_MODIFIER.get(), 1, 2, 30, 0.05F));
            trades.get(5).add(trade(Items.EMERALD, 16, ModItems.MASTERCRAFTED_TRIGGER_MODIFIER.get(), 1, 2, 30, 0.05F));
        }

        if (event.getType() == ModVillagers.CULTIST.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(1).add(trade(Items.ROTTEN_FLESH, 24, Items.EMERALD, 1, 16, 2, 0.05F));
            trades.get(1).add(trade(Items.SOUL_SAND, 8, ModItems.SOUL_CINDER.get(), 2, 12, 2, 0.05F));
            trades.get(1).add(trade(Items.EMERALD, 2, ModItems.VOID_CASING.get(), 1, 12, 1, 0.05F));

            trades.get(2).add(trade(Items.GLASS_BOTTLE, 8, Items.EMERALD, 1, 16, 5, 0.05F));
            trades.get(2).add(trade(ModItems.SOUL_CINDER.get(), 2, Items.GOLD_INGOT, 2, ModItems.CURSED_BRASS_INGOT.get(), 1, 8, 5, 0.05F));
            trades.get(2).add(trade(Items.EMERALD, 3, ModItems.SOUL_CINDER.get(), 2, 8, 5, 0.05F));

            trades.get(3).add(trade(Items.OMINOUS_BOTTLE, 1, ModItems.COAGULATED_OMEN_BLOOD.get(), 2, 6, 10, 0.05F));
            trades.get(3).add(trade(Items.EMERALD, 6, ModItems.CURSED_BRASS_INGOT.get(), 1, 6, 10, 0.05F));
            trades.get(3).add(trade(Items.EMERALD, 5, ModItems.COAGULATED_OMEN_BLOOD.get(), 1, 6, 10, 0.05F));

            trades.get(4).add(trade(Items.WITHER_SKELETON_SKULL, 1, Items.EMERALD, 8, 4, 15, 0.05F));
            trades.get(4).add(trade(Items.EMERALD, 12, ModItems.FORBIDDEN_BLUEPRINT.get(), 1, 3, 15, 0.05F));
            trades.get(4).add(trade(Items.ECHO_SHARD, 1, Items.EMERALD, 6, 6, 15, 0.05F));

            trades.get(5).add(trade(Items.NETHER_STAR, 1, Items.EMERALD, 32, 2, 30, 0.05F));
            trades.get(5).add(trade(Items.EMERALD, 8, ModItems.UNIDENTIFIED_CRATE.get(), 2, 4, 30, 0.05F));
        }
    }

    private static VillagerTrades.ItemListing trade(ItemLike cost, int costCount, ItemLike result, int resultCount, int maxUses, int xp, float priceMultiplier) {
        return (trader, random) -> new MerchantOffer(
                new ItemCost(cost, costCount),
                new ItemStack(result, resultCount),
                maxUses, xp, priceMultiplier
        );
    }

    private static VillagerTrades.ItemListing trade(ItemLike costA, int countA, ItemLike costB, int countB, ItemLike result, int resultCount, int maxUses, int xp, float priceMultiplier) {
        return (trader, random) -> new MerchantOffer(
                new ItemCost(costA, countA),
                Optional.of(new ItemCost(costB, countB)),
                new ItemStack(result, resultCount),
                maxUses, xp, priceMultiplier
        );
    }
}