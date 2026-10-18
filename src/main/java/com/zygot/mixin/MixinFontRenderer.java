package com.zygot.mixin;

import net.minecraft.client.gui.FontRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FontRenderer.class)
public abstract class MixinFontRenderer {
    // Allows overriding or hooking into the base FontRenderer
}
