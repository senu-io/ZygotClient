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
        // 1. Capes Catalog
        // ==========================================
        registerCosmetic(new CosmeticCape("cape_zygot", "Zygot Signature Cape", new ResourceLocation("zygot/cosmetics/cape.png")));
        registerCosmetic(new CosmeticCape("cape_obsidian", "Obsidian Dark Cape", new ResourceLocation("zygot/cosmetics/cape bg.png")));
        registerCosmetic(new CosmeticCape("cape_nebula", "Violet Nebula Cape", new ResourceLocation("zygot/cosmetics/bg.png")));

        // ==========================================
        // 2. Wings Catalog
        // ==========================================
        registerCosmetic(new CosmeticWings("wings_dragon", "Dragon Wings", new ResourceLocation("zygot/cosmetics/d-wings.png")));
        registerCosmetic(new CosmeticWings("wings_crystal", "Crystal Wings", new ResourceLocation("zygot/cosmetics/crystalwings.png")));
        registerCosmetic(new CosmeticWings("wings_galaxy", "Galaxy Wings", new ResourceLocation("zygot/cosmetics/galaxywings.png")));
        registerCosmetic(new CosmeticWings("wings_angel", "Angel Wings", new ResourceLocation("zygot/cosmetics/wings.png")));

        // ==========================================
        // 3. Halos & Hats Catalog
        // ==========================================
        registerCosmetic(new CosmeticHalo("halo_golden", "Golden Halo", new ResourceLocation("zygot/cosmetics/Halo.png")));
        registerCosmetic(new CosmeticWitchHat("hat_witch", "Witch Hat", new ResourceLocation("zygot/cosmetics/witch.png")));
        registerCosmetic(new CosmeticCap("hat_cap", "Baseball Cap", new ResourceLocation("zygot/cosmetics/cap.png")));

        // Default equipped items
        equip("cape_zygot");
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
