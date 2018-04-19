package com.erhodes.fallout.model;

/**
 * The value of a Derived Attribute is based on another Attribute.
 */
public class DerivedAttribute extends Attribute {
    private transient Attribute baseAttribute;
    protected float attributeMultiplier;
    private String parentKey;

    /**
     *
     * @param name
     * @param key
     * @param baseAttribute
     * @param attributeMultiplier
     * @param baseValue
     */
    public DerivedAttribute(String name, String key, Attribute baseAttribute, float attributeMultiplier, int baseValue) {
        super(name, key, baseValue);
        this.baseAttribute = baseAttribute;
        this.attributeMultiplier = attributeMultiplier;
        parentKey = baseAttribute.getKey();
        calculateFinalValue();
    }

    /**
     * Used after deserialization.
     * @param attribute
     */
    public void reloadParentAttribute(Attribute attribute) {
        if (attribute.getKey().equals(parentKey)) {
            baseAttribute = attribute;
        }
        // maybe reinitialize some stuff, I dunno, I just want it to even sort of work
    }

    public String getParentKey() {
        return parentKey;
    }
    @Override
    public void calculateFinalValue() {
        finalValue = mBaseValue + Math.round(baseAttribute.getFinalValue() * attributeMultiplier) + modifier;
    }

    public Attribute getBaseAttribute() {
        return baseAttribute;
    }
}
