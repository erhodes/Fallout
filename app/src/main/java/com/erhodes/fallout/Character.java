package com.erhodes.fallout;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Eric on 17/10/2015.
 */
public class Character {
    private HashMap<String, Integer> mAttributes;
    private HashSet<String> mAcquiredPerks;
    public String name;


    Character(String n) {
        name = n;
        mAttributes = new HashMap<String, Integer>();
        mAcquiredPerks = new HashSet<String>();
    }

    public void modifyAttribute(String attrKey, int mag) {
        int base = getAttribute(attrKey);
        mAttributes.put(attrKey, base + mag);
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

    public boolean hasPerk(Perk p) {
        Log.d("Eric", "contains perk name? " + mAcquiredPerks.contains(p.id));
        return mAcquiredPerks.contains(p.id);
    }
    public boolean applyPerk(Perk p){
        if (hasPerk(p))
            return false;
        mAcquiredPerks.add(p.id);
        for (Effect e: p.effects) {
            applyEffect(e);
        }
        return true;
    }

    public boolean removePerk(Perk p) {
        if (!hasPerk(p))
            return false;
        mAcquiredPerks.remove(p.id);
        for (Effect e: p.effects) {
            removeEffect(e);
        }
        return true;
    }
}
