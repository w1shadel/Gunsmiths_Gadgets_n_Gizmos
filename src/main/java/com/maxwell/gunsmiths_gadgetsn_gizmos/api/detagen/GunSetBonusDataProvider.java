package com.maxwell.gunsmiths_gadgetsn_gizmos.api.detagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.maxwell.gunsmiths_gadgetsn_gizmos.api.synergy.SetBonusEffect;
import com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.seteffect.AbsoluteZeroEffect;
import com.maxwell.gunsmiths_gadgetsn_gizmos.modifier.seteffect.LightningStrikeEffect;
import com.maxwell.gunsmiths_gadgetsn_gizmos.registry.ModItems;
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
        // ① ストームコーラー (電気火花オーラ + 落雷エフェクト)
        addCustomSetBonus("stormcaller",
                "gunsmiths_gadgetsn_gizmos.set_bonus.stormcaller",
                "gunsmiths_gadgetsn_gizmos.set_bonus.stormcaller.desc",
                List.of(ItemRegistry.CHAIN_LIGHTNING.get(), ItemRegistry.CHAIN_SHOT.get(), ModItems.BREEZE_CYCLONE_MODIFIER.get()),
                0.30, 0.40, -0.30, -1.0, 0,
                "#CEF8FF", "#00F8FF", ParticleTypes.ELECTRIC_SPARK,
                List.of(new LightningStrikeEffect(0.75F))
        );

        // ② 絶対零度 (雪の結晶オーラ + 完全凍結エフェクト)
        addCustomSetBonus("absolute_zero",
                "gunsmiths_gadgetsn_gizmos.set_bonus.absolute_zero",
                "gunsmiths_gadgetsn_gizmos.set_bonus.absolute_zero.desc",
                List.of(ItemRegistry.FROZEN_JACKET.get(), ItemRegistry.SINGULARITY_CHARGE_MODIFIER.get(), ItemRegistry.TRICK_BULLET_MODIFIER.get()),
                0.40, 0.20, -0.30, -1.0, 0,
                "#A8E6FF", "#A8E6FF", ParticleTypes.SNOWFLAKE,
                List.of(new AbsoluteZeroEffect(5.0, 20 * 5))
        );

        // ③ スカルク・ファントム (深淵の魂オーラ)
        addSimpleSetBonus("sculk_phantom",
                "gunsmiths_gadgetsn_gizmos.set_bonus.sculk_phantom",
                "gunsmiths_gadgetsn_gizmos.set_bonus.sculk_phantom.desc",
                List.of(ModItems.SCULK_WHISPER_SILENCER_MODIFIER.get(), ModItems.ECHOING_SONIC_CORE_MODIFIER.get(), ModItems.SCULK_DEVOURER_MODIFIER.get()),
                0.50, 0.30, -0.40, -2.0, 2,
                "#00FFFF", "#00FFFF", ParticleTypes.SCULK_SOUL
        );

        // ④ クリムゾン・オーバーロード (深紅の胞子オーラ)
        addSimpleSetBonus("crimson_overlord",
                "gunsmiths_gadgetsn_gizmos.set_bonus.crimson_overlord",
                "gunsmiths_gadgetsn_gizmos.set_bonus.crimson_overlord.desc",
                List.of(ModItems.BLOODBOUND_CALAMITY_MODIFIER.get(), ModItems.CRIMSON_SINGULARITY_MODIFIER.get(), ModItems.HEAVY_CORE_IMPACT_MODIFIER.get(), ItemRegistry.STEEL_CORE.get()),
                0.80, 0.40, -0.50, -3.0, 3,
                "#FF0033", "#FF0033", ParticleTypes.CRIMSON_SPORE
        );

        // ⑤ テンペスト・オーバークロック (水しぶき/突風オーラ)
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
    }

    // ★ エフェクトなし用のシンプルなヘルパー（引数12個）
    public void addSimpleSetBonus(String id, String nameKey, String descKey, List<ItemLike> requiredModifiers, double damageMul, double speedMul, double recoilMul, double spreadAdd, int piercingAdd, String trailColor, String muzzleColor, @Nullable ParticleOptions auraParticle) {
        addCustomSetBonus(id, nameKey, descKey, requiredModifiers, damageMul, speedMul, recoilMul, spreadAdd, piercingAdd, trailColor, muzzleColor, auraParticle, List.of());
    }

    // ★ エフェクト付き用の完全ヘルパー（引数13個）
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

        // ParticleOptions のエンコード
        if (auraParticle != null) {
            JsonElement particleJson = ParticleTypes.CODEC.encodeStart(JsonOps.INSTANCE, auraParticle).getOrThrow();
            bonuses.add("aura_particle", particleJson);
        }
        json.add("bonuses", bonuses);

        // SetBonusEffect のエンコード
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