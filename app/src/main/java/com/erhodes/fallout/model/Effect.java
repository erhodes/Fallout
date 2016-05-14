package com.erhodes.fallout.model;

/**
 * Created by Eric on 17/10/2015.
 */
public class Effect {
    String mKey;
    int mMagnitude, mDuration;
    boolean mRecurring;

    public Effect() {}

    /**
     *
     * @param key Attribute key
     * @param magnitude Magnitude of the effect
     */
    public Effect (String key, int magnitude) {
        this(key, magnitude, 0, false);
    }

    /**
     *
     * @param key Attribute key
     * @param magnitude Magnitude of the effect
     * @param duration Duration of the effect.
     */
    public Effect(String key, int magnitude, int duration) {
        this(key, magnitude, duration, false);
    }

    public Effect(String key, int magnitude, int duration, boolean recurring) {
        mKey = key;
        mMagnitude = magnitude;
        mDuration = duration;
        mRecurring = recurring;
    }

    public Effect(Effect original) {
        mKey = original.mKey;
        mMagnitude = original.mMagnitude;
        mDuration = original.mDuration;
        mRecurring = original.mRecurring;
    }

    public String getKey() { return mKey; }
    public int getMagnitude() { return mMagnitude; }
    public int getDuration() { return mDuration; }
    public boolean isRecurring() { return mRecurring; }

    @Override
    public String toString() {
        String result = "modify " + mKey + " by " + mMagnitude;
        if (mDuration > 0)
            result += " for " + mDuration + " rounds";
        return result;
    }

}
