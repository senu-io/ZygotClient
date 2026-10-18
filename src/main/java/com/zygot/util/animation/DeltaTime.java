package com.zygot.util.animation;

public class DeltaTime {
    private static int deltaTime = 16;

    public static int getDeltaTime() {
        return Math.max(1, Math.min(deltaTime, 100));
    }

    public static void setDeltaTime(int dt) {
        deltaTime = dt;
    }
}
