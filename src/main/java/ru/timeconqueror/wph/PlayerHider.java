package ru.timeconqueror.wph;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.timeconqueror.wph.proxy.Proxy;

@Mod(modid = PlayerHider.MODID,
        dependencies = "required-after:forge@[14.23.5.2847,);required-after:wynntils@[1.9.1,)",
        name = PlayerHider.MODNAME,
        version = PlayerHider.VERSION)
public class PlayerHider {
    public static final String MODID = "wynn_player_hider";
    public static final String MODNAME = "Wynn Player Hider";
    public static final String VERSION = "1.0.2";

    @SidedProxy(clientSide = "ru.timeconqueror.wph.proxy.ClientProxy", serverSide = "ru.timeconqueror.wph.proxy.ServerProxy")
    private static Proxy proxy;

    public static final Logger LOGGER = LogManager.getLogger();

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public static void init(FMLInitializationEvent e) {
        proxy.init(e);
    }

    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public static void onServerStarting(FMLServerStartingEvent e) {
    }
}
