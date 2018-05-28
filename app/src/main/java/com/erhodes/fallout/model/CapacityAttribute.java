package com.erhodes.fallout.model;

import android.util.Log;

/**
 * CapacityAttributes are used for frequently changing attributes whose cap is another attribute.
 * For example, health and max_health, ammo and max_ammo.
 * The modifier is used to track the current value, since that is what effects modify.
 * By default, a capacity attribute is at it's maximum value
 */
public class CapacityAttribute extends Attribute {
    private transient Attribute maxAttribute;
    private String parentKey;

    CapacityAttribute() {
        super();
    }

    public CapacityAttribute(String n, String key, Attribute maxAttribute) {
        super(n, key, 0);
        this.parentKey = maxAttribute.getKey();
        this.maxAttribute = maxAttribute;
        this.maxAttribute.calculateFinalValue();
//        Log.d("Eric", "max attribute is " + maxAttribute.key + ", value is " + maxAttribute.getFinalValue());
        modifier = this.maxAttribute.getFinalValue();
    }

    /**
     * Used after deserialization.
     * @param attribute
     */
    public void reloadParentAttribute(Attribute attribute) {
        if (attribute.getKey().equals(parentKey)) {
            maxAttribute = attribute;
        }
        // maybe reinitialize some stuff, I dunno, I just want it to even sort of work
    }

    public String getParentKey() {
        return parentKey;
    }

    public void addModifier(int m) {
        modifier += m;
        if (modifier > maxAttribute.getFinalValue())
            modifier = maxAttribute.getFinalValue();
    }

    public void resetToMax() {
        modifier = maxAttribute.getFinalValue();
    }

    @Override
    public void calculateFinalValue() {
        if (modifier > maxAttribute.getFinalValue())
            modifier = maxAttribute.getFinalValue();
    }

    /**
     *
     * @return The maximum value of this attribute, based on it's base attribute
     */
    public int getMaxValue() {
        return maxAttribute.getFinalValue();
    }
    @Override
    public int getFinalValue() {
        return modifier;
    }
}
