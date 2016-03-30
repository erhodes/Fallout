package com.erhodes.fallout.model;

import com.erhodes.fallout.model.Attribute;

/**
 * The value of a Derived Attribute is based on another Attribute.
 */
public class DerivedAttribute extends Attribute {
    Attribute mBaseAttribute;
    protected float mAttributeMultiplier;

    /**
     *
     * @param n
     * @param k
     * @param baseAttribute
     * @param attributeMultiplier
     * @param baseValue
     */
    public DerivedAttribute(String n, String k, Attribute baseAttribute, float attributeMultiplier, int baseValue) {
        name = n;
        key = k;
        mBaseAttribute = baseAttribute;
        mAttributeMultiplier = attributeMultiplier;
        mBaseValue = baseValue;
        calculateFinalValue();
    }

    @Override
    public void calculateFinalValue() {
        finalValue = mBaseValue + Math.round(mBaseAttribute.getFinalValue() * mAttributeMultiplier) + modifier;
    }

    public Attribute getBaseAttribute() {
        return mBaseAttribute;
    }
}
