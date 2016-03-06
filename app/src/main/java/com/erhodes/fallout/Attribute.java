package com.erhodes.fallout;

import android.util.Log;

/**
 * Created by Eric on 20/10/2015.
 */
public class Attribute {
    String name, key;
    protected int mBaseValue, modifier, finalValue;

    Attribute(){}

    Attribute(String n, String k, int b) {
        name = n;
        key = k;
        mBaseValue = b;
        calculateFinalValue();
    }

    public void addModifier(int m) {
        modifier += m;
        calculateFinalValue();
    }
    public void calculateFinalValue() {
        finalValue = mBaseValue + modifier;
    }
    public int getFinalValue() {
        return finalValue;
    }
}
