package com.erhodes.fallout.model;

/**
 * Created by Eric on 17/10/2015.
 */
public class Effect {
    String key;
    int magnitude, duration;

    public Effect() {}

    /**
     *
     * @param k Attribute key
     * @param m Magnitude of the effect
     */
    public Effect (String k, int m) {
        key = k;
        magnitude = m;
    }

    /**
     *
     * @param k Attribute key
     * @param m Magnitude of the effect
     * @param d Duration of the effect.
     */
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