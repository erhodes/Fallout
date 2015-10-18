package com.erhodes.fallout;

import java.util.HashMap;

/**
 * Created by Eric on 17/10/2015.
 */
public class Character {
    private HashMap<String, Integer> mAttributes;
    public String name;

    public static final String STRENGTH = "strength";
    public static final String RESOLVE = "resolve";

    Character(String n) {
        name = n;
        mAttributes = new HashMap<String, Integer>();
    }

    public void modifyAttribute(String attrKey, int mag) {
        mAttributes.put(attrKey, mag);
    }

    public int getAttribute(String attrKey) {
        Integer value = mAttributes.get(attrKey);
        if (value == null) {
            value = 0;
        }
        return value;
    }
    public void applyEffect(Effect e) {
        modifyAttribute(e.key, e.magnitude);
    }

    public void removeEffect(Effect e) {
        modifyAttribute(e.key, -e.magnitude);
    }

    public void applyPerk(Perk p){
        for (Effect e: p.effects) {
            applyEffect(e);
        }
    }

    public void removePerk(Perk p) {
        for (Effect e: p.effects) {
            removeEffect(e);
        }
    }
}
