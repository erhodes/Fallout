package com.erhodes.fallout;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Eric on 17/10/2015.
 */
public class Character {
    private HashMap<String, Attribute> mAttributes;
    private HashSet<String> mAcquiredPerks;
    private ArrayList<Item> mInventory;
    public int mCarriedWeight;
    public String name;


    Character(String n) {
        name = n;
        mAttributes = new HashMap<>();
        mAcquiredPerks = new HashSet<>();
        mInventory = new ArrayList<>();

        mAttributes.put(Attributes.STRENGTH, new Attribute("Strength",Attributes.STRENGTH,4));
        mAttributes.put(Attributes.ENDURANCE, new Attribute("Endurance",Attributes.ENDURANCE,4));
        mAttributes.put(Attributes.RESOLVE, new Attribute("Resolve",Attributes.RESOLVE, 4));

        //maybe derived attributes should each be their own class. Might be a better way to organize the logic
        mAttributes.put(Attributes.MORALE, new DerivedAttribute("Morale",Attributes.MORALE,mAttributes.get(Attributes.RESOLVE)) {
            @Override
            public void calculateFinalValue() {
                finalValue = mBase.getFinalValue() * 3 + modifier;
            }
        });

        mAttributes.put(Attributes.WEIGHT_LIMIT, new DerivedAttribute("Weight Limit", Attributes.WEIGHT_LIMIT, mAttributes.get(Attributes.STRENGTH)) {
            @Override
            public void calculateFinalValue() {
                finalValue = mBase.getFinalValue() * 25 + modifier;
            }
        });
        calculateAttributes();
    }

    public void calculateAttributes() {
        for (String s: Attributes.getPrimaryAttributes()) {
            getAttribute(s);
        }
        for (String s: Attributes.getDerivedAttributes()) {
            getAttribute(s);
        }
    }
    public void modifyAttribute(String attrKey, int mag) {
        mAttributes.get(attrKey).addModifier(mag);
        calculateAttributes();
    }

    public int getAttribute(String attrKey) {
        return mAttributes.get(attrKey).getFinalValue();
    }
    public void applyEffect(Effect e) {
        modifyAttribute(e.key, e.magnitude);
    }

    public void removeEffect(Effect e) {
        modifyAttribute(e.key, -e.magnitude);
    }

    // Perk Methods
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

    // Inventory Methods

    /**
     * Add an item to the character's inventory. Returns true if successful, false if the character
     * cannot carry the additional weight.
     * @param i
     * @return
     */
    public boolean acquireItem(Item i) {
        if (i.weight + mCarriedWeight > getAttribute(Attributes.WEIGHT_LIMIT)) {
            return false;
        }
        mCarriedWeight += i.weight;
        mInventory.add(i);
        return true;
    }

    public ArrayList<Item> getInventory() {
        return mInventory;
    }
}
