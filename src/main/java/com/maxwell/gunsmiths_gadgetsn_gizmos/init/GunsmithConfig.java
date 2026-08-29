package com.maxwell.gunsmiths_gadgetsn_gizmos.init;

import net.neoforged.neoforge.common.ModConfigSpec;

public class GunsmithConfig {
    public static final ModConfigSpec COMMON_SPEC;
    public static final Common COMMON;
    public static final ModConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;

    static {
        ModConfigSpec.Builder commonBuilder = new ModConfigSpec.Builder();
        COMMON = new Common(commonBuilder);
        COMMON_SPEC = commonBuilder.build();
        ModConfigSpec.Builder clientBuilder = new ModConfigSpec.Builder();
        CLIENT = new Client(clientBuilder);
        CLIENT_SPEC = clientBuilder.build();
    }

    public static class Common {
        public final ModConfigSpec.IntValue maxMarksmanPerVillage;
        public final ModConfigSpec.IntValue maxExtraModifierSlots;
        public final ModConfigSpec.DoubleValue bloodboundHpCost;
        public final ModConfigSpec.DoubleValue magneticPouchChance;
        public final ModConfigSpec.DoubleValue gamblersRingChance;

        public Common(ModConfigSpec.Builder builder) {
            builder.comment("Town Marksman Settings").push("town_marksman");
            maxMarksmanPerVillage = builder
                    .comment("Maximum number of Town Marksmen that can naturally spawn in a single village.")
                    .defineInRange("maxMarksmanPerVillage", 5, 0, Integer.MAX_VALUE);
            builder.pop();
            builder.comment("Gunsmith Bench & Upgrades").push("gunsmith_bench");
            maxExtraModifierSlots = builder
                    .comment("Maximum extra modifier slots that can be added via the Gunsmith Bench.")
                    .defineInRange("maxExtraModifierSlots", 3, 1, Integer.MAX_VALUE);
            builder.pop();
            builder.comment("Item & Curios Balance").push("balance");
            bloodboundHpCost = builder
                    .comment("HP damage dealt to the shooter per shot with Bloodbound Calamity (2.0 = 1 Heart).")
                    .defineInRange("bloodboundHpCost", 2.0, 0.0, Integer.MAX_VALUE);
            magneticPouchChance = builder
                    .comment("Chance to recover ammo/casings when firing with Magnetic Pouch equipped (0.25 = 25%).")
                    .defineInRange("magneticPouchChance", 0.25, 0.0, Integer.MAX_VALUE);
            gamblersRingChance = builder
                    .comment("Chance for free ammo / critical damage with Gambler's Ring (0.15 = 15%).")
                    .defineInRange("gamblersRingChance", 0.15, 0.0, Integer.MAX_VALUE);
            builder.pop();
        }
    }

    public static class Client {
        public final ModConfigSpec.BooleanValue enableAshStormShader;
        public final ModConfigSpec.BooleanValue enableGunAuraParticles;

        public Client(ModConfigSpec.Builder builder) {
            builder.comment("Visual & Particle Effects").push("visuals");
            enableAshStormShader = builder
                    .comment("Enable custom screen ash storm shader during the Apostle boss fight.")
                    .define("enableAshStormShader", true);
            enableGunAuraParticles = builder
                    .comment("Enable ambient aura particles on guns when Synergy Set Bonuses are active.")
                    .define("enableGunAuraParticles", true);
            builder.pop();
        }
    }
}