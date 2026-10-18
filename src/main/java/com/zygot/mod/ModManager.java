package com.zygot.mod;

import com.zygot.Zygot;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.KeyEvent;
import com.zygot.mod.impl.hud.*;
import com.zygot.mod.impl.perf.*;
import com.zygot.mod.impl.pvp.*;
import com.zygot.mod.impl.render.*;
import com.zygot.mod.impl.qol.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry and manager for all 35+ Zygot Client modules.
 */
public class ModManager {

    private final List<Mod> mods = new ArrayList<>();

    public void init() {
        Zygot.INSTANCE.eventBus.register(this);

        // ==========================================
        // 1. PvP Mods
        // ==========================================
        register(new KeystrokesMod());
        register(new CpsMod());
        register(new OldAnimationsMod());
        register(new ReachDisplayMod());
        register(new ComboCounterMod());
        register(new HitColorMod());
        register(new CrosshairMod());
        register(new NoHurtCamMod());
        register(new FreelookMod());
        register(new ArmorStatusMod());
        register(new PotionEffectsMod());

        // ==========================================
        // 2. HUD & Info Mods
        // ==========================================
        register(new FpsMod());
        register(new PingMod());
        register(new CoordinatesMod());
        register(new DirectionMod());
        register(new ScoreboardMod());
        register(new BossbarMod());
        register(new ClockMod());
        register(new SpeedometerMod());
        register(new ServerIPMod());
        register(new DayCounterMod());
        register(new WatermarkMod());

        // ==========================================
        // 3. Render & Visual Mods
        // ==========================================
        register(new FullbrightMod());
        register(new ZoomMod());
        register(new TimeChangerMod());
        register(new MotionBlurMod());
        register(new MenuBlurMod());
        register(new ItemPhysicsMod());
        register(new BlockOverlayMod());
        register(new LowFireMod());

        // ==========================================
        // 4. Performance Mods
        // ==========================================
        register(new ParticleCullingMod());
        register(new ParticleMultiplierMod());
        register(new FastMathMod());
        register(new ChunkAnimatorMod());

        // ==========================================
        // 5. Quality of Life (QoL) Mods
        // ==========================================
        register(new ToggleSprintMod());
        register(new ToggleSneakMod());
        register(new AutoGGMod());
        register(new NickHiderMod());
        register(new ScrollTooltipsMod());
        register(new ChatTweaksMod());
        register(new ScreenshotMod());
        register(new TNTTimerMod());
    }

    private void register(Mod mod) {
        this.mods.add(mod);
    }

    @EventTarget
    public void onKey(KeyEvent e) {
        if (e.getKey() == 0) return;
        for (Mod mod : mods) {
            if (mod.getKeybind() == e.getKey()) {
                mod.toggle();
            }
        }
    }

    public List<Mod> getMods() {
        return mods;
    }

    public List<HudMod> getHudMods() {
        List<HudMod> hudMods = new ArrayList<>();
        for (Mod m : mods) {
            if (m instanceof HudMod) {
                hudMods.add((HudMod) m);
            }
        }
        return hudMods;
    }

    public List<Mod> getModsByCategory(Category category) {
        List<Mod> list = new ArrayList<>();
        for (Mod m : mods) {
            if (m.getCategory() == category) {
                list.add(m);
            }
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public <T extends Mod> T getMod(Class<T> modClass) {
        for (Mod m : mods) {
            if (m.getClass().equals(modClass)) {
                return (T) m;
            }
        }
        return null;
    }

    public Mod getModByName(String name) {
        for (Mod m : mods) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }
}
