package com.erhodes.fallout;

/**
 * Created by Eric on 17/10/2015.
 */
public class Effect {
    String key;
    int magnitude;

    public Effect() {}

    public Effect(String k, int m) {
        key = k;
        magnitude = m;
    }

    @Override
    public String toString() {
        return "modify " + key + " by " + magnitude;
    }

}
