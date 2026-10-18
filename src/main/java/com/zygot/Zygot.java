package com.zygot;

import com.zygot.branding.ClientBranding;
import com.zygot.branding.ZygotProfile;
import com.zygot.config.ConfigManager;
import com.zygot.cosmetics.CosmeticManager;
import com.zygot.event.EventBus;
import com.zygot.font.FontManager;
import com.zygot.mod.ModManager;
import com.zygot.theme.ThemeManager;
import com.zygot.ui.hud.ZygotHudEditor;
import com.zygot.ui.modmenu.ZygotModMenu;
import com.zygot.ui.splash.ZygotLoadingScreen;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.lwjgl.opengl.Display;

import java.io.File;

/**
 * Main Client Singleton for Zygot Client (Minecraft 1.8.9).
 */
@Mod(
        modid = Zygot.MODID,
        name = Zygot.NAME,
        version = Zygot.VERSION,
        acceptedMinecraftVersions = "[1.8.9]",
        clientSideOnly = true
)
public class Zygot {

    public static final String MODID = "zygotclient";
    public static final String NAME = "Zygot Client";
    public static final String VERSION = "1.0.0";

    @Instance(MODID)
    public static Zygot INSTANCE;

    public Minecraft mc = Minecraft.getMinecraft();

    // Core Managers
    public EventBus eventBus;
    public ThemeManager themeManager;
    public FontManager fontManager;
    public ModManager modManager;
    public CosmeticManager cosmeticManager;
    public ConfigManager configManager;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        INSTANCE = this;

        // 1. Initialize Event Bus
        this.eventBus = new EventBus();

        // 2. Load external branding/profile overrides if present
        File configDir = new File(mc.mcDataDir, "zygot");
        configDir.mkdirs();
        ZygotProfile.load(new File(configDir, "zygot_profile.json"));
        ZygotProfile.saveSample(new File(configDir, "zygot_profile.sample.json"));

        // 3. Initialize Theme Engine
        this.themeManager = new ThemeManager();

        // 4. Initialize Comfortaa TTF Typography System
        this.fontManager = new FontManager();
        this.fontManager.init();

        // 5. Update Window Title
        Display.setTitle(ClientBranding.getFormattedWindowTitle());
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ZygotLoadingScreen.setProgress(3, 10, "Initializing Mod Manager...");

        // 6. Initialize 35+ Mods
        this.modManager = new ModManager();
        this.modManager.init();

        ZygotLoadingScreen.setProgress(6, 10, "Loading 3D Cosmetics Engine...");

        // 7. Initialize 3D Cosmetics Subsystem
        this.cosmeticManager = new CosmeticManager();
        this.cosmeticManager.init();

        ZygotLoadingScreen.setProgress(8, 10, "Loading Configurations...");

        // 8. Initialize Configuration & Persistence
        this.configManager = new ConfigManager();
        this.configManager.loadConfig();

        // Register shutdown hook for auto-saving
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (configManager != null) {
                    configManager.saveConfig();
                }
            } catch (Exception e) {
                System.err.println("[Zygot] Error saving configuration on shutdown: " + e.getMessage());
            }
        }));

        ZygotLoadingScreen.setProgress(10, 10, "Ready!");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        System.out.println("[Zygot] " + ClientBranding.NAME + " v" + ClientBranding.VERSION + " successfully initialized!");
    }
}
