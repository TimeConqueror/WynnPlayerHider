package ru.timeconqueror.wph;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
@Config(modid = PlayerHider.MODID, type = Config.Type.INSTANCE)
public class CHConfig {
    @Config.LangKey("config.wph.type")
    @Config.Comment({"Determines the type of player visualization change upon other player approach.",
            "Possible values: none, shrink, vanish",
            "Default: none"})
    public static String type = "none";

    @Config.LangKey("config.wph.distance")
    @Config.Comment({"Determines the radius of player visualization change.",
            "Default: 7"})
    @Config.RangeDouble(min = 4)
    public static double distance = 7;

    @Config.LangKey("config.wph.size")
    @Config.Comment({"Only for visualization type 'shrink'. Determines the minimum size of shrunk players.",
            "Default: 0.3"})
    @Config.RangeDouble(min = 0.01, max = 1)
    public static double size = 0.3;

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.getModID().equals(PlayerHider.MODID)) {
            ConfigManager.sync(PlayerHider.MODID, Config.Type.INSTANCE);
        }
    }
}