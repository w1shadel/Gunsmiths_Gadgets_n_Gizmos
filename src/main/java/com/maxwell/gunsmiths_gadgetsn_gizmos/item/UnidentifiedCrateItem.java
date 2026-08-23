package com.maxwell.gunsmiths_gadgetsn_gizmos.item;

import com.maxwell.gunsmiths_gadgetsn_gizmos.registry.ModItems;
import io.redspace.irons_artifice.registry.ItemRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.function.Supplier;

public class UnidentifiedCrateItem extends Item {
    private static final List<Supplier<? extends Item>> REWARD_POOL = List.of(
            ItemRegistry.BULLET,
            ItemRegistry.BLACKPOWDER,
            ItemRegistry.SIMPLE_MECHANICAL_COMPONENTS,
            ItemRegistry.MECHANICAL_COMPONENTS,
            ItemRegistry.GUN_OIL,
            ItemRegistry.STEEL_CORE,
            ItemRegistry.SCATTERSHOT,
            ModItems.PISTON_RAMROD_MODIFIER,
            ModItems.TOWN_BELL_FLARE_MODIFIER
    );

    public UnidentifiedCrateItem(Properties properties) {
        super(properties.stacksTo(16));
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (!level.isClientSide() && level instanceof ServerLevel serverLevel) {
            RandomSource random = level.getRandom();
            Item rewardItem = REWARD_POOL.get(random.nextInt(REWARD_POOL.size())).get();
            int count = (rewardItem == ItemRegistry.BULLET.get() || rewardItem == ItemRegistry.BLACKPOWDER.get())
                    ? random.nextIntBetweenInclusive(8, 16)
                    : 1;
            ItemStack rewardStack = new ItemStack(rewardItem, count);
            if (!player.addItem(rewardStack)) {
                player.drop(rewardStack, false);
            }
            serverLevel.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.BARREL_OPEN, SoundSource.PLAYERS, 1.0F, 1.2F);
            serverLevel.sendParticles(ParticleTypes.CRIT,
                    player.getX(), player.getY() + 1.0, player.getZ(),
                    15, 0.3, 0.3, 0.3, 0.1);
        }
        held.consume(1, player);
        return InteractionResult.SUCCESS;
    }
}