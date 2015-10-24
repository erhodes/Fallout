package com.erhodes.fallout;

/**
 * Created by Eric on 20/10/2015.
 */
public class Attribute {
    String name, key;
    int mBaseValue, modifier, finalValue;

    Attribute(){}

    Attribute(String n, String k, int b) {
        name = n;
        key = k;
        mBaseValue = b;
    }

    public void addModifier(int m) {
        modifier += m;
    }
    public void calculateFinalValue() {
        finalValue = mBaseValue + modifier;
    }
    public int getFinalValue() {
        return finalValue;
    }
}
