package com.erhodes.fallout.model;

import android.util.Log;

/**
 * Created by Eric on 20/10/2015.
 */
public class Attribute {
    public String type;
    protected String name, key;
    protected int mBaseValue, modifier, finalValue;

    public Attribute(){
        type = getClass().getSimpleName();
    }

    public Attribute(String name, String key, int base) {
        this();
        this.name = name;
        this.key = key;
        mBaseValue = base;
    }

    public void addModifier(int m) {
        modifier += m;
        calculateFinalValue();
    }

    public String getName() {
        return name;
    }
    public String getKey() {
        return key;
    }
    public void calculateFinalValue() {
        finalValue = mBaseValue + modifier;
    }
    public int getFinalValue() {
        return finalValue;
    }
}
