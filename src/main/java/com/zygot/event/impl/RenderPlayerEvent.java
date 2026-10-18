package com.zygot.event.impl;

import com.zygot.event.Event;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;

public class RenderPlayerEvent extends Event {
    private final AbstractClientPlayer entityPlayer;
    private final ModelPlayer modelPlayer;
    private final float partialTicks;

    public RenderPlayerEvent(AbstractClientPlayer entityPlayer, ModelPlayer modelPlayer, float partialTicks) {
        this.entityPlayer = entityPlayer;
        this.modelPlayer = modelPlayer;
        this.partialTicks = partialTicks;
    }

    public AbstractClientPlayer getEntityPlayer() {
        return entityPlayer;
    }

    public ModelPlayer getModelPlayer() {
        return modelPlayer;
    }

    public float getPartialTicks() {
        return partialTicks;
    }
}
