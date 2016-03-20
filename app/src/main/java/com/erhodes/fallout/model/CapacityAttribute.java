package com.erhodes.fallout.model;

import android.util.Log;

/**
 * CapacityAttributes are used for frequently changing attributes whose cap is another attribute.
 * For example, health and max_health, ammo and max_ammo.
 * The modifier is used to track the current value, since that is what effects modify.
 * By default, a capacity attribute is at it's maximum value
 */
public class CapacityAttribute extends Attribute {
    Attribute mMaxAttribute;

    public CapacityAttribute(String n, Attribute maxAttribute) {
        name = n;
        mMaxAttribute = maxAttribute;
        mMaxAttribute.calculateFinalValue();
        Log.d("Eric", "max attribute is " + mMaxAttribute.key + ", value is " + mMaxAttribute.getFinalValue());
        modifier = mMaxAttribute.getFinalValue();
    }

    public void addModifier(int m) {
        modifier += m;
        if (modifier > mMaxAttribute.getFinalValue())
            modifier = mMaxAttribute.getFinalValue();
    }

    public void resetToMax() {
        modifier = mMaxAttribute.getFinalValue();
    }

    @Override
    public void calculateFinalValue() {
        if (modifier > mMaxAttribute.getFinalValue())
            modifier = mMaxAttribute.getFinalValue();
    }

    /**
     *
     * @return The maximum value of this attribute, based on it's base attribute
     */
    public int getMaxValue() {
        return mMaxAttribute.getFinalValue();
    }
    @Override
    public int getFinalValue() {
        return modifier;
    }
}
