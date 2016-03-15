package com.erhodes.fallout.model;

import java.util.HashMap;

/**
 * Created by Eric on 23/01/2016.
 */
public abstract class GameObject {
    protected HashMap<String, Attribute> mAttributes;

    GameObject() {
        mAttributes = new HashMap<>();
    }
    public abstract void applyEffect(Effect e);

    public abstract String getName();

    public void removeEffect(Effect e) {
        modifyAttribute(e.key, -e.magnitude);
    }

    public abstract boolean isValidAttribute(String attributeKey);

    public int getAttribute(String attrKey) {
        if (isValidAttribute(attrKey))
            return mAttributes.get(attrKey).getFinalValue();
        return 0;
    }

    public void modifyAttribute(String attrKey, int mag) {
        if (isValidAttribute(attrKey))
            mAttributes.get(attrKey).addModifier(mag);
    }
}
