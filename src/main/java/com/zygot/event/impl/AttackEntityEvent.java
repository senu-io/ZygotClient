package com.zygot.event.impl;

import com.zygot.event.CancellableEvent;
import net.minecraft.entity.Entity;

public class AttackEntityEvent extends CancellableEvent {
    private final Entity target;

    public AttackEntityEvent(Entity target) {
        this.target = target;
    }

    public Entity getTarget() {
        return target;
    }
}
