package com.maxwell.gunsmiths_gadgetsn_gizmos.client;

import com.maxwell.gunsmiths_gadgetsn_gizmos.GunsmithsGadgetsnGizmos;
import com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy.GunSetBonus;
import com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy.GunSetBonusManager;
import io.redspace.irons_artifice.item.GunItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

import java.util.List;

@EventBusSubscriber(modid = GunsmithsGadgetsnGizmos.MODID, value = Dist.CLIENT)
public class ClientGunAuraEvents {
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.isPaused() || mc.level == null || mc.player == null) return;
        Player player = mc.player;
        ItemStack heldGun = player.getMainHandItem();
        if (heldGun.getItem() instanceof GunItem) {
            List<GunSetBonus> bonuses = GunSetBonusManager.getMatchingBonuses(heldGun);
            if (!bonuses.isEmpty()) {
                RandomSource random = mc.level.getRandom();
                if (player.tickCount % 2 == 0) {
                    Vec3 look = player.getLookAngle();
                    Vec3 right = look.cross(new Vec3(0, 1, 0)).normalize();
                    Vec3 gunPos = player.getEyePosition()
                            .add(look.scale(0.6))
                            .add(right.scale(player.getMainArm() == net.minecraft.world.entity.HumanoidArm.RIGHT ? 0.35 : -0.35))
                            .add(0, -0.2, 0);
                    for (GunSetBonus bonus : bonuses) {
                        ParticleOptions particle = bonus.bonuses().auraParticle().orElse(ParticleTypes.ENCHANT);
                        mc.level.addParticle(
                                particle,
                                gunPos.x + (random.nextDouble() - 0.5) * 0.1,
                                gunPos.y + (random.nextDouble() - 0.5) * 0.1,
                                gunPos.z + (random.nextDouble() - 0.5) * 0.1,
                                (random.nextDouble() - 0.5) * 0.02,
                                0.03 + random.nextDouble() * 0.02,
                                (random.nextDouble() - 0.5) * 0.02
                        );
                    }
                }
            }
        }
    }
}