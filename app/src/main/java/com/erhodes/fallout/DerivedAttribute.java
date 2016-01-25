package com.erhodes.fallout;

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
    DerivedAttribute(String n, String k, Attribute baseAttribute, float attributeMultiplier, int baseValue) {
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
}
