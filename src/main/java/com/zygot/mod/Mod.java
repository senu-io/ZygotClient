package com.zygot.mod;

import com.zygot.Zygot;
import com.zygot.mod.setting.Setting;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all Zygot Client modules.
 */
public abstract class Mod {

    protected final Minecraft mc = Minecraft.getMinecraft();

    private final String name;
    private final String description;
    private final Category category;
    private boolean toggled;
    private int keybind;

    private final List<Setting> settings = new ArrayList<>();

    public Mod(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;
    }

    public void onEnable() {
        Zygot.INSTANCE.eventBus.register(this);
    }

    public void onDisable() {
        Zygot.INSTANCE.eventBus.unregister(this);
    }

    public void toggle() {
        setToggled(!toggled);
    }

    public void setToggled(boolean toggled) {
        if (this.toggled != toggled) {
            this.toggled = toggled;
            if (this.toggled) {
                onEnable();
            } else {
                onDisable();
            }
        }
    }

    public void addSettings(Setting... s) {
        for (Setting setting : s) {
            this.settings.add(setting);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Setting> T getSetting(String name) {
        for (Setting s : settings) {
            if (s.getName().equalsIgnoreCase(name)) {
                return (T) s;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isToggled() {
        return toggled;
    }

    public int getKeybind() {
        return keybind;
    }

    public void setKeybind(int keybind) {
        this.keybind = keybind;
    }

    public List<Setting> getSettings() {
        return settings;
    }
}
