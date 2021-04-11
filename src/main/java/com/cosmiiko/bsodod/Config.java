package com.cosmiiko.bsodod;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

// Thank god for https://cadiboo.github.io/tutorials/1.15.1/forge/3.3-config/ and screw Forge's docs
@Mod.EventBusSubscriber(modid = "bsodod", bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    public static final ClientConfig CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        final Pair<ClientConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static class ClientConfig {
        public final ForgeConfigSpec.ConfigValue<String> openUrl;
        public final ForgeConfigSpec.ConfigValue<Integer> delayInSecs;
        public final ForgeConfigSpec.ConfigValue<Boolean> defused;

        public ClientConfig(ForgeConfigSpec.Builder builder) {
            openUrl = builder
                    .comment("URL Opened before the bluescreen. Set to 'none' to disable.")
                    .define("openUrl", "https://www.youtube.com/watch?v=dQw4w9WgXcQ");

            delayInSecs = builder
                    .comment("Delay in seconds before the blue screen.")
                    .define("delayInSecs", 5);

            defused = builder
                    .comment("Whether or not the mod is defused. If true, you will not blue screen.")
                    .define("defused", false);
        }
    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == Config.CLIENT_SPEC) {
            Config.bakeConfig();
        }
    }

    public static String openUrl;
    public static Integer delayInSecs;
    public static Boolean defused;

    public static void bakeConfig() {
        openUrl = CLIENT.openUrl.get();
        delayInSecs = CLIENT.delayInSecs.get();
        defused = CLIENT.defused.get();
    }
}
