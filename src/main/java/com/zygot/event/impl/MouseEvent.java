package com.zygot.event.impl;

import com.zygot.event.Event;

public class MouseEvent extends Event {
    private final int button;
    private final boolean state;

    public MouseEvent(int button, boolean state) {
        this.button = button;
        this.state = state;
    }

    public int getButton() {
        return button;
    }

    public boolean isState() {
        return state;
    }
}
