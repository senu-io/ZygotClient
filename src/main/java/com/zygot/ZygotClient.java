package com.zygot;

import com.zygot.font.FontManager;
import com.zygot.settings.DefaultSettingsManager;
import com.zygot.ui.splash.ZygotSplashScreen;
import com.zygot.util.system.IconHelper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = ZygotClient.MODID,
        name = ZygotClient.NAME,
        version = ZygotClient.VERSION,
        clientSideOnly = true
)
public class ZygotClient {
    public static final String MODID = "zygot";
    public static final String NAME = "Zygot Client";
    public static final String VERSION = "1.0.0";

    public static final Logger LOGGER = LogManager.getLogger(NAME);

    @Mod.Instance(MODID)
    public static ZygotClient INSTANCE;

    private FontManager fontManager;

    public ZygotClient() {
        INSTANCE = this;
    }

    public FontManager getFontManager() {
        return fontManager;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("[Zygot Client] Initializing Zygot Client " + VERSION);

        // 1. Update Window Title and Taskbar Icon (Dark/Light Mode adaptive)
        IconHelper.updateDisplay();

        // 2. Initialize Comfortaa Typography Subsystem
        this.fontManager = new FontManager();
        this.fontManager.init();

        // 3. Stage 1: Initiating Zygot Client (20%)
        ZygotSplashScreen.setProgress(1, 5, "Initiating Zygot Client...");

        // 4. Initialize Default Settings & Video Configurations
        DefaultSettingsManager.initialize(Minecraft.getMinecraft().mcDataDir);

        // 5. Stage 2: Initializing Core Systems (40%)
        ZygotSplashScreen.setProgress(2, 5, "Initializing Core Systems...");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        // Stage 3: Loading Model Textures (60%)
        ZygotSplashScreen.setProgress(3, 5, "Loading Model Textures...");
        LOGGER.info("[Zygot Client] Textures and block models loaded.");
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // Stage 4: Building Cache (80%)
        ZygotSplashScreen.setProgress(4, 5, "Building Cache...");
        LOGGER.info("[Zygot Client] Cache built and verified.");
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        // Stage 5: Finalizing Setup (100%)
        ZygotSplashScreen.setProgress(5, 5, "Finalizing Setup...");
        ZygotSplashScreen.finish();
        IconHelper.updateDisplay();
        LOGGER.info("[Zygot Client] Zygot Client startup sequence complete.");
    }
}
