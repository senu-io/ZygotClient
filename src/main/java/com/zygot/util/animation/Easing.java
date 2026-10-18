package com.zygot.util.animation;

public enum Easing {
    LINEAR {
        @Override
        public float ease(float t, float b, float c, float d) {
            return c * t / d + b;
        }
    },
    QUAD_OUT {
        @Override
        public float ease(float t, float b, float c, float d) {
            return -c * (t /= d) * (t - 2) + b;
        }
    },
    CUBIC_OUT {
        @Override
        public float ease(float t, float b, float c, float d) {
            return c * ((t = t / d - 1) * t * t + 1) + b;
        }
    },
    CUBIC_IN_OUT {
        @Override
        public float ease(float t, float b, float c, float d) {
            if ((t /= d / 2) < 1) return c / 2 * t * t * t + b;
            return c / 2 * ((t -= 2) * t * t + 2) + b;
        }
    };

    public abstract float ease(float t, float b, float c, float d);
}
