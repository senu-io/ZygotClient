package com.zygot.event.impl;

import com.zygot.event.CancellableEvent;

public class ChatMessageEvent extends CancellableEvent {
    private String message;

    public ChatMessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
