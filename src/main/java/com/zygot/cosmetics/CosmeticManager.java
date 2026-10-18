package com.zygot.cosmetics;

import com.zygot.Zygot;
import com.zygot.cosmetics.impl.*;
import com.zygot.event.EventTarget;
import com.zygot.event.impl.RenderPlayerEvent;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.*;

/**
 * Manages the cosmetic catalog, equipped accessories, and player model rendering.
 * Fully extensible to support unlimited capes, wings, hats, and halos.
 */
public class CosmeticManager {

    private final Map<String, Cosmetic> catalog = new LinkedHashMap<>();
    private final Map<CosmeticType, String> equipped = new HashMap<>();

    public void init() {
        Zygot.INSTANCE.eventBus.register(this);

        // ==========================================
        // 1. Capes Catalog (Static & Animated GIFs)
        // ==========================================
        registerCosmetic(new CosmeticCape("cape_zygot", "Zygot Signature Cape", new ResourceLocation("zygot", "cosmetics/cape.png")));
        registerCosmetic(new CosmeticCape("cape_obsidian", "Obsidian Dark Cape", new ResourceLocation("zygot", "cosmetics/cape bg.png")));
        registerCosmetic(new CosmeticCape("cape_nebula", "Violet Nebula Cape", new ResourceLocation("zygot", "cosmetics/bg.png")));

        // Animated GIF Capes
        registerCosmetic(new CosmeticCape("cape_animated_fire", "Animated Fire Cape", new ResourceLocation("zygot", "cosmetics/capes/animated_fire_cape.gif"), true));
        registerCosmetic(new CosmeticCape("cape_glitchstorm", "Glitchstorm Cape", new ResourceLocation("zygot", "cosmetics/capes/glitchstorm.gif"), true));
        registerCosmetic(new CosmeticCape("cape_lightning", "Lightning Cape", new ResourceLocation("zygot", "cosmetics/capes/lightning.gif"), true));
        registerCosmetic(new CosmeticCape("cape_astelic_badlion", "Astelic Badlion Cape", new ResourceLocation("zygot", "cosmetics/capes/astelic_badlion_cape.gif"), true));
        registerCosmetic(new CosmeticCape("cape_adorable_bunny", "Adorable Bunny Girl Cape", new ResourceLocation("zygot", "cosmetics/capes/adorable_bunny_girl.gif"), true));
        registerCosmetic(new CosmeticCape("cape_enchanter", "Enchanter Cape", new ResourceLocation("zygot", "cosmetics/capes/enchanter.gif"), true));
        registerCosmetic(new CosmeticCape("cape_purpled_cloak", "Purpled Cloak", new ResourceLocation("zygot", "cosmetics/capes/purpled_cloak.gif"), true));
        registerCosmetic(new CosmeticCape("cape_rickroll", "Rickroll Cape", new ResourceLocation("zygot", "cosmetics/capes/rickroll.gif"), true));
        registerCosmetic(new CosmeticCape("cape_shiny_pickaxe", "Shiny Pickaxe V2 Cape", new ResourceLocation("zygot", "cosmetics/capes/shiny_pickaxe_v2.gif"), true));
        registerCosmetic(new CosmeticCape("cape_thirtyvirus", "ThirtyVirus Code Red Cape", new ResourceLocation("zygot", "cosmetics/capes/thirtyvirus_code_red.gif"), true));
        registerCosmetic(new CosmeticCape("cape_vers_glitched", "Vers Glitched Cape", new ResourceLocation("zygot", "cosmetics/capes/vers_glitched.gif"), true));

        // ==========================================
        // 2. Wings Catalog
        // ==========================================
        registerCosmetic(new CosmeticWings("wings_dragon", "Dragon Wings", new ResourceLocation("zygot", "cosmetics/d-wings.png")));
        registerCosmetic(new CosmeticWings("wings_crystal", "Crystal Wings", new ResourceLocation("zygot", "cosmetics/crystalwings.png")));
        registerCosmetic(new CosmeticWings("wings_galaxy", "Galaxy Wings", new ResourceLocation("zygot", "cosmetics/galaxywings.png")));
        registerCosmetic(new CosmeticWings("wings_angel", "Angel Wings", new ResourceLocation("zygot", "cosmetics/wings.png")));

        // ==========================================
        // 3. Halos & Hats Catalog
        // ==========================================
        registerCosmetic(new CosmeticHalo("halo_golden", "Golden Halo", new ResourceLocation("zygot", "cosmetics/Halo.png")));
        registerCosmetic(new CosmeticWitchHat("hat_witch", "Witch Hat", new ResourceLocation("zygot", "cosmetics/witch.png")));
        registerCosmetic(new CosmeticCap("hat_cap", "Baseball Cap", new ResourceLocation("zygot", "cosmetics/cap.png")));

        // Default equipped items
        equip("cape_animated_fire");
        equip("wings_dragon");
    }

    public void registerCosmetic(Cosmetic cosmetic) {
        catalog.put(cosmetic.getId(), cosmetic);
    }

    public void equip(String id) {
        Cosmetic c = catalog.get(id);
        if (c != null) {
            equipped.put(c.getType(), id);
        }
    }

    public void unequip(CosmeticType type) {
        equipped.remove(type);
    }

    public boolean isEquipped(String id) {
        Cosmetic c = catalog.get(id);
        if (c == null) return false;
        String eq = equipped.get(c.getType());
        return id.equalsIgnoreCase(eq);
    }

    public void toggleEquip(String id) {
        if (isEquipped(id)) {
            Cosmetic c = catalog.get(id);
            if (c != null) unequip(c.getType());
        } else {
            equip(id);
        }
    }

    @EventTarget
    public void onRenderPlayer(RenderPlayerEvent e) {
        renderPlayerCosmetics(e.getEntityPlayer(), e.getModelPlayer(), e.getPartialTicks());
    }

    public void renderPlayerCosmetics(AbstractClientPlayer player, ModelPlayer model, float partialTicks) {
        for (String id : equipped.values()) {
            Cosmetic c = catalog.get(id);
            if (c != null) {
                c.render(player, model, partialTicks);
            }
        }
    }

    public Collection<Cosmetic> getAllCosmetics() {
        return catalog.values();
    }

    public List<Cosmetic> getCosmeticsByType(CosmeticType type) {
        List<Cosmetic> list = new ArrayList<>();
        for (Cosmetic c : catalog.values()) {
            if (c.getType() == type) {
                list.add(c);
            }
        }
        return list;
    }

    public Map<CosmeticType, String> getEquippedMap() {
        return equipped;
    }
}
