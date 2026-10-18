package com.zygot.util.animation;

/**
 * Delta-time based smooth animation controller for UI elements.
 */
public class Animate {

    private float value;
    private float min = 0f;
    private float max = 1f;
    private float speed = 50f;
    private float time = 0f;
    private boolean reversed = false;
    private Easing ease = Easing.CUBIC_OUT;

    public Animate() {
    }

    public Animate setEase(Easing ease) {
        this.ease = ease;
        return this;
    }

    public Animate setMin(float min) {
        this.min = min;
        return this;
    }

    public Animate setMax(float max) {
        this.max = max;
        return this;
    }

    public Animate setSpeed(float speed) {
        this.speed = speed;
        return this;
    }

    public Animate setReversed(boolean reversed) {
        this.reversed = reversed;
        return this;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void reset() {
        this.time = min;
        this.value = min;
    }

    public Animate update() {
        float dt = DeltaTime.getDeltaTime() * 0.001F * speed;
        if (reversed) {
            if (time > min) time -= dt;
        } else {
            if (time < max) time += dt;
        }
        time = Math.max(min, Math.min(max, time));
        float easeVal = ease.ease(time, min, max, max);
        this.value = Math.max(min, Math.min(max, easeVal));
        return this;
    }

    public float getValue() {
        return value;
    }

    public int getValueInt() {
        return Math.round(value);
    }

    public boolean hasFinished() {
        return reversed ? value <= min : value >= max;
    }
}
