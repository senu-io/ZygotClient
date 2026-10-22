package com.zygot;

import com.zygot.settings.DefaultSettingsManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
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

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("[Zygot Client] Initializing Zygot Client " + VERSION);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        DefaultSettingsManager.initialize(Minecraft.getMinecraft().mcDataDir);
        LOGGER.info("[Zygot Client] Base Forge Client with OptiFine loaded successfully.");
    }
}
