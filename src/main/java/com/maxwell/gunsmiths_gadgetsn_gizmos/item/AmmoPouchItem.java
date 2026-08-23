package com.maxwell.gunsmiths_gadgetsn_gizmos.item;

import com.maxwell.gunsmiths_gadgetsn_gizmos.api.ammo.IAmmoContainer;
import com.maxwell.gunsmiths_gadgetsn_gizmos.container.AmmoPouchMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

public class AmmoPouchItem extends Item implements IAmmoContainer {
    protected final int slots;

    public AmmoPouchItem(Properties properties, int slots) {
        super(properties
                .stacksTo(1)
                .component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
        );
        this.slots = slots;
    }

    public AmmoPouchItem(Properties properties) {
        this(properties, 4);
    }

    @Override
    public int getPouchSlotCount(ItemStack containerStack) {
        return this.slots;
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        ItemStack held = player.getItemInHand(hand);
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (id, inv, p) -> new AmmoPouchMenu(id, inv, held),
                    Component.translatable("item.gunsmiths_gadgetsn_gizmos.ammo_pouch")
            ));
            level.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ARMOR_EQUIP_LEATHER, SoundSource.PLAYERS, 1.0F, 1.2F);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void appendHoverText(@NonNull ItemStack itemStack, @NonNull TooltipContext context, @NonNull TooltipDisplay display, @NonNull Consumer<Component> builder, @NonNull TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.tooltip.ammo_pouch").withStyle(ChatFormatting.GOLD));
        builder.accept(Component.translatable("gunsmiths_gadgetsn_gizmos.tooltip.ammo_pouch.desc").withStyle(ChatFormatting.GRAY));
    }
}