package com.erhodes.fallout;

/**
 * Created by Eric on 17/10/2015.
 */
public class Effect {
    String key;
    int magnitude, duration;

    public Effect() {}

    public Effect (String k, int m) {
        key = k;
        magnitude = m;
    }
    public Effect(String k, int m, int d) {
        key = k;
        magnitude = m;
        duration = d;
    }

    public Effect(Effect original) {
        key = original.key;
        magnitude = original.magnitude;
        duration = original.duration;
    }

    @Override
    public String toString() {
        return "modify " + key + " by " + magnitude + " for " + duration;
    }

}
