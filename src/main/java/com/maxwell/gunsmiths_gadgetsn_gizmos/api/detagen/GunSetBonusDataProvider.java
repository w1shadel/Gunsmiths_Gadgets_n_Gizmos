package com.maxwell.gunsmiths_gadgetsn_gizmos.api.detagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy.SetBonusEffect;
import com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.seteffect.*;
import com.maxwell.gunsmiths_gadgetsn_gizmos.init.ModItems;
import com.mojang.serialization.JsonOps;
import io.redspace.irons_artifice.registry.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class GunSetBonusDataProvider implements DataProvider {
    private final PackOutput output;
    private final String modId;
    private final Map<String, JsonObject> bonuses = new HashMap<>();

    public GunSetBonusDataProvider(PackOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }

    public GunSetBonusDataProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        this(output, modId);
    }

    protected void buildSetBonuses() {

        addCustomSetBonus("stormcaller",
                "gunsmiths_gadgetsn_gizmos.set_bonus.stormcaller",
                "gunsmiths_gadgetsn_gizmos.set_bonus.stormcaller.desc",
                List.of(ItemRegistry.CHAIN_LIGHTNING.get(), ItemRegistry.CHAIN_SHOT.get(), ModItems.BREEZE_CYCLONE_MODIFIER.get()),
                0.30, 0.40, -0.30, -1.0, 0,
                "#CEF8FF", "#00F8FF", ParticleTypes.ELECTRIC_SPARK,
                List.of(new LightningStrikeEffect(0.75F))
        );

        addCustomSetBonus("absolute_zero",
                "gunsmiths_gadgetsn_gizmos.set_bonus.absolute_zero",
                "gunsmiths_gadgetsn_gizmos.set_bonus.absolute_zero.desc",
                List.of(ItemRegistry.FROZEN_JACKET.get(), ItemRegistry.SINGULARITY_CHARGE_MODIFIER.get(), ItemRegistry.TRICK_BULLET_MODIFIER.get()),
                0.40, 0.20, -0.30, -1.0, 0,
                "#A8E6FF", "#A8E6FF", ParticleTypes.SNOWFLAKE,
                List.of(new AbsoluteZeroEffect(5.0, 20 * 5))
        );

        addSimpleSetBonus("sculk_phantom",
                "gunsmiths_gadgetsn_gizmos.set_bonus.sculk_phantom",
                "gunsmiths_gadgetsn_gizmos.set_bonus.sculk_phantom.desc",
                List.of(ModItems.SCULK_WHISPER_SILENCER_MODIFIER.get(), ModItems.ECHOING_SONIC_CORE_MODIFIER.get(), ModItems.SCULK_DEVOURER_MODIFIER.get()),
                0.50, 0.30, -0.40, -2.0, 2,
                "#00FFFF", "#00FFFF", ParticleTypes.SCULK_SOUL
        );

        addSimpleSetBonus("crimson_overlord",
                "gunsmiths_gadgetsn_gizmos.set_bonus.crimson_overlord",
                "gunsmiths_gadgetsn_gizmos.set_bonus.crimson_overlord.desc",
                List.of(ModItems.BLOODBOUND_CALAMITY_MODIFIER.get(), ModItems.CRIMSON_SINGULARITY_MODIFIER.get(), ModItems.HEAVY_CORE_IMPACT_MODIFIER.get(), ItemRegistry.STEEL_CORE.get()),
                0.80, 0.40, -0.50, -3.0, 3,
                "#FF0033", "#FF0033", ParticleTypes.CRIMSON_SPORE
        );

        addSimpleSetBonus("tempest_overclock",
                "gunsmiths_gadgetsn_gizmos.set_bonus.tempest_overclock",
                "gunsmiths_gadgetsn_gizmos.set_bonus.tempest_overclock.desc",
                List.of(
                        ModItems.UNSTABLE_OVERCLOCK_MODIFIER.get(),
                        ModItems.BREEZE_CYCLONE_MODIFIER.get(),
                        ModItems.PISTON_RAMROD_MODIFIER.get()
                ),
                0.30,
                0.50,
                -0.30,
                -1.5,
                0,
                "#FFA500",
                "#FFA500",
                ParticleTypes.SPLASH
        );




        addSimpleSetBonus("recoil_control",
                "gunsmiths_gadgetsn_gizmos.set_bonus.recoil_control",
                "gunsmiths_gadgetsn_gizmos.set_bonus.recoil_control.desc",
                List.of(
                        ItemRegistry.GAS_VENT.get(),
                        ItemRegistry.BUFFER_SPRING.get()
                ),
                0.0, 0.0, -0.20, -1.0, 0,
                "#DDDDDD", "#DDDDDD", ParticleTypes.SMOKE
        );

        addSimpleSetBonus("light_skirmisher",
                "gunsmiths_gadgetsn_gizmos.set_bonus.light_skirmisher",
                "gunsmiths_gadgetsn_gizmos.set_bonus.light_skirmisher.desc",
                List.of(
                        ItemRegistry.HAIR_TRIGGER.get(),
                        ItemRegistry.GUN_OIL.get()
                ),
                0.0, 0.15, 0.0, 0.0, 0,
                "#FFD700", "#FFD700", ParticleTypes.CRIT
        );

        addSimpleSetBonus("heavy_impact",
                "gunsmiths_gadgetsn_gizmos.set_bonus.heavy_impact",
                "gunsmiths_gadgetsn_gizmos.set_bonus.heavy_impact.desc",
                List.of(
                        ItemRegistry.LEAD_CORE.get(),
                        ItemRegistry.BREACHING_SHELL.get()
                ),
                0.10, 0.15, 0.0, 0.0, 0,
                "#888888", "#888888", ParticleTypes.POOF
        );

        addSimpleSetBonus("incendiary_scatter",
                "gunsmiths_gadgetsn_gizmos.set_bonus.incendiary_scatter",
                "gunsmiths_gadgetsn_gizmos.set_bonus.incendiary_scatter.desc",
                List.of(
                        ItemRegistry.INCENDIARY_TIP_MODIFIER.get(),
                        ItemRegistry.SCATTERSHOT.get()
                ),
                0.10, 0.10, 0.0, 0.0, 0,
                "#FF6600", "#FF6600", ParticleTypes.FLAME
        );

        addSimpleSetBonus("aero_ricochet",
                "gunsmiths_gadgetsn_gizmos.set_bonus.aero_ricochet",
                "gunsmiths_gadgetsn_gizmos.set_bonus.aero_ricochet.desc",
                List.of(
                        ItemRegistry.WIND_CHAMBER.get(),
                        ItemRegistry.TRICK_BULLET_MODIFIER.get()
                ),
                0.0, 0.20, 0.0, -1.0, 0,
                "#A0E6FF", "#A0E6FF", ParticleTypes.GUST
        );

        addCustomSetBonus("omega_apocalypse",
                "gunsmiths_gadgetsn_gizmos.set_bonus.omega_apocalypse",
                "gunsmiths_gadgetsn_gizmos.set_bonus.omega_apocalypse.desc",
                List.of(
                        ModItems.CRIMSON_SINGULARITY_MODIFIER.get(), 
                        ModItems.CLOCKWORK_GATLING_MODIFIER.get(),   
                        ItemRegistry.SINGULARITY_CHARGE_MODIFIER.get(), 
                        ItemRegistry.OVERCHARGED_POWDER.get(),          
                        ItemRegistry.STEEL_CORE.get()                   
                ),
                1.50, 
                0.80, 
                -0.60, 
                -4.0,  
                5,     
                "#FF0055", "#FF0055", ParticleTypes.PORTAL,
                List.of(new GravitationalCollapseEffect(8.0, 15.0F))
        );



        addCustomSetBonus("cataclysm_stormruler",
                "gunsmiths_gadgetsn_gizmos.set_bonus.cataclysm_stormruler",
                "gunsmiths_gadgetsn_gizmos.set_bonus.cataclysm_stormruler.desc",
                List.of(
                        ModItems.REAPERS_TEMPEST_MODIFIER.get(),     
                        ModItems.ECHOING_SONIC_CORE_MODIFIER.get(),  
                        ModItems.BREEZE_CYCLONE_MODIFIER.get(),      
                        ItemRegistry.CHAIN_LIGHTNING.get(),          
                        ItemRegistry.FROZEN_JACKET.get(),            
                        ItemRegistry.WIND_CHAMBER.get()              
                ),
                1.20, 
                1.00, 
                -0.50, 
                -3.0,
                3,    
                "#00FFFF", "#FFFFFF", ParticleTypes.SONIC_BOOM,
                List.of(
                        new MultiLightningStrikeEffect(3), 
                        new AbsoluteZeroEffect(6.0, 20 * 6) 
                )
        );

        addCustomSetBonus("soul_of_eternity",
                "gunsmiths_gadgetsn_gizmos.set_bonus.soul_of_eternity",
                "gunsmiths_gadgetsn_gizmos.set_bonus.soul_of_eternity.desc",
                List.of(
                        ModItems.CRIMSON_SINGULARITY_MODIFIER.get(),     
                        ModItems.CLOCKWORK_GATLING_MODIFIER.get(),       
                        ModItems.REAPERS_TEMPEST_MODIFIER.get(),         
                        ModItems.MIDAS_TOUCH_CHAMBER_MODIFIER.get(),     
                        ModItems.ECHOING_SONIC_CORE_MODIFIER.get(),      
                        ModItems.SCULK_DEVOURER_MODIFIER.get(),          
                        ModItems.MASTERCRAFTED_TRIGGER_MODIFIER.get(),   
                        ItemRegistry.SINGULARITY_CHARGE_MODIFIER.get(),  
                        ItemRegistry.CHAIN_LIGHTNING.get(),              
                        ItemRegistry.ENCHANTED_BULLET_MODIFIER.get()     
                ),
                3.00,  
                1.50,  
                -0.80, 
                -5.0,  
                10,    
                "#FFFFFF", "#FFD700", ParticleTypes.TOTEM_OF_UNDYING,
                List.of(
                        new GravitationalCollapseEffect(12.0, 30.0F), 
                        new MultiLightningStrikeEffect(5),            
                        new AbsoluteZeroEffect(8.0, 20 * 10),         
                        new CorpsePoisonBloomEffect(6.0F, 20 * 10),   
                        SoulOfEternityEffect.INSTANCE                 
                )
        );
    }

    public void addSimpleSetBonus(String id, String nameKey, String descKey, List<ItemLike> requiredModifiers, double damageMul, double speedMul, double recoilMul, double spreadAdd, int piercingAdd, String trailColor, String muzzleColor, @Nullable ParticleOptions auraParticle) {
        addCustomSetBonus(id, nameKey, descKey, requiredModifiers, damageMul, speedMul, recoilMul, spreadAdd, piercingAdd, trailColor, muzzleColor, auraParticle, List.of());
    }

    public void addCustomSetBonus(String id, String nameKey, String descKey, List<ItemLike> requiredModifiers, double damageMul, double speedMul, double recoilMul, double spreadAdd, int piercingAdd, String trailColor, String muzzleColor, @Nullable ParticleOptions auraParticle, List<SetBonusEffect> effects) {
        JsonObject json = new JsonObject();
        json.addProperty("name", nameKey);
        json.addProperty("description", descKey);

        JsonArray modsArray = new JsonArray();
        for (ItemLike item : requiredModifiers) {
            modsArray.add(BuiltInRegistries.ITEM.getKey(item.asItem()).toString());
        }
        json.add("required_modifiers", modsArray);

        JsonObject bonuses = new JsonObject();
        if (damageMul != 0) bonuses.addProperty("damage_multiplier", damageMul);
        if (speedMul != 0) bonuses.addProperty("bullet_speed_multiplier", speedMul);
        if (recoilMul != 0) bonuses.addProperty("recoil_multiplier", recoilMul);
        if (spreadAdd != 0) bonuses.addProperty("spread_add", spreadAdd);
        if (piercingAdd != 0) bonuses.addProperty("piercing_add", piercingAdd);
        if (trailColor != null) bonuses.addProperty("trail_color", trailColor);
        if (muzzleColor != null) bonuses.addProperty("muzzle_flash_color", muzzleColor);

        if (auraParticle != null) {
            JsonElement particleJson = ParticleTypes.CODEC.encodeStart(JsonOps.INSTANCE, auraParticle).getOrThrow();
            bonuses.add("aura_particle", particleJson);
        }
        json.add("bonuses", bonuses);

        if (!effects.isEmpty()) {
            JsonArray effectsArray = new JsonArray();
            for (SetBonusEffect effect : effects) {
                JsonElement el = SetBonusEffect.CODEC.encodeStart(JsonOps.INSTANCE, effect).getOrThrow();
                effectsArray.add(el);
            }
            json.add("custom_effects", effectsArray);
        }

        this.bonuses.put(id, json);
    }

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput output) {
        this.bonuses.clear();
        buildSetBonuses();
        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (Map.Entry<String, JsonObject> entry : bonuses.entrySet()) {
            Path path = this.output.getOutputFolder(PackOutput.Target.DATA_PACK)
                    .resolve(this.modId)
                    .resolve("gun_set_bonuses")
                    .resolve(entry.getKey() + ".json");
            futures.add(DataProvider.saveStable(output, entry.getValue(), path));
        }
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public @NonNull String getName() {
        return "Gun Set Bonuses: " + this.modId;
    }
}