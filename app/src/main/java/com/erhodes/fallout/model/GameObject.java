package com.erhodes.fallout.model;

import android.arch.persistence.room.Ignore;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eric on 23/01/2016.
 */
public abstract class GameObject {
    protected HashMap<String, Attribute> attributes;

    @Ignore
    protected ArrayList<Attribute> regularAttributes;
    @Ignore
    protected ArrayList<DerivedAttribute> derivedAttributes;
    @Ignore
    protected ArrayList<CapacityAttribute> capacityAttributes;

    GameObject() {
        attributes = new HashMap<>();
        regularAttributes = new ArrayList<>();
        derivedAttributes = new ArrayList<>();
        capacityAttributes = new ArrayList<>();
    }
    public abstract void applyEffect(Effect e);

    public abstract String getName();

    public void removeEffect(Effect e) {
        if (!e.isRecurring())
            modifyAttribute(e.mKey, -e.mMagnitude);
    }

    public abstract boolean isValidAttribute(String attributeKey);

    /**
     * Return the final value of a given attribute
     * @param attrKey
     * @return
     */
    public int getAttributeValue(String attrKey) {
        if (isValidAttribute(attrKey))
            return attributes.get(attrKey).getFinalValue();
        return 0;
    }

    public Attribute getAttribute(String attrKey) {
        if (isValidAttribute(attrKey))
            return attributes.get(attrKey);
        return null;
    }

    public void modifyAttribute(String attrKey, int mag) {
        if (isValidAttribute(attrKey))
            attributes.get(attrKey).addModifier(mag);
    }

    public void addAttribute(Attribute attribute) {
        attributes.put(attribute.key, attribute);
        regularAttributes.add(attribute);
    }

    public void addCapacityAttribute(CapacityAttribute attribute) {
        attributes.put(attribute.key, attribute);
        capacityAttributes.add(attribute);
    }
    public void addDerivedAttribute(DerivedAttribute attribute) {
        attributes.put(attribute.key, attribute);
        derivedAttributes.add(attribute);
    }

    public HashMap<String, Attribute> getAttributes() {
        return attributes;
    }
    public void setAttributes(HashMap<String, Attribute> attributes) {
        this.attributes = attributes;
        for (Attribute attribute : attributes.values()) {
            if (attribute instanceof  CapacityAttribute) {
                ((CapacityAttribute) attribute).reloadParentAttribute(attributes.get(((CapacityAttribute) attribute).getParentKey()));
            } else if (attribute instanceof DerivedAttribute) {
                DerivedAttribute derivedAttribute = (DerivedAttribute) attribute;
                derivedAttribute.reloadParentAttribute(attributes.get(derivedAttribute.getParentKey()));
            }
            attribute.calculateFinalValue();
        }
    }
    // getters and setters for room
    public ArrayList<Attribute> getRegularAttributes() {
        return regularAttributes;
    }

    public void setRegularAttributes(ArrayList<Attribute> regularAttributes) {
        this.regularAttributes.clear();
        for (Attribute attribute : regularAttributes) {
            Log.d("Eric","adding " + attribute.getKey());
            addAttribute(attribute);
        }
        Log.d("Eric","now there's " + attributes.size());
    }

    public ArrayList<DerivedAttribute> getDerivedAttributes() {
        return derivedAttributes;
    }

    public void setDerivedAttributes(ArrayList<DerivedAttribute> newAttributes) {
        for (DerivedAttribute derivedAttribute : derivedAttributes) {
            attributes.remove(derivedAttribute.getKey());
        }
        derivedAttributes.clear();
        for (DerivedAttribute derivedAttribute : newAttributes) {
            Log.d("Eric","adding " + derivedAttribute.getKey());
            addDerivedAttribute(derivedAttribute);
        }
    }

    public ArrayList<CapacityAttribute> getCapacityAttributes() {
        return capacityAttributes;
    }

    public void setCapacityAttributes(ArrayList<CapacityAttribute> newAttributes) {
        Log.d("Eric","set capacity attrits to " + newAttributes.size());
        // remove any old capacity attributes
        for (CapacityAttribute capacityAttribute : capacityAttributes) {
            attributes.remove(capacityAttribute.getKey());
        }
        this.capacityAttributes.clear();
        for (CapacityAttribute attribute : newAttributes) {
            Log.d("Eric","adding " + attribute.getKey());
            attribute.reloadParentAttribute(attributes.get(attribute.getParentKey()));
            addCapacityAttribute(attribute);
        }
        Log.d("Eric","now there's " + attributes.size());
    }
}
