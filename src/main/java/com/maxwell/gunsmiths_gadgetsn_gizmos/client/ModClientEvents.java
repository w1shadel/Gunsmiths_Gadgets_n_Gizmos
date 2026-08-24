package com.maxwell.gunsmiths_gadgetsn_gizmos.client;

import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.model.DefaultedItemGeoModel;
import com.geckolib.renderer.GeoItemRenderer;
import com.google.common.base.Suppliers;
import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.client.gui.AmmoPouchScreen;
import com.maxwell.gunsmiths_gadgetsn_gizmos.client.gui.CursedAltarScreen;
import com.maxwell.gunsmiths_gadgetsn_gizmos.client.gui.GunsmithBenchScreen;
import com.maxwell.gunsmiths_gadgetsn_gizmos.registry.ModItems;
import com.maxwell.gunsmiths_gadgetsn_gizmos.registry.ModMenuTypes;
import io.redspace.irons_artifice.client.gun.GunArmPoses;
import io.redspace.irons_artifice.client.gun.GunInHandRenderer;
import io.redspace.irons_artifice.item.GunItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID, value = Dist.CLIENT)
public class ModClientEvents {
    @SubscribeEvent
    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.CURSED_ALTAR_MENU.get(), CursedAltarScreen::new);
        event.register(ModMenuTypes.GUNSMITH_BENCH_MENU.get(), GunsmithBenchScreen::new);
        event.register(ModMenuTypes.AMMO_POUCH_MENU.get(), AmmoPouchScreen::new);
    }
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        if (ModItems.CLUNKER_RIFLE.get() instanceof GunItem gun) {
            Identifier modelId = BuiltInRegistries.ITEM.getKey(gun);
            gun.geoRenderProvider.setValue(new GeoRenderProvider() {
                private final Supplier<GeoItemRenderer<GunItem>> renderer =
                        Suppliers.memoize(() -> new GunInHandRenderer(new DefaultedItemGeoModel<>(modelId)));

                @Override
                public @Nullable GeoItemRenderer<GunItem> getGeoItemRenderer() {
                    return this.renderer.get();
                }
            });
        }
    }

    // 3. ★ ライフル構えポーズの登録
    @SubscribeEvent
    public static void registerClientExtensions(RegisterClientExtensionsEvent event) {
        IClientItemExtensions riflePose = new IClientItemExtensions() {
            @Override
            public HumanoidModel.ArmPose getArmPose(LivingEntity entityLiving, InteractionHand hand, ItemStack itemStack) {
                return GunArmPoses.RIFLE.getValue();
            }
        };

        event.registerItem(riflePose, ModItems.CLUNKER_RIFLE.get());
    }
}