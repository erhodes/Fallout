package com.erhodes.fallout;

/**
 * Created by Eric on 17/10/2015.
 */
public class Effect {
    String key;
    int magnitude;

    @Override
    public String toString() {
        return "modify " + key + " by " + magnitude;
    }

}
